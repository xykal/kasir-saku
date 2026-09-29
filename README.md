# Kasir Saku

[![Android ABI APKs](https://github.com/xykal/kasir-saku/actions/workflows/android-build.yml/badge.svg?branch=main)](https://github.com/xykal/kasir-saku/actions/workflows/android-build.yml)

## Bahasa Indonesia

Kasir Saku adalah aplikasi kasir Android yang sedang dikembangkan untuk usaha kecil. Fokus awalnya: transaksi offline satu perangkat, stok, dan tata letak adaptif untuk ponsel/tablet. Build CI menghasilkan dua APK debug terpisah: `arm64-v8a` dan `armeabi-v7a`—tanpa APK universal.

**Status:** prototipe aktif, belum siap produksi. Cetak printer, invoice PDF, backup/pemulihan, serta uji perangkat nyata belum selesai. Jangan masukkan data pelanggan atau transaksi nyata.

## English

Kasir Saku is an **early Android POS project** for small shops. The first milestone focuses on one device, local transactions, stock checks, and a tablet-friendly cashier layout.

> **Status:** prototype / active development. Not production-ready. Printer support, backup/restore, and migration tests are not implemented yet.

## What exists now

- Kotlin + Jetpack Compose app shell with custom line icons and adaptive phone/tablet layout.
- Local SQLite database with sample items, product creation, cart, stock decrement, transaction history, and a daily summary.
- Tunai, QRIS, and transfer can be recorded as payment methods. QRIS and transfer are **not verified automatically**.
- GitHub Actions workflow builds separate `arm64-v8a` and `armeabi-v7a` debug APK artifacts. Universal APK generation is disabled.

## Not implemented

Thermal Bluetooth/USB printing, PDF invoice/receipt, barcode scanning, discounts, returns, expenses, customer credit, data backup/restore, cloud sync, and production data migration. No custom `.so` library is included. Test each POS terminal and printer model before claiming support.

## Build APKs with GitHub Actions

1. Open the [public repository](https://github.com/xykal/kasir-saku).
2. Builds run on pushes to `main`; a manual run is available under **Actions → Android ABI APKs → Run workflow**.
3. Download the separate artifacts: `kasir-saku-arm64-v8a` and `kasir-saku-armeabi-v7a`.

The workflow checks that both ABI outputs exist and rejects a universal APK. These are **debug APKs**, not signed release builds. Release signing is intentionally not configured; add signing credentials through GitHub Secrets only after selecting a release process.

## Local development

Requirements: Android Studio, Android SDK 37.0, JDK 17, and Gradle 9.4.1. Create the wrapper once from this directory with `gradle wrapper --gradle-version 9.4.1`, then run `./gradlew :app:assembleDebug`. GitHub Actions has successfully built both ABI debug APKs; real phone, tablet, and printer hardware testing is still outstanding.

The minimum Android API is 23. ABI describes CPU architecture—not screen size. The Compose layout adapts separately for phones, tablets, and landscape displays.

## Data and privacy

Current transaction and product data stay in the app's local SQLite database. There is no account, analytics, network API, or payment processing in this milestone. Until backup/restore exists, uninstalling the app or losing the device can lose this data. Do not enter real customer or payment data into prototype builds.

## Design

See [`DESIGN.md`](DESIGN.md) for palette, font, icon, touch-target, and responsive-layout decisions. Inter is planned as a bundled font; the starter currently uses the Android system sans font.

## Security reports

See [`SECURITY.md`](SECURITY.md). Do not post credentials, customer records, or exploitable details in public issues.

## License

Licensed under the MIT License. See [`LICENSE`](LICENSE).
