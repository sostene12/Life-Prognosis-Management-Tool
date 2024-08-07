#!/bin/bash

USER_STORE="data-store/user-store.txt"
PATIENT_STORE="data-store/patient.txt"

update_patient_profile(){
    local email=$1
    local firstName=$2
    local lastName=$3
    local dob=$4
    local hasHIV=$5
    local diagnosisDate=$6
    local isOnART=$7
    local startedART=$8
    local countryISO=$9

    # Validate the input (simplified validation)
    [[ -z "$firstName" ]] && { echo "Error: First name is required"; exit 1; }
    [[ -z "$lastName" ]] && { echo "Error: Last name is required"; exit 1; }
    [[ -z "$dob" ]] && { echo "Error: Date of birth is required"; exit 1; }
    [[ "$hasHIV" != "yes" && "$hasHIV" != "no" ]] && { echo "Error: HIV status must be 'yes' or 'no'"; exit 1; }
    [[ -z "$countryISO" ]] && { echo "Error: Country ISO is required"; exit 1; }

    # Check if the user exists
    local user_line
    user_line=$(grep ":$email:" "$USER_STORE")

    if [[ -z "$user_line" ]]; then
        echo "Error: User with email $email not found."
        exit 1
    fi

    # Extract the user's uuid and other existing data
    local uuid
    local existing_password
    local existing_registered
    IFS=':' read -r _ _ _ uuid _ existing_password existing_registered <<< "$user_line"

    # Update user-store.txt while preserving existing data
    sed -i "s/^.*:$email:.*$/$firstName:$lastName:$email:$uuid:Patient:$existing_password:$existing_registered/" "$USER_STORE"

    # Update or add entry in patient.txt
    if grep -q "^$uuid:" "$PATIENT_STORE"; then
        sed -i "s/^$uuid:.*$/$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO/" "$PATIENT_STORE"
    else
        echo "$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO" >> "$PATIENT_STORE"
    fi

    echo "Profile updated successfully for $email"
}

# Main logic to dispatch function calls based on parameters
case $1 in
    "update_patient_profile")
        update_patient_profile "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9" "${10}" ;;
    *)
        echo "Invalid command."
        exit 1
        ;;
esac