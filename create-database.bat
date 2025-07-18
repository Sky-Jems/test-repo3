@echo off
cd "%SystemDrive%\PostgreSQLBinary\pgsql\bin"
setlocal

set PGUSER=postgres
set PGPASSWORD=%1

set DBS=auth_service_db billing_service_db discount_service_db product_service_db gateway_service_db order_service_db order_orchestrator_service_db payment_service_db 

for %%D in (%DBS%) do (
echo Creating database : %%D
psql -U %PGUSER% -d postgres -c "CREATE DATABASE %%D;"
)