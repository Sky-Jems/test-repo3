@echo off
REM Script to build all microservices and collect their JAR files

REM Create a directory to store all JAR files
if not exist jars mkdir jars

REM List of all microservices
set SERVICES=auth-service billing-service common discount-service gateway-service notification-service order-orchestrator-service order-service payment-service product-service

REM Build each service and copy its JAR file
for %%s in (%SERVICES%) do (
    echo Building %%s...
    
    REM Check if the service directory exists
    if exist %%s (
        REM Navigate to the service directory
        cd %%s
        
        REM Build the service with Maven
        call mvnw.cmd clean package -DskipTests
        
        REM Find the JAR file and copy it to the jars directory
        for /r target %%f in (*.jar) do (
            if not "%%~nf"=="%%~nf-sources" if not "%%~nf"=="%%~nf-javadoc" (
                copy "%%f" ..\jars\
            )
        )
        
        REM Go back to the parent directory
        cd ..
        
        echo %%s built successfully
    ) else (
        echo Warning: %%s directory not found, skipping
    )
)

echo All services built. JAR files are available in the 'jars' directory.