# Publish this project to GitHub

The project owner selected a **public** repository and the MIT License. The current workspace has no GitHub CLI/authentication or configured remote, so it cannot create or push `xykal/kasir-saku` directly. Run these steps in a terminal where GitHub CLI is already authenticated as `xykal`.

## Create the public repository

From this project directory:

```sh
git init -b main
git add .
git commit -m "chore: scaffold Kasir Saku Android POS"
gh repo create xykal/kasir-saku --public --source=. --remote=origin --push --description "Offline-first Android POS starter for small shops; separate arm64-v8a and armeabi-v7a APK builds."
```

## Add repository topics

In GitHub, open **Settings → General → Topics** and add:

`android`, `kotlin`, `jetpack-compose`, `point-of-sale`, `pos`, `offline-first`, `sqlite`, `inventory-management`, `small-business`, `retail`, `cashier`, `indonesia`.

These topics describe implemented scope; do not add printer/thermal topics until named printer models have passed hardware tests.

## Harden repository settings

- Keep visibility **Public** as requested.
- Enable secret scanning, Dependabot alerts/security updates, and private vulnerability reporting where available.
- Protect `main`: require a pull request and a successful **Android ABI APKs** check; disallow force-push and branch deletion.
- Keep Actions workflow permissions read-only. The build workflow needs no secrets.
- Do not create release signing secrets until release signing is designed. Never commit credentials or production data.

After the first push, use **Actions → Android ABI APKs → Run workflow** or push a follow-up commit to `main` to produce the two debug APK artifacts.
