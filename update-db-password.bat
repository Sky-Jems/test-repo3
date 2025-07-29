@echo off
cd "%SystemDrive%\PostgreSQLBinary\pgsql\bin"

set PGUSER=postgres
set PGPASSWORD=%1

psql -U %PGUSER% -d postgres -c "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_authid WHERE rolname='postgres' AND rolpassword IS NOT NULL) THEN ALTER USER postgres WITH PASSWORD '%1'; END IF; END $$;"