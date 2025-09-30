#!/bin/bash

# Exit on error
set -e

# Configuration
OPENAPI_GENERATOR_VERSION="7.3.0"
OPENAPI_SPEC_PATH="../backend/api/openapi.yaml"
OUTPUT_DIR="./src/api/generated"

# Create output directory if it doesn't exist
mkdir -p "$OUTPUT_DIR"

# Download OpenAPI Generator if not present
if [ ! -f "openapi-generator-cli.jar" ]; then
    echo "Downloading OpenAPI Generator..."
    curl -L "https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/${OPENAPI_GENERATOR_VERSION}/openapi-generator-cli-${OPENAPI_GENERATOR_VERSION}.jar" -o openapi-generator-cli.jar
fi

# Generate TypeScript client
echo "Generating TypeScript client..."
java -jar openapi-generator-cli.jar generate \
    -i "$OPENAPI_SPEC_PATH" \
    -g typescript-fetch \
    -o "$OUTPUT_DIR" \
    --additional-properties=supportsES6=true,withSeparateModelsAndApi=true

echo "TypeScript client generated successfully in $OUTPUT_DIR" 