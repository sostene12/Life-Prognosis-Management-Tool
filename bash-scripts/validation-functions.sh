#!/bin/bash

# Function to validate first name
validate_firstName() {
    local firstName=$1
    # Check if the first name is empty
    if [[ -z "$firstName" ]]; then
        echo "Info: First name cannot be empty."
        exit 1
    fi
}

# Function to validate last name
validate_lastName() {
    local lastName=$1
    # Check if the last name is empty
    if [[ -z "$lastName" ]]; then
        echo "Info: Last name cannot be empty."
        exit 1
    fi
}

# Function to validate date of birth (DOB)
validate_dob() {
    local dob=$1
    # Check if DOB matches the format YYYY-MM-DD
    if ! [[ "$dob" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        echo "Info: Invalid date of birth format. Use YYYY-MM-DD."
        exit 1
    fi
}

# Function to validate HIV status
validate_hasHIV() {
    local hasHIV=$1
    # Check if HIV status is either 'yes' or 'no'
    if [[ "$hasHIV" != "yes" && "$hasHIV" != "no" ]]; then
        echo "Info: Invalid input for HIV status. Use 'yes' or 'no'."
        exit 1
    fi
}

# Function to validate diagnosis date
validate_diagnosisDate() {
    local diagnosisDate=$1
    # Check if diagnosis date matches the format YYYY-MM-DD and HIV status is 'yes'
    if [[ "$hasHIV" == "yes" && ! "$diagnosisDate" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        echo "Info: Invalid diagnosis date format. Use YYYY-MM-DD."
        exit 1
    fi
}

# Function to validate ART (Antiretroviral Therapy) status
validate_isOnART() {
    local isOnART=$1
    # Check if ART status is either 'yes' or 'no'
    if [[ "$isOnART" != "yes" && "$isOnART" != "no" ]]; then
        echo "Info: Invalid input for ART status. Use 'yes' or 'no'."
        exit 1
    fi
}

# Function to validate ART start date
validate_startedART() {
    local startedART=$1
    # Check if ART start date matches the format YYYY-MM-DD and ART status is 'yes'
    if [[ "$isOnART" == "yes" && ! "$startedART" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        echo "Info: Invalid ART start date format. Use YYYY-MM-DD."
        exit 1
    fi
}

# Function to validate country ISO code
validate_countryISO() {
    local countryISO=$1
    # Check if country ISO code consists of two uppercase letters
    if ! [[ "$countryISO" =~ ^[A-Z]{2}$ ]]; then
        echo "Info: Invalid country ISO code. Use two uppercase letters."
        exit 1
    fi
}

# Function to validate password
validate_password() {
    local password=$1
    # Check if the password is empty
    if [[ -z "$password" ]]; then
        echo "Info: Password cannot be empty."
        exit 1
    fi
}