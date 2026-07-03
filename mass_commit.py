import os
import subprocess

# Run git ls-files
result = subprocess.run(['git', 'ls-files', '--others', '--exclude-standard'], capture_output=True, text=True, encoding='utf-8')
files = [f for f in result.stdout.split('\n') if f]

chunk_size = 30
counter = 1

for i in range(0, len(files), chunk_size):
    chunk = files[i:i+chunk_size]
    with open('chunk.txt', 'w', encoding='utf-8', newline='\n') as f:
        f.write('\n'.join(chunk))
    
    subprocess.run(['git', 'add', '--pathspec-from-file=chunk.txt'])
    subprocess.run(['git', 'commit', '-m', f'feat: add app infrastructure components (batch {counter})'])
    counter += 1

subprocess.run(['git', 'push', 'origin', 'main'])
print('Massive push completed successfully!')
