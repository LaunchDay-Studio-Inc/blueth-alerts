<!-- Logo placeholder: replace with your Blueth Alerts banner -->
<!-- <img src="https://blueth.online/assets/blueth-alerts-banner.png" alt="Blueth Alerts" width="600"> -->

<div align="center">

# Blueth Alerts

### Free Discord webhook alerts for your Minecraft server — zero config, instant setup.

[![Paper 1.21+](https://img.shields.io/badge/Paper-1.21%2B-blue?logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABBSURBVChTY2RgYPgPxGQBJiBFNmBi+A8yigwANZIIoGoEAWINBgNSDcYCBkcjNh+TBUg1GAtgHE06INVgcgEDAwBcOA0PEu4rFwAAAABJRU5ErkJggg==)](https://papermc.io/)
[![Java 21+](https://img.shields.io/badge/Java-21%2B-orange?logo=openjdk)](https://adoptium.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![GitHub Release](https://img.shields.io/github/v/release/LaunchDay-Studio-Inc/blueth-alerts?label=Release&color=blue)](https://github.com/LaunchDay-Studio-Inc/blueth-alerts/releases/latest)
[![GitHub Stars](https://img.shields.io/github/stars/LaunchDay-Studio-Inc/blueth-alerts?style=flat&label=Stars&color=yellow)](https://github.com/LaunchDay-Studio-Inc/blueth-alerts/stargazers)

---

</div>

## Why Blueth Alerts?

> **Know what's happening on your server — without logging in.**

- ⚡ **Instant Discord notifications** — player joins, deaths, low TPS, and more — right in your Discord channel
- 🎛️ **8 event types, all toggleable** — enable only what you need, disable what you don't
- 🚀 **Rate-limited & fully async** — never blocks the main thread, built-in 5/5s rate limiter prevents Discord throttling
- 🧩 **Part of the Blueth ecosystem** — pairs with [Blueth Contracts](https://github.com/LaunchDay-Studio-Inc/blueth-contracts) for a complete server management stack

---

## Features

<table>
<tr>
<td align="center" width="33%"><h3>🟢</h3><b>Player Join / Leave</b><br>Online count, UUID, player head</td>
<td align="center" width="33%"><h3>💀</h3><b>Player Death</b><br>Death message, location, PVP killer</td>
<td align="center" width="33%"><h3>💬</h3><b>Chat (opt-in)</b><br>Message logging, off by default</td>
</tr>
<tr>
<td align="center"><h3>🚀</h3><b>Server Start / Stop</b><br>Version info, uptime on shutdown</td>
<td align="center"><h3>⚡</h3><b>Low TPS Alert</b><br>Configurable threshold & cooldown</td>
<td align="center"><h3>🏆</h3><b>Advancements</b><br>Track player progression</td>
</tr>
<tr>
<td align="center"><h3>🔧</h3><b>Staff Commands</b><br>Logs commands from monitored players</td>
<td align="center"><h3>🌟</h3><b>First Join Welcome</b><br>Special gold embed for new players</td>
<td align="center"></td>
</tr>
</table>

---

## Quick Start

Three steps. That's it.

**1.** Drop `BluethAlerts-1.0.0.jar` into your server's `plugins/` folder

**2.** Set your Discord webhook URL in `plugins/BluethAlerts/config.yml`:
```yaml
discord:
  webhook-url: "https://discord.com/api/webhooks/..."
```

**3.** Restart your server. Done. ✅

---

## Discord Embed Preview

Every notification is sent as a rich Discord embed with:

- 🎨 **Colored sidebar** — unique color per event type (fully customizable)
- 🧑 **Player head avatar** — pulled from [Crafatar](https://crafatar.com/) using the player's UUID
- 🕐 **Timestamp** — exact time of the event
- 📝 **Detail fields** — player name, UUID, location, online count, etc.
- 📌 **Server name footer** — always know which server sent the alert

<!-- Add screenshot of Discord embeds here -->

---

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/alerts reload` | Reload configuration without restarting | `blueth.alerts.admin` |
| `/alerts test` | Send a test embed to verify your webhook | `blueth.alerts.admin` |
| `/alerts status` | Show which events are enabled/disabled | `blueth.alerts.admin` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `blueth.alerts.admin` | Access to all `/alerts` admin commands | OP |
| `blueth.alerts.monitor` | Commands from this player are logged to Discord | OP |

---

## Configuration

Full default `config.yml` with comments:

```yaml
# ─────────────────────────────────────────────
# Blueth Alerts — Configuration
# ─────────────────────────────────────────────

# Discord webhook settings
discord:
  # Paste your Discord webhook URL here
  webhook-url: ""
  # Display name for the webhook bot in Discord
  bot-name: "Blueth Alerts"
  # Custom avatar URL for the webhook bot (leave empty for default)
  avatar-url: ""

# ─────────────────────────────────────────────
# Event toggles — enable/disable each event
# Color values are hex codes used for the embed sidebar
# ─────────────────────────────────────────────
events:
  # Player connects to the server
  player-join:
    enabled: true
    color: "#00FF00"

  # Player disconnects from the server
  player-leave:
    enabled: true
    color: "#FF0000"

  # Player dies (includes PVP killer info)
  player-death:
    enabled: true
    color: "#FF6600"

  # Player chat messages — DISABLED by default for privacy
  player-chat:
    enabled: false
    color: "#AAAAAA"

  # Server starts up
  server-start:
    enabled: true
    color: "#00AAFF"

  # Server shuts down (includes uptime)
  server-stop:
    enabled: true
    color: "#FF0000"

  # Commands run by players with the monitor permission
  player-command:
    enabled: true
    # Only players with this permission have their commands logged
    permission: "blueth.alerts.monitor"
    color: "#FFAA00"

  # Fires when server TPS drops below the threshold
  low-tps:
    enabled: true
    # TPS value that triggers the warning
    threshold: 18.0
    # Seconds between repeated warnings (prevents spam)
    cooldown: 300
    color: "#FF0000"

  # Special embed when a player joins for the first time
  first-join:
    enabled: true
    color: "#FFD700"

  # Player earns an advancement
  advancement:
    enabled: true
    color: "#AA00FF"

# ─────────────────────────────────────────────
# Messages
# ─────────────────────────────────────────────
messages:
  # Shown in the footer of every embed
  server-name: "My Server"
```

---

## FAQ

<details>
<summary><b>Will this lag my server?</b></summary>

No. All webhook requests are sent **asynchronously** on a dedicated thread. The main server thread is never blocked. Built-in rate limiting (5 webhooks per 5 seconds) prevents Discord from throttling you.
</details>

<details>
<summary><b>Can I use multiple webhook URLs?</b></summary>

Not yet — this is planned for **v1.1**. Currently all events go to a single webhook.
</details>

<details>
<summary><b>Does it work with Geyser / Bedrock players?</b></summary>

Yes. Player events fire normally for Bedrock players connecting via Geyser — join, leave, death, chat, and all other events work as expected.
</details>

<details>
<summary><b>Does it support Folia?</b></summary>

Folia support is **planned** for a future release.
</details>

<details>
<summary><b>Is chat logging a privacy concern?</b></summary>

Chat logging is **disabled by default**. You must explicitly set `player-chat.enabled: true` in `config.yml`. We recommend informing your players if you enable it.
</details>

<details>
<summary><b>How does rate limiting work?</b></summary>

Blueth Alerts allows a maximum of **5 webhooks per 5 seconds**. Any excess messages are queued and sent when the window opens. Discord HTTP 429 responses are also handled automatically with retry logic.
</details>

---

<div align="center">

## Part of the **Blueth** Plugin Ecosystem

</div>

<table>
<tr>
<td>🔗</td>
<td><a href="https://blueth.online"><b>blueth.online</b></a> — Home of the Blueth ecosystem</td>
</tr>
<tr>
<td>🎯</td>
<td><a href="https://github.com/LaunchDay-Studio-Inc/blueth-contracts"><b>Blueth Contracts</b></a> — Premium quest board & season tasks for your server</td>
</tr>
<tr>
<td>💬</td>
<td><a href="https://blueth.online/discord"><b>Join our Discord</b></a> — Support, updates, and community</td>
</tr>
</table>

---

<div align="center">

Made with ❤️ by **LaunchDay Studio** — Copyright © 2026

</div>
