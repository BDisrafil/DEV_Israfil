package com.example.data.local

import com.example.data.model.CategoryEntity
import com.example.data.model.ProjectEntity

object SeedDataProvider {
    val defaultCategories = listOf(
        CategoryEntity(id = "cat-html", name = "HTML", iconName = "html", colorHex = "#E34F26", description = "Modern semantic HTML templates and landing pages"),
        CategoryEntity(id = "cat-css", name = "CSS", iconName = "css", colorHex = "#1572B6", description = "Tailwind, animations, grid layouts and glassmorphic designs"),
        CategoryEntity(id = "cat-js", name = "JavaScript", iconName = "javascript", colorHex = "#F7DF1E", description = "Interactive vanilla JS components, canvas games, and tools"),
        CategoryEntity(id = "cat-php", name = "PHP", iconName = "php", colorHex = "#777BB4", description = "Backend scripts, REST APIs, authentication and MySQL integrations"),
        CategoryEntity(id = "cat-python", name = "Python", iconName = "python", colorHex = "#3776AB", description = "Automation scripts, data scrapers, bots, and AI utilities"),
        CategoryEntity(id = "cat-react", name = "React", iconName = "react", colorHex = "#61DAFB", description = "Modern React & Next.js single-page applications and UI kits"),
        CategoryEntity(id = "cat-android", name = "Android", iconName = "android", colorHex = "#3DDC84", description = "Jetpack Compose apps, Room databases, and Kotlin utilities"),
        CategoryEntity(id = "cat-webapps", name = "Web Apps", iconName = "webapps", colorHex = "#A855F7", description = "Full-stack SaaS boilerplates, dashboards, and productivity tools"),
        CategoryEntity(id = "cat-games", name = "Games", iconName = "games", colorHex = "#EC4899", description = "Retro arcade, canvas, puzzle, and WebGL web games"),
        CategoryEntity(id = "cat-other", name = "Other", iconName = "other", colorHex = "#00F0FF", description = "Snippets, cheat sheets, configs, and starter templates")
    )

    val defaultProjects = listOf(
        ProjectEntity(
            id = "proj-calc-glass",
            title = "Premium Neumorphic & Glass Calculator",
            slug = "premium-neumorphic-calculator",
            description = "A responsive, futuristic glassmorphic calculator with history logging, keyboard shortcuts, memory operations, and smooth ripple animations built with HTML, CSS, and vanilla JavaScript.",
            category = "JavaScript",
            technologies = "HTML, CSS, JavaScript",
            version = "1.2.0",
            fileSize = "1.4 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_calculator",
            liveDemoUrl = "https://devisrafil.demo/calculator",
            downloadCount = 1245,
            status = "PUBLISHED",
            isFeatured = true,
            createdAt = System.currentTimeMillis() - 86400000L * 2,
            sourceFilesJson = """[
                {"name":"index.html","path":"index.html","language":"html","content":"<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n  <meta charset=\"UTF-8\">\n  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n  <title>DEV_Israfil Glass Calculator</title>\n  <link rel=\"stylesheet\" href=\"style.css\">\n</head>\n<body>\n  <div class=\"glass-calculator\">\n    <div class=\"display-screen\">\n      <div class=\"history\" id=\"calcHistory\"></div>\n      <div class=\"current-input\" id=\"calcInput\">0</div>\n    </div>\n    <div class=\"buttons-grid\">\n      <button class=\"btn-action\" onclick=\"clearAll()\">AC</button>\n      <button class=\"btn-action\" onclick=\"deleteLast()\">DEL</button>\n      <button class=\"btn-action\" onclick=\"appendOp('%')\">%</button>\n      <button class=\"btn-op\" onclick=\"appendOp('/')\">&divide;</button>\n      <button class=\"btn-num\" onclick=\"appendNum('7')\">7</button>\n      <button class=\"btn-num\" onclick=\"appendNum('8')\">8</button>\n      <button class=\"btn-num\" onclick=\"appendNum('9')\">9</button>\n      <button class=\"btn-op\" onclick=\"appendOp('*')\">&times;</button>\n      <button class=\"btn-num\" onclick=\"appendNum('4')\">4</button>\n      <button class=\"btn-num\" onclick=\"appendNum('5')\">5</button>\n      <button class=\"btn-num\" onclick=\"appendNum('6')\">6</button>\n      <button class=\"btn-op\" onclick=\"appendOp('-')\">&minus;</button>\n      <button class=\"btn-num\" onclick=\"appendNum('1')\">1</button>\n      <button class=\"btn-num\" onclick=\"appendNum('2')\">2</button>\n      <button class=\"btn-num\" onclick=\"appendNum('3')\">3</button>\n      <button class=\"btn-op\" onclick=\"appendOp('+')\">+</button>\n      <button class=\"btn-num\" onclick=\"appendNum('0')\">0</button>\n      <button class=\"btn-num\" onclick=\"appendNum('.')\">.</button>\n      <button class=\"btn-equal\" onclick=\"calculate()\">=</button>\n    </div>\n  </div>\n  <script src=\"script.js\"></script>\n</body>\n</html>"},
                {"name":"style.css","path":"style.css","language":"css","content":"* {\n  box-sizing: border-box;\n  margin: 0;\n  padding: 0;\n  font-family: 'Inter', system-ui, sans-serif;\n}\nbody {\n  min-height: 100vh;\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  background: radial-gradient(circle at top, #1e1b4b, #030712);\n  color: #f8fafc;\n}\n.glass-calculator {\n  background: rgba(15, 23, 42, 0.75);\n  backdrop-filter: blur(24px);\n  border: 1px solid rgba(255, 255, 255, 0.12);\n  box-shadow: 0 25px 50px -12px rgba(0, 240, 255, 0.25);\n  border-radius: 28px;\n  padding: 28px;\n  width: 360px;\n}\n.display-screen {\n  background: rgba(2, 6, 23, 0.6);\n  border-radius: 18px;\n  padding: 20px;\n  margin-bottom: 24px;\n  text-align: right;\n  border: 1px solid rgba(0, 240, 255, 0.2);\n}\n.history { font-size: 14px; color: #94a3b8; min-height: 20px; }\n.current-input { font-size: 38px; font-weight: 700; color: #00f0ff; overflow-x: auto; }\n.buttons-grid {\n  display: grid;\n  grid-template-columns: repeat(4, 1fr);\n  gap: 12px;\n}\nbutton {\n  height: 60px;\n  border: none;\n  border-radius: 16px;\n  font-size: 18px;\n  font-weight: 600;\n  cursor: pointer;\n  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);\n  background: rgba(30, 41, 59, 0.7);\n  color: #f8fafc;\n}\nbutton:active { transform: scale(0.95); }\n.btn-op { background: rgba(168, 85, 247, 0.3); color: #c084fc; }\n.btn-action { background: rgba(239, 68, 68, 0.2); color: #f87171; }\n.btn-equal {\n  grid-column: span 2;\n  background: linear-gradient(135deg, #00f0ff, #a855f7);\n  color: #030712;\n  font-weight: 700;\n}"},
                {"name":"script.js","path":"script.js","language":"javascript","content":"let currentInput = '0';\nlet historyStr = '';\nlet resetNext = false;\n\nfunction updateDisplay() {\n  document.getElementById('calcInput').innerText = currentInput;\n  document.getElementById('calcHistory').innerText = historyStr;\n}\n\nfunction appendNum(num) {\n  if (currentInput === '0' || resetNext) {\n    currentInput = num;\n    resetNext = false;\n  } else {\n    currentInput += num;\n  }\n  updateDisplay();\n}\n\nfunction appendOp(op) {\n  historyStr = currentInput + ' ' + op;\n  resetNext = true;\n  updateDisplay();\n}\n\nfunction clearAll() {\n  currentInput = '0';\n  historyStr = '';\n  resetNext = false;\n  updateDisplay();\n}\n\nfunction deleteLast() {\n  currentInput = currentInput.slice(0, -1) || '0';\n  updateDisplay();\n}\n\nfunction calculate() {\n  try {\n    const expr = (historyStr + ' ' + currentInput).replace(/×/g, '*').replace(/÷/g, '/');\n    const result = Function('\"use strict\"; return (' + expr + ')')();\n    historyStr = expr + ' =';\n    currentInput = String(Number(result.toFixed(6)));\n    resetNext = true;\n    updateDisplay();\n  } catch (e) {\n    currentInput = 'Error';\n    updateDisplay();\n  }\n}"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"========================================\n DEV_Israfil - Premium Coding Resources\n Project: Neumorphic & Glass Calculator\n Author: DEV_Israfil\n License: MIT\n========================================\n\nHOW TO USE:\n1. Open index.html in any modern browser (Chrome, Edge, Firefox, Safari).\n2. Enjoy the responsive glassmorphism UI!\n\nFor more coding projects, tutorials and updates:\nVisit DEV_Israfil Platform."}
            ]"""
        ),
        ProjectEntity(
            id = "proj-dev-portfolio",
            title = "Modern Cyberpunk Developer Portfolio",
            slug = "modern-cyberpunk-developer-portfolio",
            description = "A stunning developer portfolio template featuring interactive 3D terminal, floating code particles, project showcase modal, dynamic contact form, and light/dark theme switcher.",
            category = "HTML",
            technologies = "HTML, CSS, JavaScript, WebAudio",
            version = "2.1.0",
            fileSize = "2.8 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_portfolio",
            liveDemoUrl = "https://devisrafil.demo/portfolio",
            downloadCount = 3840,
            status = "PUBLISHED",
            isFeatured = true,
            createdAt = System.currentTimeMillis() - 86400000L * 5,
            sourceFilesJson = """[
                {"name":"index.html","path":"index.html","language":"html","content":"<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n  <meta charset=\"UTF-8\">\n  <title>DEV_Israfil Portfolio</title>\n  <link rel=\"stylesheet\" href=\"style.css\">\n</head>\n<body>\n  <header class=\"navbar\">\n    <div class=\"logo\">&lt;DEV_Israfil/&gt;</div>\n    <nav>\n      <a href=\"#projects\">Projects</a>\n      <a href=\"#skills\">Skills</a>\n      <a href=\"#contact\">Contact</a>\n    </nav>\n  </header>\n  <section class=\"hero\">\n    <h1>Building The Future Of Code</h1>\n    <p>Full-Stack Developer & UI/UX Designer crafting high-performance digital experiences.</p>\n  </section>\n</body>\n</html>"},
                {"name":"style.css","path":"style.css","language":"css","content":"body { background: #080c14; color: #f8fafc; font-family: sans-serif; }\n.navbar { display: flex; justify-content: space-between; padding: 20px 40px; background: rgba(15,23,42,0.8); }\n.logo { font-size: 24px; font-weight: bold; color: #00f0ff; }\n.hero { padding: 100px 40px; text-align: center; }"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"DEV_Israfil Cyberpunk Portfolio Source Code\nReady for customization."}
            ]"""
        ),
        ProjectEntity(
            id = "proj-react-weather",
            title = "AeroCast Realtime Weather Dashboard",
            slug = "aerocast-realtime-weather-dashboard",
            description = "A feature-packed React weather web app with 7-day hourly forecasting, animated atmospheric conditions, geolocation detection, UV index gauges, and city bookmarks.",
            category = "React",
            technologies = "React, TypeScript, TailwindCSS, ChartJS",
            version = "1.5.0",
            fileSize = "3.2 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_weather",
            liveDemoUrl = "https://devisrafil.demo/aerocast",
            downloadCount = 2190,
            status = "PUBLISHED",
            isFeatured = true,
            createdAt = System.currentTimeMillis() - 86400000L * 7,
            sourceFilesJson = """[
                {"name":"App.jsx","path":"src/App.jsx","language":"javascript","content":"import React, { useState } from 'react';\n\nexport default function App() {\n  const [city, setCity] = useState('Tokyo');\n  return (\n    <div className=\"min-h-screen bg-slate-950 text-white p-8\">\n      <h1 className=\"text-3xl font-bold text-cyan-400\">AeroCast Weather</h1>\n      <p className=\"text-slate-400 mt-2\">Real-time meteorological telemetry for {city}</p>\n    </div>\n  );\n}"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"AeroCast React Weather App by DEV_Israfil."}
            ]"""
        ),
        ProjectEntity(
            id = "proj-python-scraper",
            title = "Automated E-Commerce Price Tracker & Scraper",
            slug = "python-price-tracker-scraper",
            description = "A Python automated crawler with BeautifulSoup and Selenium that tracks product price drops, parses metadata, exports clean CSVs, and sends Telegram alert notifications.",
            category = "Python",
            technologies = "Python, BeautifulSoup4, Requests, Telegram Bot API",
            version = "2.0.1",
            fileSize = "1.8 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_python",
            liveDemoUrl = "",
            downloadCount = 1870,
            status = "PUBLISHED",
            isFeatured = false,
            createdAt = System.currentTimeMillis() - 86400000L * 10,
            sourceFilesJson = """[
                {"name":"main.py","path":"main.py","language":"python","content":"import requests\nfrom bs4 import BeautifulSoup\nimport time\n\ndef check_prices(url):\n    headers = {'User-Agent': 'Mozilla/5.0'}\n    response = requests.get(url, headers=headers)\n    soup = BeautifulSoup(response.content, 'html.parser')\n    print('[DEV_Israfil Scraper] Analyzing page: ' + url)\n\nif __name__ == '__main__':\n    print('DEV_Israfil Python Scraper Started.')"},
                {"name":"requirements.txt","path":"requirements.txt","language":"text","content":"requests==2.31.0\nbeautifulsoup4==4.12.2\npython-telegram-bot==20.6"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"DEV_Israfil Python Price Tracker."}
            ]"""
        ),
        ProjectEntity(
            id = "proj-php-auth-api",
            title = "Secure RESTful Authentication API Engine",
            slug = "secure-php-rest-auth-api",
            description = "Production-grade PHP REST API with JWT bearer tokens, bcrypt password hashing, MySQL PDO prepared statements, rate limiting, and CORS headers.",
            category = "PHP",
            technologies = "PHP 8, MySQL, JWT, Apache .htaccess",
            version = "1.3.0",
            fileSize = "2.1 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_php",
            liveDemoUrl = "https://devisrafil.demo/api/docs",
            downloadCount = 980,
            status = "PUBLISHED",
            isFeatured = false,
            createdAt = System.currentTimeMillis() - 86400000L * 12,
            sourceFilesJson = """[
                {"name":"auth.php","path":"api/auth.php","language":"php","content":"<?php\nheader('Content-Type: application/json');\nheader('Access-Control-Allow-Origin: *');\n\nrequire_once '../config/Database.php';\n\n// DEV_Israfil Authentication API\necho json_encode(['status' => 'success', 'message' => 'DEV_Israfil API Online']);\n?>"},
                {"name":"Database.php","path":"config/Database.php","language":"php","content":"<?php\nclass Database {\n    private ${'$'}host = 'localhost';\n    private ${'$'}db_name = 'dev_israfil_db';\n}\n?>"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"PHP Authentication API by DEV_Israfil."}
            ]"""
        ),
        ProjectEntity(
            id = "proj-android-task-manager",
            title = "TaskFlow Android Modern Productivity Suite",
            slug = "taskflow-android-productivity-suite",
            description = "A Jetpack Compose Android application with Room local database, swipe-to-dismiss gestures, dark/light theme, category tags, and priority reminder scheduling.",
            category = "Android",
            technologies = "Kotlin, Jetpack Compose, Room DB, Coroutines",
            version = "1.0.0",
            fileSize = "4.5 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_android",
            liveDemoUrl = "",
            downloadCount = 3120,
            status = "PUBLISHED",
            isFeatured = true,
            createdAt = System.currentTimeMillis() - 86400000L * 15,
            sourceFilesJson = """[
                {"name":"TaskViewModel.kt","path":"app/src/main/java/TaskViewModel.kt","language":"kotlin","content":"package com.devisrafil.taskflow\n\nimport androidx.lifecycle.ViewModel\nimport kotlinx.coroutines.flow.MutableStateFlow\n\nclass TaskViewModel : ViewModel() {\n    val tasks = MutableStateFlow(listOf(\"Complete DEV_Israfil Project\", \"Deploy to production\"))\n}"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"TaskFlow Kotlin Compose Project by DEV_Israfil."}
            ]"""
        ),
        ProjectEntity(
            id = "proj-arcade-space-game",
            title = "Nebula Strike 2D Retro Arcade Canvas Game",
            slug = "nebula-strike-retro-arcade-game",
            description = "A retro space shooter built entirely in HTML5 Canvas and JavaScript with particle explosion physics, multi-wave boss encounters, powerup drops, and 8-bit sound synthesizers.",
            category = "Games",
            technologies = "HTML5 Canvas, JavaScript, WebAudio API",
            version = "1.1.2",
            fileSize = "1.9 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_game",
            liveDemoUrl = "https://devisrafil.demo/games/nebulastrike",
            downloadCount = 4250,
            status = "PUBLISHED",
            isFeatured = true,
            createdAt = System.currentTimeMillis() - 86400000L * 18,
            sourceFilesJson = """[
                {"name":"game.js","path":"game.js","language":"javascript","content":"const canvas = document.getElementById('gameCanvas');\nconst ctx = canvas.getContext('2d');\n\nlet score = 0;\nfunction gameLoop() {\n  ctx.fillStyle = '#050510';\n  ctx.fillRect(0, 0, canvas.width, canvas.height);\n  requestAnimationFrame(gameLoop);\n}\ngameLoop();"},
                {"name":"index.html","path":"index.html","language":"html","content":"<!DOCTYPE html>\n<html>\n<head><title>Nebula Strike - DEV_Israfil</title></head>\n<body style=\"margin:0; background:#000; overflow:hidden;\">\n<canvas id=\"gameCanvas\" width=\"800\" height=\"600\"></canvas>\n<script src=\"game.js\"></script>\n</body>\n</html>"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"Nebula Strike Canvas Game by DEV_Israfil."}
            ]"""
        ),
        ProjectEntity(
            id = "proj-markdown-cloud-notes",
            title = "ZenPad Markdown Live Editor & Note App",
            slug = "zenpad-markdown-live-editor",
            description = "A responsive web application for writing markdown notes with live split-screen preview, syntax highlighting, export to PDF/HTML, word count analytics, and offline localStorage sync.",
            category = "Web Apps",
            technologies = "JavaScript, Marked.js, Highlight.js, LocalStorage",
            version = "2.0.0",
            fileSize = "1.6 MB",
            thumbnailUrl = "hero_coding_banner",
            youtubeUrl = "https://youtube.com/watch?v=sample_zenpad",
            liveDemoUrl = "https://devisrafil.demo/zenpad",
            downloadCount = 1590,
            status = "PUBLISHED",
            isFeatured = false,
            createdAt = System.currentTimeMillis() - 86400000L * 22,
            sourceFilesJson = """[
                {"name":"app.js","path":"app.js","language":"javascript","content":"// DEV_Israfil ZenPad Editor\nconsole.log('ZenPad initialized.');"},
                {"name":"README.txt","path":"README.txt","language":"text","content":"ZenPad Markdown Editor by DEV_Israfil."}
            ]"""
        )
    )
}
