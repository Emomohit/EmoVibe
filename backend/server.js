const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const crypto = require('crypto');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
    res.send('<h1>EMOVibes Together Server is Running! 🚀</h1><p>Open the EMOVibes app to use the Listen Together feature.</p>');
});

const PORT = 8080;

const sessions = new Map(); // sessionId -> sessionData
const codes = new Map(); // code -> sessionId

function generateCode() {
    return Math.floor(100000 + Math.random() * 900000).toString();
}

app.post('/v1/together/sessions', (req, res) => {
    const { hostDisplayName, settings } = req.body;
    
    const sessionId = crypto.randomUUID();
    let code = generateCode();
    while (codes.has(code)) {
        code = generateCode();
    }
    
    const hostKey = crypto.randomUUID();
    const guestKey = crypto.randomUUID();
    
    const host = req.headers.host || `localhost:${PORT}`;
    const wsUrl = `ws://${host}/v1/together/ws`;
    
    const sessionData = {
        sessionId,
        code,
        hostKey,
        guestKey,
        settings,
        hostConnection: null,
        guests: new Map(), // participantId -> ws connection
    };
    
    sessions.set(sessionId, sessionData);
    codes.set(code, sessionId);
    
    console.log(`Session created: ${code} by ${hostDisplayName}`);
    
    res.json({
        sessionId,
        code,
        hostKey,
        guestKey,
        wsUrl,
        settings
    });
});

app.post('/v1/together/sessions/resolve', (req, res) => {
    const { code } = req.body;
    const sessionId = codes.get(code);
    
    if (!sessionId) {
        return res.status(404).json({ error: "Session not found" });
    }
    
    const sessionData = sessions.get(sessionId);
    const host = req.headers.host || `localhost:${PORT}`;
    const wsUrl = `ws://${host}/v1/together/ws`;
    
    res.json({
        sessionId,
        guestKey: sessionData.guestKey,
        wsUrl,
        settings: sessionData.settings
    });
});

const server = http.createServer(app);
const wss = new WebSocket.Server({ server, path: '/v1/together/ws' });

wss.on('connection', (ws) => {
    let participantId = null;
    let sessionId = null;
    let isHost = false;
    let sessionData = null;
    
    ws.on('message', (message) => {
        try {
            const data = JSON.parse(message.toString());
            const type = data.type;
            
            if (type === 'client_hello') {
                sessionId = data.sessionId;
                sessionData = sessions.get(sessionId);
                
                if (!sessionData) {
                    ws.send(JSON.stringify({ type: 'server_error', sessionId, message: 'Invalid session' }));
                    ws.close(4000, 'Invalid session');
                    return;
                }
                
                participantId = crypto.randomUUID();
                
                if (data.sessionKey === sessionData.hostKey) {
                    isHost = true;
                    sessionData.hostConnection = ws;
                    console.log(`Host joined session ${sessionData.code}`);
                } else if (data.sessionKey === sessionData.guestKey) {
                    isHost = false;
                    sessionData.guests.set(participantId, ws);
                    console.log(`Guest joined session ${sessionData.code}`);
                } else {
                    ws.send(JSON.stringify({ type: 'server_error', sessionId, message: 'Invalid session key' }));
                    ws.close(4000, 'Invalid session key');
                    return;
                }
                
                const welcome = {
                    type: 'server_welcome',
                    protocolVersion: 1,
                    sessionId: sessionId,
                    participantId: participantId,
                    role: isHost ? 'HOST' : 'GUEST',
                    isPending: !isHost && sessionData.settings.requireHostApprovalToJoin,
                    settings: sessionData.settings
                };
                
                ws.send(JSON.stringify(welcome));
                
                if (!isHost && sessionData.hostConnection) {
                    const participantJoined = {
                        type: welcome.isPending ? 'join_request' : 'participant_joined',
                        sessionId: sessionId,
                        participant: {
                            id: participantId,
                            name: data.displayName || 'Guest',
                            isHost: false,
                            isPending: welcome.isPending,
                            isConnected: true
                        }
                    };
                    sessionData.hostConnection.send(JSON.stringify(participantJoined));
                }
                
            } else {
                if (!sessionData) return;
                
                if (isHost) {
                    if (type === 'join_decision' && data.participantId) {
                        const guest = sessionData.guests.get(data.participantId);
                        if (guest) guest.send(JSON.stringify(data));
                        for (const [id, guestWs] of sessionData.guests.entries()) {
                            if (guestWs !== ws) guestWs.send(JSON.stringify(data));
                        }
                    } else if (type === 'kick' || type === 'ban') {
                        const guest = sessionData.guests.get(data.participantId);
                        if (guest) {
                            guest.send(JSON.stringify(data));
                            guest.close(1000, 'Removed by host');
                        }
                    } else {
                        for (const [id, guestWs] of sessionData.guests.entries()) {
                            if (guestWs.readyState === WebSocket.OPEN) {
                                guestWs.send(JSON.stringify(data));
                            }
                        }
                    }
                } else {
                    if (sessionData.hostConnection && sessionData.hostConnection.readyState === WebSocket.OPEN) {
                        sessionData.hostConnection.send(JSON.stringify(data));
                    }
                }
            }
        } catch (err) {
            console.error("Message error:", err);
        }
    });

    ws.on('close', () => {
        if (!sessionData) return;
        
        if (isHost) {
            console.log(`Host left session ${sessionData.code}`);
            for (const [id, guestWs] of sessionData.guests.entries()) {
                if (guestWs.readyState === WebSocket.OPEN) {
                    guestWs.send(JSON.stringify({
                        type: 'server_error',
                        sessionId,
                        message: 'Host ended the session'
                    }));
                    guestWs.close(1000, 'Host left');
                }
            }
            sessions.delete(sessionId);
            codes.delete(sessionData.code);
        } else {
            console.log(`Guest ${participantId} left session ${sessionData.code}`);
            sessionData.guests.delete(participantId);
            if (sessionData.hostConnection && sessionData.hostConnection.readyState === WebSocket.OPEN) {
                sessionData.hostConnection.send(JSON.stringify({
                    type: 'participant_left',
                    sessionId,
                    participantId
                }));
            }
        }
    });
});

server.listen(PORT, '0.0.0.0', () => {
    console.log(`Together Server running on port ${PORT}`);
});
