# EMOVibes 🎵
*Feel the Music, Live the Vibe.*

🌐 **Official Website & Download:** [emovibes.vercel.app](https://emovibes.vercel.app/)

Welcome to **EMOVibes**, a modern, premium, and purely emotional music player built to provide the ultimate ad-free, secure, and beautiful listening experience directly on your Android device.

---

## 🛑 Problem Statement
In today's digital era, the majority of mainstream music streaming apps force users into subscription traps, bombard them with intrusive advertisements, and continuously track user listening data. Most local music players are visually dull and lack integrations with modern music databases to fetch high-quality metadata, lyrics, or album art. There was a glaring need for an open, secure, deeply customizable, and beautiful music player that respects user privacy while delivering a premium aesthetic.

## 🎯 How I Addressed the Project and Why I Made It
**Why I Made It:** 
I built EMOVibes because I wanted a music experience that matches the user's emotional state—free from corporate tracking and paywalls. Music is personal, and the tools we use to listen to it should be just as intimate and respectful of our privacy.

**How I Addressed It:** 
I completely refactored an open-source music player core, rebranded it to EMOVibes, and overhauled the infrastructure to ensure it works entirely offline. I integrated APIs like InnerTube (YouTube), Spotify, and LastFM purely for fetching metadata, ensuring the music source is vast but the app itself remains an independent entity. I also implemented a state-of-the-art dark mode UI featuring glowing soundwaves to provide a deeply immersive aesthetic.

## 🛠️ Approaches Used
- **Offline-First Architecture:** Implemented Room SQLite for local database caching, ensuring 100% offline playback capability for downloaded songs without any mandatory internet requirement.
- **Deep Rebranding & Theming:** Automated deep refactoring scripts using Python to globally rename internal namespaces, package structures, and resources. I overhauled the UI/UX using Jetpack Compose to deliver a sleek, dark-mode-first aesthetic with vivid neon gradients.
- **Multi-API Aggregation:** Aggregated data from InnerTube (for audio streams), Spotify (for artist metadata), and LRCLIB (for synced lyrics) to provide a rich music profile without requiring the user to hold accounts on those platforms.
- **Automated SDK Compilation:** Built a robust Gradle/PowerShell pipeline to handle Android SDK license acceptance and multi-architecture APK compilation programmatically.

## 🔍 Findings
- **Performance Overhead in Multi-Module Builds:** I discovered that compiling a multi-module Kotlin Android app with KSP (Kotlin Symbol Processing) and Room auto-migrations requires significant JVM heap space (minimum 8GB).
- **Deep Rename Complexities:** Finding and replacing strings across thousands of files revealed that Android's resource compilation is highly sensitive to binary image names (ic_launcher) and XML namespace references, requiring specialized Python scripts to handle binary and text data distinctly.
- **Privacy is Achievable:** It is entirely possible to provide a "Spotify-level" metadata experience (lyrics, artist art, bios) without embedding a single tracker or analytics SDK into the application.

## 🌟 Impact Created
- **Empowered Users:** Users now have a completely free, ad-less, and tracker-free premium music application.
- **Enhanced Privacy:** By utilizing local Room databases and avoiding Firebase/Google Analytics, user listening habits remain strictly on their devices.
- **Visual Delight:** The new premium dark mode and emotional soundwave UI offer a drastically improved user experience compared to stock music players.
- **Seamless Portability:** The generated Universal APK allows the app to be seamlessly installed and enjoyed across almost any modern Android device without compatibility issues.

---
*Created by Mohit*
