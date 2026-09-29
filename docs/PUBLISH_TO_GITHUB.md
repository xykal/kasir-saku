# GitHub repository

## Live project

- Repository: https://github.com/xykal/kasir-saku
- Visibility: public (owner decision)
- License: MIT
- Default branch: `main`
- Topics: `android`, `kotlin`, `jetpack-compose`, `point-of-sale`, `pos`, `offline-first`, `sqlite`, `inventory-management`, `small-business`, `retail`, `cashier`, `indonesia`.

## Repository controls configured

- Secret scanning, push protection, and Dependabot security updates enabled.
- `main` branch protection requires a pull request, one approval, and the successful `Build separate Android APKs` check. Force-push and branch deletion are blocked; linear history and conversation resolution are required.
- Actions workflow permissions are read-only; build requires no repository secrets.
- Dependabot checks Gradle and GitHub Actions dependencies weekly.

## Build artifacts

The workflow builds separate debug artifacts named `kasir-saku-arm64-v8a` and `kasir-saku-armeabi-v7a`, rejects a universal APK, and retains artifacts for seven days. The initial successful build is recorded at [GitHub Actions run 36625714316](https://github.com/xykal/kasir-saku/actions/runs/36625714316).

## Clone

```sh
git clone https://github.com/xykal/kasir-saku.git
```

Do not add release-signing keys, API credentials, customer data, or real transaction exports to the public repository. Rotate any credential that was previously uploaded as a plain-text file.
