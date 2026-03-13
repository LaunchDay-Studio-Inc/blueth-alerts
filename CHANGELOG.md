# Changelog

All notable changes to Blueth Alerts will be documented in this file.

## [1.0.0] - 2026-03-13

### Added
- Player join/leave notifications with online player count
- Player death notifications with killer info for PVP deaths
- Player chat logging (disabled by default for privacy)
- Server start/stop notifications with version and uptime
- Player command logging for monitored players
- Low TPS warnings with configurable threshold and cooldown
- First-join detection with special gold embed
- Player advancement notifications
- Discord webhook integration with rich embeds
- Crafatar player head avatars in embeds
- Async webhook delivery with rate limiting (5 per 5 seconds)
- `/alerts reload` command to hot-reload configuration
- `/alerts test` command to verify webhook connectivity
- `/alerts status` command to view enabled events
- Full tab completion for all commands
