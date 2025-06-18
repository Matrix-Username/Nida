
![1718225213536](https://github.com/Matrix-Username/Nida/assets/59887239/872cb3dc-1db5-417d-a684-e8d0bfe07daf)


> Debug & Inspect Android apps like never before.

[![Version](https://img.shields.io/badge/version-2.0-blue.svg)](#)

## 🚀 Overview

**NIDA** is a runtime instrumentation and inspection framework tailored for Android applications. It leverages a **pure Java** hooking engine—no native libraries—to maximize stealth and compatibility across devices.

Whether you are debugging complex networking flows, reverse engineering obfuscated logic, NIDA provides deep access without tampering with the app binary.

---

## 🔥 Key Features

| Feature                                     | NIDA | Frida | Xposed |
| ------------------------------------------- | :--: | :---: | :----: |
| Pure Java Hook Engine (no native code)      |   ✅  |   ❌   |    ❌   |
| Supports Android 7.0–16.0                   |   ✅  |   ✅   |    ✅   |
| Architecture Support: x86\_64, ARM32, ARM64 |   ✅  |   ✅   |    ✅   |
| Root Injection: ODEX Patching               |   ✅  |   ❌   |    ❌   |
| Root Injection: Xposed via Nida Injector    |   ✅  |   ⚠️  |    ✅   |
| Non-Root Injection (manual payload & VMs)   |   ✅  |   ⚠️  |   ⚠️   |
| Runtime Deobfuscation                       |   ✅  |   ❌   |    ❌   |
| Built-in Network Inspector                  |   ✅  |   ❌   |    ❌   |
| Dex Memory Dump from RAM                    |   ✅  |   ⚠️  |   ⚠️   |

---

## 🧠 Technical Highlights

### 🪝 Pure Java Hook Engine

NIDA's hook system is implemented entirely in Java, avoiding reliance on C/C++ or native libraries. This design improves stealth (no suspicious native modules) and broad compatibility across Android versions and CPU architectures.

### 🔄 Supported Platforms

* **Android Versions:** 7.0 (API 24) through 16.0 (API 36)
* **Architectures:** x86\_64, ARM32, ARM64

### ⚙️ Injection Methods

* **With Root**:

  * **ODEX Patching**: Modify app ODEX to load NIDA service at startup.
  * **Xposed**: Deploy via Nida Injector module in Xposed framework.
* **Without Root**:

  * **Manual APK Modification**: Insert NIDA payload into the target APK.
  * **Virtual Environment**: Run inside a sandboxed VM that injects NIDA at launch.

---

## 🖥️ Nida Manager (PC Control)

Control and interact with NIDA service using **Nida Manager** desktop client (Linux & Windows).

> ![image](https://github.com/user-attachments/assets/05fa65e0-49cb-4f32-a0c0-66d4abe3de63)
> 
> ![image](https://github.com/user-attachments/assets/ae6a7d90-4a8a-4d8a-a224-f4060fc728f9)
>
> ![image](https://github.com/user-attachments/assets/d31d1e5a-fb9e-4628-a615-0faf113b45f8)
>
> ![image](https://github.com/user-attachments/assets/88ce82b2-f20d-4514-b3df-ddef39360560)



### Workflow

1. **Auto-Discovery**: Manager scans local network and automatically pulls running NIDA-enabled apps.
2. **Handshake**: The NIDA service on device initiates a greeting handshake to the Manager.
3. **WebSocket Channel**: Establish a bidirectional WebSocket for command & data exchange.
4. **Tracing & Inspection**: Send commands to trace methods, intercept network calls, dump dex classes, and more.

### Key Capabilities

* **IDE-like UI** styled after Android Studio
* **Decompiled Code Browser**: Navigate, search, and annotate smali/Java code
* **Method Tracing**: View call arguments, return values, and performance metrics
* **Network Interception**: Bypass SSL pinning and view raw HTTP/S payloads
* **Dex Dump**: Extract in-memory dex classes

---
## 🗺️ Roadmap

* [ ] Dynamic Smali code editing in real-time
* [ ] Native code tracing (C/C++ hooks)
* [ ] Plugin system for community extensions
* [ ] AI-powered analysis assistant

---

## 📄 License

MIT © 2025 [Nazar Sladkovskyi](https://github.com/Matrix-Username)

---

## 📬 Contact

* Telegram: [@dalvikvm](https://t.me/dalvikvm)

---

> ⚠️ **This is a demo README.**  
> The full-featured release of **NIDA 2.0** is planned for **August 2025**.
