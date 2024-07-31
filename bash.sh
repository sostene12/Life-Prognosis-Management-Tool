#!/bin/bash

# Function to check if UUID exists
check_uuid() {
    local uuid=$1
    while IFS=: read -r _ _ _ stored_uuid _ _; do
        if [[ "$stored_uuid" == "$uuid" ]]; then
            return 0
        fi
    done < "user-store.txt"
    return 1
}

# Function to complete registration
complete_registration() {
    local uuid=$1
    local firstName=$2
    local lastName=$3
    local dob=$4
    local hasHIV=$5
    local diagnosisDate=$6
    local isOnART=$7
    local startedART=$8
    local countryISO=$9
    local password=${10}

    if check_uuid "$uuid"; then
        local hashed_password
        hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

        [ "$diagnosisDate" = "null" ] && diagnosisDate=""
        [ "$startedART" = "null" ] && startedART=""

        echo "$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO" >> "patient.txt"

        awk -v uuid="$uuid" -v hashed_password="$hashed_password" \
            -v firstName="$firstName" -v lastName="$lastName" \
            -F: -v OFS=: \
            '{ if ($4 == uuid) { $1=firstName; $2=lastName; $6=hashed_password; } print }' user-store.txt > user-store.tmp && mv user-store.tmp user-store.txt

        echo "Registration completed for UUID: $uuid"
        exit 0
    else
        echo "Error: UUID does not exist."
        exit 1
    fi
}

# Function to login user
login_user() {
    local email=$1
    local password=$2

    local hashed_password
    hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

    while IFS=: read -r firstname lastname stored_email _ _ role stored_hashed_password; do
        if [[ "$stored_email" == "$email" && "$stored_hashed_password" == "$hashed_password" ]]; then
            echo "$role:$firstname:$lastname"
            exit 0
        fi
    done < "user-store.txt"

    echo "null"
    exit 1
}

# Main logic to dispatch function calls based on parameters
case $1 in
    "complete_registration")
        complete_registration "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9" "${10}" "${11}"
        ;;
    "login_user")
        login_user "$2" "$3"
        ;;
    *)
        echo "Invalid command. Use 'complete_registration' or 'login_user'."
        exit 1
        ;;
esac
