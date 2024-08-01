#!/bin/bash

# Validation Functions
validate_firstName() {
    local firstName=$1
    if [[ -z "$firstName" ]]; then
        echo "Info: First name cannot be empty."
        exit 1
    fi
}

validate_lastName() {
    local lastName=$1
    if [[ -z "$lastName" ]]; then
        echo "Info: Last name cannot be empty."
        exit 1
    fi
}

validate_dob() {
    local dob=$1
    if ! [[ "$dob" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        echo "Info: Invalid date of birth format. Use YYYY-MM-DD."
        exit 1
    fi
}

validate_hasHIV() {
    local hasHIV=$1
    if [[ "$hasHIV" != "yes" && "$hasHIV" != "no" ]]; then
        echo "Info: Invalid input for HIV status. Use 'yes' or 'no'."
        exit 1
    fi
}

validate_diagnosisDate() {
    local diagnosisDate=$1
    if [[ "$hasHIV" == "yes" && ! "$diagnosisDate" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        echo "Info: Invalid diagnosis date format. Use YYYY-MM-DD."
        exit 1
    fi
}

validate_isOnART() {
    local isOnART=$1
    if [[ "$isOnART" != "yes" && "$isOnART" != "no" ]]; then
        echo "Info: Invalid input for ART status. Use 'yes' or 'no'."
        exit 1
    fi
}

validate_startedART() {
    local startedART=$1
    if [[ "$isOnART" == "yes" && ! "$startedART" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        echo "Info: Invalid ART start date format. Use YYYY-MM-DD."
        exit 1
    fi
}

validate_countryISO() {
    local countryISO=$1
    if ! [[ "$countryISO" =~ ^[A-Z]{2}$ ]]; then
        echo "Info: Invalid country ISO code. Use two uppercase letters."
        exit 1
    fi
}

validate_password() {
    local password=$1
    if [[ -z "$password" ]]; then
        echo "Info: Password cannot be empty."
        exit 1
    fi
}