# AppHarbor ⚓

**Your Android app library, beyond the Play Store.**

AppHarbor brings F-Droid repositories, GitHub, GitLab, Codeberg, Gitea, Forgejo, and independent Android projects into one place. Follow developers, discover their apps, build a personal library, and keep everything organized and up to date without hunting through release pages by hand.

[![Development release](https://img.shields.io/github/v/release/stupidgiraffe/AppHarbor?include_prereleases&label=development)](https://github.com/stupidgiraffe/AppHarbor/releases/tag/appharbor-dev)
[![Android](https://img.shields.io/badge/Android-6.0%2B-3DDC84?logo=android&logoColor=white)](#)
[![License](https://img.shields.io/github/license/stupidgiraffe/AppHarbor)](LICENSE)

## What AppHarbor does

- **Browse F-Droid-style repositories** alongside apps published directly on software forges.
- **Follow an entire developer account** on GitHub, GitLab, Codeberg, Gitea, or Forgejo and discover its Android releases automatically.
- **Track individual repositories** when you only want one project.
- **Install and update APKs** from their original release source.
- **Keep a personal app library** instead of remembering dozens of GitHub pages and obscure repositories.
- **Resume creator discovery in the background** when scans are interrupted, with persisted progress and retry handling.
- **Inspect app metadata before installation**, including the real package metadata available from release APKs.
- **Stay local-first**: the core app library works on-device, with optional sync planned for people who want the same library across devices.

## Get AppHarbor

### Development build

The latest tested development APK is published as a GitHub prerelease:

**[Download AppHarbor Dev](https://github.com/stupidgiraffe/AppHarbor/releases/tag/appharbor-dev)**

The release includes:

- `AppHarbor-v1.0.5-dev.apk`
- a SHA-256 checksum
- version and source commit information

This channel is for active AppHarbor development. A production-signed release channel will follow when the app is ready for broader distribution.

## Why AppHarbor

Some of the best Android apps never reach a traditional app store. They live in GitHub releases, small F-Droid repositories, Codeberg projects, self-hosted forges, and developer accounts you discover once and then struggle to keep track of.

AppHarbor turns those scattered sources into a **personal Android catalog**.

Instead of:

> find project → open releases → identify the correct APK → download → remember to check again later

the goal is:

> follow source once → discover apps → keep them in your library → update from the source

## Current development status

AppHarbor already has a working installable development build and the core multi-source discovery/update foundation inherited from Omnify.

### Working now

- F-Droid-style repository browsing
- GitHub, GitLab, Codeberg, Gitea, and Forgejo external sources
- whole-account / creator discovery
- individual repository tracking
- resumable background creator scans
- persisted scan checkpoints and progress
- retry handling for transient provider failures
- APK builds published automatically through GitHub Releases

### In active development

- faster creator/repository search and filtering
- a stronger personal Library experience
- developer/creator pages and better grouping
- imports from existing Android app-library tools
- optional multi-device sync
- broader source support
- full AppHarbor visual identity, launcher artwork, and fresh screenshots

## Build from source

AppHarbor currently builds with JDK 17 and the Android SDK.

```bash
./gradlew --no-daemon testDebugUnitTest assembleDebug
```

The debug APK is produced under:

```text
app/build/outputs/apk/debug/
```

## Project lineage

AppHarbor is built from the excellent work of:

- [Omnify](https://github.com/Victor-root/Omnify) by Victor-root
- [Droid-ify](https://github.com/Droid-ify/client) by LooKeR
- [Foxy-Droid](https://github.com/kitsunyan/foxy-droid) by kitsunyan

AppHarbor is evolving that foundation toward a broader **personal Android discovery and library layer** focused on apps from both repositories and independent developer sources.

For upgrade/data compatibility during development, several inherited internal Android identifiers remain unchanged. They are implementation details rather than the AppHarbor product identity.

## License

AppHarbor is free software licensed under the **GNU General Public License v3**. See [LICENSE](LICENSE).
