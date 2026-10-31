# SettleBook
SettleBook sends payouts and keeps a correct, double-entry record of the
money, even when payment rails take money back in different ways and at
different times.
Status: week 1. The project skeleton and a mock payment rail. No ledger code
yet.
## What you need
- Git
- Docker Desktop, running
- Java 21 (for example Eclipse Temurin 21)
  You do not need to install Maven. The project includes the Maven wrapper,
  which downloads the right Maven for you.
  Commands below are for macOS and Linux. On Windows PowerShell, type
  `.\mvnw.cmd` instead of `./mvnw`.
## Start in 5 steps
1. Get the code:
   git clone https://github.com/ahlawat-dev/settlebook.git
   cd settlebook
   
2. Start the database:
   docker compose up -d
   PostgreSQL 16 now runs on localhost:5432. The database, user and password
   are all `settlebook`. These are for local use only.

3. Check the database:
   docker compose exec postgres psql -U settlebook -c "select version();"
   You should see a line starting with `PostgreSQL 16`.
4. Build and test everything:
   ./mvnw verify
   The first run downloads libraries and takes a few minutes. You should see
   `BUILD SUCCESS`.

5. Run the app once, so Flyway applies the database migrations:
   ./mvnw install -DskipTests
   ./mvnw -pl payout-api spring-boot:run
   Wait for `Started PayoutApiApplication`. In a second terminal, run:
   docker compose exec postgres psql -U settlebook -c "select version,
   description, success from flyway_schema_history;"
   You should see `1 | baseline | t`. Stop the app with Ctrl+C.
## Stop
- `docker compose stop` pauses the database and keeps its data.
- `docker compose down` removes it. The next `up` starts with an empty
  database.
## Modules
| Module | What it is |
| --- | --- |
| ledger-core | The double-entry ledger. It has no dependency on any payout
module, so the build fails if payout code leaks in. Migrations live in
`ledger-core/src/main/resources/db/migration`. |
| payout-orchestrator | Moves each payout through its states. Empty for now.
|
| payout-api | The front door. This week it is also the app that runs Flyway.
|
| mock-rail-provider | A fake payment rail for tests. See its own README. |
| reconciler | Scheduled checks of our books against the outside world. Empty
for now. |

## Troubleshooting
- **Port 5432 is already in use.** You probably run PostgreSQL locally.
  Change `"5432:5432"` to `"5433:5432"` in `docker-compose.yml`, and the port
  in `payout-api/src/main/resources/application.yml`.
- **Wrong Java version.** `java -version` must show 21, and JAVA_HOME must
  point to Java 21.
- **`Could not find artifact com.settlebook:ledger-core`.** Run `./mvnw
install -DskipTests` before running the app.