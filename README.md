# AppHarbor

AppHarbor is an Android app discovery browser and personal APK harbor built from [Omnify](https://github.com/Victor-root/Omnify), which itself builds on [Droid-ify](https://github.com/Droid-ify/client).

The current app already combines F-Droid-style repository browsing with direct external sources from GitHub, GitLab, Codeberg, and compatible Gitea/Forgejo hosts. Whole creator/account discovery is durable and resumable: scans run through WorkManager, survive process death, checkpoint completed repositories, retry transient provider failures, and report persisted progress back to the UI.

AppHarbor is under active development. The longer-term direction is a broader universal Android discovery/library layer that can aggregate more source types and user-supplied repositories. Appteka integration and other store/catalog sources are future work; they are not implemented today.

## Development builds

Installable development APKs are published as **GitHub pre-releases** in this repository:

**https://github.com/stupidgiraffe/AppHarbor/releases**

Each development release is tied to a specific commit and includes the APK plus a SHA-256 checksum file. These are debug/development builds for testing AppHarbor as it evolves. They are separate from a future production-signed release channel.

GitHub Actions also uploads ordinary CI artifacts on pushes and pull requests. Those artifacts are temporary build evidence and expire; the GitHub pre-releases are the durable download path.

## Build from source

Requires JDK 17 and an Android SDK.

```bash
./gradlew --no-daemon testDebugUnitTest assembleDebug
```

The debug APK is produced under `app/build/outputs/apk/debug/`.

## Compatibility identity

AppHarbor intentionally retains several inherited internal identifiers so existing installs and stored data are not broken by a cosmetic rename:

- stable application ID: `com.omnify.vroot`
- debug application ID: `com.omnify.vroot.debug`
- Kotlin/Android namespace: `com.looker.droidify`
- existing Omnify-era preference keys, migration markers, provider authorities, and compatible deep-link state where changing them would strand existing installs or data

Those internal names do not define the user-facing product identity. The app label, in-app attribution, build metadata, repository landing page, APK names, and AppHarbor update source use **AppHarbor**.

## Screenshots

Screenshots are intentionally omitted from this landing page until a fresh set has been captured from a verified AppHarbor build. The inherited Omnify screenshots in the repository are historical upstream assets and are not presented here as AppHarbor UI.

## Upstream and license

AppHarbor exists because of the work in:

- [Omnify](https://github.com/Victor-root/Omnify) by Victor-root
- [Droid-ify](https://github.com/Droid-ify/client) by LooKeR
- [Foxy-Droid](https://github.com/kitsunyan/foxy-droid) by kitsunyan

The project remains licensed under the GNU General Public License v3. See [LICENSE](LICENSE).
