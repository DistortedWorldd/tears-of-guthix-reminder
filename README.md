# Tears of Guthix Reminder Plugin

A RuneLite plugin that reminds you to complete your weekly Tears of Guthix minigame.

## Features

- **Automatic Reset Tracking**: Tracks the weekly reset (Wednesday 00:00 UTC)
- **In-game Notifications**: Get notified when Tears of Guthix is available
- **Overlay Display**: Shows time remaining until next reset or "Available now!"
- **Manual Tracking**: Mark as completed or reset the status manually

## Installation

### Method 1: Using RuneLite Plugin Hub (if published)
1. Open RuneLite
2. Click the Plugin Hub button
3. Search for "Tears of Guthix Reminder"
4. Click Install

### Method 2: Manual Installation (Sideloading)
1. Build the plugin:
   ```bash
   gradlew build
   ```
2. Copy the generated JAR from `build/libs/` to your RuneLite plugins folder:
   - Windows: `%USERPROFILE%\.runelite\sideloaded-plugins\`
   - macOS/Linux: `~/.runelite/sideloaded-plugins/`
3. Restart RuneLite

## Configuration

Open the plugin settings in RuneLite to configure:

- **Enable Notifications**: Toggle notifications when Tears of Guthix becomes available
- **Show Overlay**: Display/hide the overlay showing time until reset
- **Mark as Completed**: Manually mark Tears of Guthix as completed this week
- **Reset Completion**: Reset the completion status (for testing or if you forgot to mark it)

## Usage

1. Enable the plugin in RuneLite
2. The overlay will show how long until the next reset
3. When available, you'll see "Available now!" and receive a notification (if enabled)
4. After completing Tears of Guthix, click "Mark as Completed" in the plugin settings
5. The timer will automatically track the next Wednesday reset

## How It Works

Tears of Guthix resets every **Wednesday at 00:00 UTC**. The plugin:
- Tracks when you last completed it
- Calculates the next Wednesday reset
- Notifies you when it's available again
- Shows a countdown timer in the overlay

## Requirements

- RuneLite client
- Java 11 or higher

## Building from Source

```bash
gradlew build
```

The compiled plugin will be in `build/libs/`.

## Support

If you encounter any issues or have suggestions, please create an issue on the GitHub repository.

## License

This plugin is open source and available for personal use.
