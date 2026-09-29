# GitHub repository setup checklist

## Proposed repository

- Owner: `xykal`
- Name: `kasir-saku`
- Description: `Offline-first Android POS starter for small shops, with separate arm64-v8a and armeabi-v7a APK builds.`
- Suggested topics: `android`, `kotlin`, `jetpack-compose`, `point-of-sale`, `pos`, `offline-first`, `sqlite`, `inventory-management`, `small-business`, `retail`, `cashier`, `indonesia`.
- License: undecided. Do not add an open-source license until the owner selects one.

## Safe defaults

- Visibility: **public**, as explicitly requested by the owner.
- License selected by owner: MIT (`LICENSE` included).
- Enable two-factor authentication on the account.
- Keep Actions permissions read-only by default; this build workflow uses no repository secrets.
- Enable Dependabot alerts, security updates, secret scanning, and private vulnerability reporting where available.
- `main` is protected: require a pull request, one approval, and the `Build separate Android APKs` check; block force pushes/deletion and require linear history/conversation resolution.
- Do not add signing keys or credentials to the repository. Add release-signing secrets only when a release workflow is approved.
- Before public release, review source, licenses, screenshots, data-loss warnings, and security policy.

The topic list and settings above are recommendations; GitHub repository metadata is configured in GitHub, not by committing this document.
