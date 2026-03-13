<div align="center">

# Blueth Alerts

**Free Discord webhook alerts for your Minecraft server**

[![Paper 1.21+](https://img.shields.io/badge/Paper-1.21%2B-blue?logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABBSURBVChTY2RgYPgPxGQBJiBFNmBi+A8yigwANZIIoGoEAWINBgNSDcYCBkcjNh+TBUg1GAtgHE06INVgcgEDAwBcOA0PEu4rFwAAAABJRU5ErkJggg==)](https://papermc.io/)
[![Java 21](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://adoptium.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

</div>

## Features

| Event | Description |
|-------|-------------|
| 🟢 **Player Join** | Notifies when a player connects, with online count |
| 🔴 **Player Leave** | Notifies when a player disconnects |
| 💀 **Player Death** | Death message, location, and killer (PVP) |
| 💬 **Player Chat** | Logs chat messages (off by default for privacy) |
| 🚀 **Server Start** | Server version and player slots |
| 🛑 **Server Stop** | Uptime and version info |
| ⌨️ **Player Command** | Logs commands from monitored players |
| ⚠️ **Low TPS** | Warning when TPS drops below threshold |
| ⭐ **First Join** | Special gold embed for brand-new players |
| 🏆 **Advancement** | When players earn advancements |

## Quick Start

1. **Drop** the `BluethAlerts.jar` into your server's `plugins/` folder
2. **Set** your Discord webhook URL in `plugins/BluethAlerts/config.yml`
3. **Restart** your server — you're done!

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/alerts reload` | Reload configuration | `blueth.alerts.admin` |
| `/alerts test` | Send a test webhook | `blueth.alerts.admin` |
| `/alerts status` | Show enabled events | `blueth.alerts.admin` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `blueth.alerts.admin` | Access to admin commands | OP |
| `blueth.alerts.monitor` | Commands from this player are logged | OP |

## Configuration

```yaml
discord:
  webhook-url: ""
  bot-name: "Blueth Alerts"
  avatar-url: ""

events:
  player-join:
    enabled: true
    color: "#00FF00"
  player-leave:
    enabled: true
    color: "#FF0000"
  player-death:
    enabled: true
    color: "#FF6600"
  player-chat:
    enabled: false
    color: "#AAAAAA"
  server-start:
    enabled: true
    color: "#00AAFF"
  server-stop:
    enabled: true
    color: "#FF0000"
  player-command:
    enabled: true
    permission: "blueth.alerts.monitor"
    color: "#FFAA00"
  low-tps:
    enabled: true
    threshold: 18.0
    cooldown: 300
    color: "#FF0000"
  first-join:
    enabled: true
    color: "#FFD700"
  advancement:
    enabled: true
    color: "#AA00FF"

messages:
  server-name: "My Server"
```

## FAQ

**How are rate limits handled?**
Blueth Alerts has built-in rate limiting — max 5 webhooks per 5 seconds. Excess messages are queued and sent when the window opens. Discord 429 responses are also handled automatically.

**Is chat logging a privacy concern?**
Chat logging is **disabled by default**. You must explicitly enable it in `config.yml`. We recommend informing your players if you turn it on.

**Does it support Folia?**
Folia support is planned for a future release.

**Does it work with Bedrock/Geyser players?**
Yes — player events fire normally for Bedrock players connecting via Geyser.

---

<div align="center">

### Part of the Blueth Ecosystem

Blueth Alerts is a **free** plugin by [LaunchDay Studio](https://blueth.online).

Looking for advanced server management? Check out **[Blueth Contracts](https://blueth.online)** — our premium product for the Blueth ecosystem.

**[Website](https://blueth.online)** · **[Discord](https://blueth.online/discord)**

</div>
