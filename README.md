# Treasure Island

A digital deduction game inspired by the board game **Cryptid** — with pirates, a hidden treasure, and one decisive twist: you can play it **solo**.

## What's it about?

Somewhere on the island, a treasure is buried. Every pirate holds a secret clue — for example, _"The treasure lies within two tiles of a forest"_. By cleverly questioning the other pirates, you narrow down where each clue points to, and you dig up the chest. Whoever uncovers the right spot first wins.

## What makes Treasure Island special?

- **Solo mode against the AI.** No need to round up players. Pick how hard the AI should make your life — four difficulty levels are ready: **Easy**, **Medium**, **Hard**, **Perfect**.
- **No more miscommunication.** In the original board game it's easy for someone to accidentally give a wrong answer about their clue — and the whole game falls apart. Treasure Island evaluates every question **automatically and flawlessly**. No arguments, no doubt, just pure deduction.
- **Endless replay value.** Every game is procedurally generated. No two matches are the same.
- **Bigger maps available.** Play on the normal size, or take on the **large variant** — more tiles, more clue combinations, more to chew on.
- **Built-in hints.** Optionally, the game shows you which clues have already been ruled out, gives time info, or displays the rules. You decide how much help you want.

## Platforms

Treasure Island runs on:

- **Desktop** (Windows, macOS, Linux) via LWJGL3
- **Android** (API 28+)
- **Web** via TeaVM — playable in the browser, no install

## Tech stack

- [libgdx](https://libgdx.com/) **1.14.0**
- Java **11** (source & target)
- Gradle **8.13**, Android Gradle Plugin **8.12.0**
- TeaVM backend via [`gdx-teavm`](https://github.com/xpenatan/gdx-teavm) **1.5.3**

Project layout:

```
core/      Game logic (platform-independent)
desktop/   LWJGL3 launcher
android/   Android app
teavm/     Web build (TeaVM)
assets/    Graphics, fonts, levels
```

## How to run

### Desktop

```bash
./gradlew desktop:run
```

### Web (TeaVM, local Jetty server)

```bash
./gradlew teavm:run
```

After the build finishes, a local server starts; the URL is printed to the console.

For a release build without the server:

```bash
./gradlew teavm:buildRelease
```

Output lands in `teavm/build/dist/`.

### Android

Prerequisite: the Android SDK is installed and its path is set in `local.properties`:

```
sdk.dir=C:/Users/<user>/AppData/Local/Android/Sdk
```

Build a debug APK:

```bash
./gradlew android:assembleDebug
```

Install on a connected device and launch:

```bash
./gradlew android:installDebug
./gradlew android:run
```

### Signed release APK (optional)

In `~/.gradle/gradle.properties`:

```
PIRATE_KEYSTORE_PATH=/path/to/keystore.jks
PIRATE_KEYSTORE_PASSWORD=...
PIRATE_KEY_ALIAS=pirate
PIRATE_KEY_PASSWORD=...
```

Then:

```bash
./gradlew android:assembleRelease
```

## License & credits

Copyright © Dirk Aporius — see license headers in the source files. Inspired by the board game **Cryptid** (Osprey Games).
