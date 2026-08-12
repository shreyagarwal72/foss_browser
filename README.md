# 🌸 Petal Browser

**Petal Browser** is a fast, ultra-lightweight, and privacy-focused Android web browser built with **Material 3 Expressive Design**, Jetpack Compose, and native Android WebView technology. Inspired by Chrome and Cromite Android UI architectures, Petal delivers a 60fps lag-free browsing experience with dynamic wallpaper colors, dark surface containers, and spring-physics micro-interactions.

---

## ✨ Features & Highlights

### 🎨 Material 3 Expressive & Stride UI
- **AMOLED & Dark Surface Container Tinting**: Deep dark mode support (`28dp` rounded corners) optimized for OLED screens.
- **Chrome & Cromite Style Omnibox**: Responsive address bar with security lock status badges, domain highlighting, and instant site fast-toggles.
- **Scroll-Linked Address Bar Collapse**: On scroll down, the full address bar smoothly animates out of view and is replaced by a small circular floating action bubble (`fab_bubble`) positioned at bottom-right with spring physics (`OvershootInterpolator`).
- **Spring-Physics Micro-Animations**: Tactile squishy press feedback on buttons (`bouncyClickable`) matching Stride Motion design guidelines.

### ⚡ Lightning Fast & Lag-Free
- **Async AdBlock Engine**: Asynchronous host loading off the main UI thread ensures zero delay when creating new tabs or loading pages.
- **60fps Tab Selector Overview**: Chrome-inspired 2-column live tab switcher grid with instant tab switching and smooth closing controls.
- **Zero-Lag Instant Popup Menu**: High-performance 270dp `PopupWindow` with window dimming and quick-action tool shortcuts.

### 🛡️ Privacy & Security First
- **Built-in Ad & Tracker Shield**: Automatic blocking of web ads and trackers using updated host lists.
- **Default Search Engine Chooser**: Custom onboarding modal & settings sheet to select your preferred search provider (Google, DuckDuckGo, Startpage, Brave, SearXNG, Bing, Qwant, Ecosia).
- **Chrome-Inspired History Page**: Integrated search & filter for visited sites, clear browsing data shortcut, and single-tap history entry deletion.
- **Zero Data Collection**: No telemetry, tracking, or remote data analytics.

### 📦 Inbuilt Download Manager
- Integrated Jetpack Compose Download Manager with real-time progress bars, pausing, resuming, and direct file opening.

---

## 🛠️ Build & Installation

### Requirements
- **Android SDK**: API 26+ (Android 8.0 Oreo or higher)
- **Target SDK**: API 34+ (Android 14)
- **Gradle**: 8.0+ / AGP 8.0+

### Building APK via Command Line
```bash
./gradlew assembleDebug
```

---

## 📄 License
Licensed under GNU General Public License v3.0 (GPL-3.0).