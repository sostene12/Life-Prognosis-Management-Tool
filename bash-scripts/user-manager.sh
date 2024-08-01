#!/bin/bash

USER_STORE="data-store/user-store.txt"
PATIENT_STORE="data-store/patient.txt"

# Function to initialize user-store.txt with an admin user
initialize_user_store() {
    local admin_password="pass123"
    local hashed_password
    hashed_password=$(hash_password "$admin_password")

    if [ ! -f $USER_STORE ]; then
        echo "Creating $USER_STORE and initializing with admin user."
        echo "hirwa:jc:admin@admin.com:$(uuidgen):Admin:$hashed_password" > $USER_STORE
        echo "Admin user initialized."
    else
        echo "$USER_STORE already exists."
    fi
}

# Function to hash a password using SHA-256
hash_password() {
    local password=$1
    echo -n "$password" | sha256sum | awk '{print $1}'
}

# Function to check if UUID exists
check_uuid() {
    local uuid=$1

    # Check if UUID exists
    local uuid_exists=false
    while IFS=: read -r _ _ _ stored_uuid _ _; do
        if [[ "$stored_uuid" == "$uuid" ]]; then
            uuid_exists=true
            break
        fi
    done < $USER_STORE

    if $uuid_exists; then
        echo "0"  # UUID exists
    else
        echo "1"  # UUID does not exist
    fi
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

    # Check if UUID exists
    local hashed_password
    hashed_password=$(hash_password "$password")

    [ "$diagnosisDate" = "null" ] && diagnosisDate=""
    [ "$startedART" = "null" ] && startedART=""

    echo "$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO" >> $PATIENT_STORE

    # Update user-store
    awk -v uuid="$uuid" -v hashed_password="$hashed_password" \
        -v firstName="$firstName" -v lastName="$lastName" \
        -F: -v OFS=: '
        {
            if ($4 == uuid) {
                $1 = firstName;
                $2 = lastName;
                $6 = hashed_password;
            }
            print
        }' $USER_STORE > user-store.tmp && mv user-store.tmp $USER_STORE

    echo "Registration completed for UUID: $uuid"
    exit 0
}

# Function to login user
login_user() {
    local email=$1
    local password=$2

    local hashed_password
    hashed_password=$(hash_password "$password")

    while IFS=: read -r firstname lastname stored_email _ role stored_hashed_password; do
        if [[ "$stored_email" == "$email" && "$stored_hashed_password" == "$hashed_password" ]]; then
            echo "$role:$firstname:$lastname"
            exit 0
        fi
    done < "$USER_STORE"

    echo "null"
    exit 0
}

# Function to check if email exists and register a patient
register_patient() {
    local email=$1
    local uuid=$2

    # Check if email already exists
    local email_exists=false
    while IFS=: read -r stored_email; do
        if [[ "$stored_email" == "$email" ]]; then
            email_exists=true
            break
        fi
    done < <(cut -d: -f3 $USER_STORE)

    if $email_exists; then
        echo "Info: Email already exists."
        exit 0
    else
        echo "null:null:$email:$uuid:Patient:null" >> $USER_STORE
        echo "Patient registered with email: $email and UUID: $uuid"
    fi
}

# Main logic to dispatch function calls based on parameters
case $1 in
    "initialize_user_store")
        initialize_user_store ;;
    "complete_registration")
        complete_registration "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9" "${10}" "${11}" ;;
    "login_user")
        login_user "$2" "$3" ;;
    "register_patient")
        register_patient "$2" "$3" ;;
    "check_uuid")
        check_uuid "$2" ;;
    *)
        echo "Invalid command. Use 'initialize_user_store', 'complete_registration', 'login_user', 'register_patient', or 'check_uuid'."
        exit 1
        ;;
esac