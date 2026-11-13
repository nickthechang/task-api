# JWT signing key configuration and rotation

The base and development profiles previously stored JWT signing keys in the repository, and tests reused the development key. **Treat those old keys as compromised**, even after this fix: they remain in Git history.

Before starting any application environment, provide a **new, random** `JWT_SECRET` of at least 32 UTF-8 bytes using a secret manager or deployment environment. Do not reuse the old values. One local generation method is `python3 -c 'import secrets; print(secrets.token_urlsafe(48))'`; store the output securely, never in Git, CI logs, issues, or chat.

Missing, blank, or weak secrets now fail startup; nonpositive JWT expiration is also refused. Changing the secret invalidates existing tokens and requires users to log in again. No database migration is required for this key rotation.

The app no longer automatically activates the `dev` profile. For local development explicitly use `SPRING_PROFILES_ACTIVE=dev`; production must select its intended profile and supply datasource settings as environment configuration. Default Hibernate SQL/bind parameter logging is disabled.

**Security work still outstanding:** task ownership/authorization requires a separate schema migration and multi-user integration tests; tracked dev database credentials should be rotated and removed; production `ddl-auto=update` requires review. Do not deploy this change until the new secret is provisioned and relevant checks pass. The keys in `src/test/resources` and unit tests are synthetic and must never be used by a deployment.
