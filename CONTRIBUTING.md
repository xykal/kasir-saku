# Contributing

Thanks for helping improve Kasir Saku. The project is an early prototype; check the README for current scope and known gaps.

## Before opening a change

- Search existing issues first. For a large feature, open an issue to agree on scope before implementation.
- Keep changes focused. Avoid introducing network services, analytics, payment processing, or new native `.so` libraries without an explicit design decision.
- Never include credentials, signing keys, customer records, real transaction data, or private database files.

## Development checks

- Build with Android Studio or `./gradlew :app:assembleDebug` after the wrapper and Android SDK are available.
- Test both ABI APK outputs in CI: `arm64-v8a` and `armeabi-v7a`.
- For UI changes, verify phone, tablet, portrait, landscape, font scaling, and touch targets.
- For data changes, cover empty data, zero stock, insufficient stock, interrupted transactions, and schema migration.
- Printer support must name tested models, connection type, paper width, and Android version. Do not claim universal printer compatibility.

## Pull requests

Include the user problem, behavior changed, test evidence, limitations, and screenshots for UI changes. Do not mark builds verified unless a successful CI run is linked.
