#!/bin/bash

# Script to build all microservices and collect their JAR files

# Create a directory to store all JAR files
mkdir -p jar-releases

# Install common first (dependency of others)
if [ -d "common" ]; then
    echo "Installing common..."
    cd common
    ./mvnw clean install -DskipTests
    cd ..
    echo "common installed"
else
    echo "Error: common module not found"
    exit 1
fi

# List of all microservices
SERVICES=(
    "auth-service"
    "billing-service"
    "discount-service"
    "gateway-service"
    "order-orchestrator-service"
    "order-service"
    "payment-service"
    "product-service"
)

# Build each service and copy its JAR file
for service in "${SERVICES[@]}"; do
    echo "Building $service..."
    
    # Check if the service directory exists
    if [ -d "$service" ]; then
        # Navigate to the service directory
        cd "$service"
        
        # Build the service with Maven
        ./mvnw clean package -DskipTests
        
        # Find the JAR file and copy it to the jars directory
        find ./target -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} ../jar-releases/ \;
        
        # Go back to the parent directory
        cd ..
        
        echo "$service built successfully"
    else
        echo "Warning: $service directory not found, skipping"
    fi
done

echo "All services built. JAR files are available in the 'jars' directory."