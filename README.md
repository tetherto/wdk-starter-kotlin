# WDK Starter Kotlin

A sample Android wallet app built with [Tether WDK](https://github.com/niclas-nickleby/wdk-core-kotlin) (Wallet Development Kit). This app demonstrates how to integrate `wdk-core-kotlin` into a Jetpack Compose Android application, supporting both **Ethereum (Sepolia testnet)** and **Bitcoin (testnet)**.

## Features

- Create a new wallet with a 12-word seed phrase
- Import an existing wallet from a mnemonic
- View balances for ETH (Sepolia) and BTC (Testnet)
- Send transactions on both networks with fee estimation
- Receive funds via QR code and address display
- Sign and verify messages with your private key
- Dark theme UI built with Jetpack Compose and Material 3

## Prerequisites

- **Android Studio** (Hedgehog or newer)
- **JDK 17**
- **Android SDK 35** (compileSdk)
- **Android Emulator** or physical device running API 33+
- **Node.js** (for building the WDK worklet bundle)

## Project Setup

This project references `wdk-core-kotlin` as a local Gradle module. The expected directory layout:

```
Tether/
  wdk-core-kotlin/     # The WDK core library
  wdk-starter-kotlin/  # This starter app
```

### First-Time Setup

1. **Build the WDK bundle** (if not already done):

```bash
cd ../wdk-core-kotlin/js
npm install
npm run generate
```

2. **Open in Android Studio**: Open the `wdk-starter-kotlin` folder in Android Studio. Gradle sync will automatically pick up the local `wdk-core-kotlin` module.

3. **Run the app**: Select a device/emulator (API 33+) and press Run.

### Building from Terminal

```bash
./gradlew assembleDebug
```

To install on a connected device:

```bash
./gradlew installDebug
```

### Starting the Emulator

Make sure the Android SDK tools are on your `PATH` (add this to your `~/.zshrc` or shell profile):

```bash
export ANDROID_HOME="$HOME/Library/Android/sdk"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"
```

List your available virtual devices and start one in the background:

```bash
emulator -list-avds
emulator -avd Pixel_9 &
```

Wait for the emulator to finish booting, then install the app:

```bash
adb wait-for-device
./gradlew installDebug
```

### Running the App

Launch the app on the emulator (or a connected device):

```bash
adb shell monkey -p to.tether.wdk.starter -c android.intent.category.LAUNCHER 1
```

## Architecture

The app follows MVVM with Jetpack Compose:

- **WalletViewModel** manages all wallet state and WDK API calls
- **Screens.kt** contains all screen composables (Welcome, Create, Import, Home, Send, Receive, Sign)
- **Components.kt** contains shared UI components (buttons, cards, QR code, seed cells)
- **MainActivity.kt** sets up the dark theme and hosts the app

## Networks

The app is configured for testnet use:

| Network | Blockchain | Provider |
|---------|-----------|----------|
| Sepolia | Ethereum (testnet) | ethereum-sepolia.publicnode.com |
| Bitcoin | Bitcoin (testnet) | blockbook.tbtc-1.zelcore.io |

## License

See the LICENSE file for details.
