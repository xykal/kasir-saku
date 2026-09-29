# Security policy

## Project status

Kasir Saku is an early prototype and must not be used for production transactions. Current data is stored locally without a backup/restore flow. The application does not process or verify payments.

## Reporting a vulnerability

Please report suspected vulnerabilities through GitHub's **private vulnerability reporting / security advisories** for this repository when enabled. If it is unavailable, contact the repository owner privately through GitHub. Do not open a public issue containing exploit steps, credentials, customer information, or device data.

Include the affected version/commit, Android version and device model, impact, and a minimal reproduction. Do not attach real business or customer data.

## Response

The maintainer will acknowledge a report when available, investigate it, and coordinate a fix and disclosure timeline with the reporter. No response-time SLA is promised for this prototype.

## Current boundaries

- No login, cloud sync, or payment gateway is implemented.
- Product and transaction data are stored in the application's local SQLite database.
- Backup/restore, encrypted exports, printer SDKs, and release signing are not implemented.
- Do not commit API keys, signing keys, customer data, database files, or production exports.
