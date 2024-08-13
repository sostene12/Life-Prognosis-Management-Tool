#!/bin/bash

# Function to validate country ISO code
validate_countryISO() {
    local countryISO=$1
    # Check if country ISO code consists of two uppercase letters
    if ! [[ "$countryISO" =~ ^[A-Z]{2}$ ]]; then
        echo "Info: Invalid country ISO code. Use two uppercase letters."
        exit 1
    fi
}