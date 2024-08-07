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

    # Get current patient data
    local current_patient_data
    current_patient_data=$(grep "^$uuid:" "$PATIENT_STORE")

    if [[ -n "$current_patient_data" ]]; then
        IFS=':' read -r _ current_dob current_hasHIV current_diagnosisDate current_isOnART current_startedART current_countryISO <<< "$current_patient_data"

        # Use current values if new values are empty
        dob=${dob:-$current_dob}
        hasHIV=${hasHIV:-$current_hasHIV}
        countryISO=${countryISO:-$current_countryISO}

        if [[ "$hasHIV" == "yes" ]]; then
            diagnosisDate=${diagnosisDate:-$current_diagnosisDate}
            isOnART=${isOnART:-$current_isOnART}
            startedART=${startedART:-$current_startedART}
        else
            # Clear HIV-related data if status changed to "no"
            diagnosisDate="null"
            isOnART="null"
            startedART="null"
        fi

        # Update patient.txt
        sed -i "s/^$uuid:.*$/$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO/" "$PATIENT_STORE"
    else
        # Add new entry if not exists
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