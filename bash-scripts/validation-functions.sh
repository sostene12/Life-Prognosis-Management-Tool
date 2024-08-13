#!/bin/bash

# Define constants for file paths and timestamps
CSV_FILE="files/life-expectancy.csv"

# Function to validate country ISO code
validate_countryISO() {
    local iso_code=$1

    local result=$(awk -F, -v iso="$iso_code" '$4 == iso || $5 == iso {print iso}' "$CSV_FILE")

    if [[ -z "$result" ]]; then
        echo "Unknown"
    else
        echo $result
    fi
}


# Main logic to dispatch function calls based on command-line arguments
case $1 in
    "validateISO")
        validate_countryISO "$2" ;;
    *)
        echo "Invalid command."
        exit 1
        ;;
esac