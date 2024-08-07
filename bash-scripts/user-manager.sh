#!/bin/bash

# Get current date and time
CSV_FILE="files/life-expectancy.csv"
timestamp=$(date +"%Y-%m-%d_%H:%M:%S")

# Files
USER_STORE="data-store/user-store.txt"
PATIENT_STORE="data-store/patient.txt"
USER_DATA_CSV="exports/user_data_$timestamp.csv"
ANALYTICS_CSV="exports/analytics_$timestamp.csv"


source bash-scripts/validation-functions.sh

# Function to initialize user-store.txt with an admin user
initialize_user_store() {
    local admin_password="pass123"
    local hashed_password
    hashed_password=$(hash_password "$admin_password")

    if [ ! -f $USER_STORE ]; then
        echo "Creating $USER_STORE and initializing with admin user."
        echo "hirwa:jc:admin@admin.com:$(uuidgen):Admin:$hashed_password:1" > $USER_STORE
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

# Function to check if UUID and email exist
check_uuid_email() {
    local uuid=$1
    local email=$2
    local uuid_exists=false
    local is_registered
    while IFS=: read -r _ _ stored_email stored_uuid _ _ was_registered; do
        if [[ "$stored_uuid" == "$uuid" && "$stored_email" == "$email" ]]; then
            uuid_exists=true
            
            if [[ "$was_registered" == "1" ]]; then
                is_registered="1"
            else
                is_registered="0"
            fi
            break
        fi
    done < $USER_STORE

    if [[ $uuid_exists && $is_registered == "0" ]]; then
        echo "0"  # UUID exists and is linked with email
    elif [[ $uuid_exists && $is_registered == "1" ]]; then
        echo "1"  # user had completed their registration
    else
        echo "2"  # UUID does not exist or is not linked with email
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
    local registered=1

    # Validate inputs
    validate_firstName "$firstName"
    validate_lastName "$lastName"
    validate_dob "$dob"
    validate_hasHIV "$hasHIV"
    validate_diagnosisDate "$diagnosisDate"
    validate_isOnART "$isOnART"
    validate_startedART "$startedART"
    validate_countryISO "$countryISO"
    validate_password "$password"

    # Check if UUID exists
    local hashed_password
    hashed_password=$(hash_password "$password")

    [ "$diagnosisDate" = "null" ] && diagnosisDate=""
    [ "$startedART" = "null" ] && startedART=""

    echo "$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO" >> $PATIENT_STORE

    # Update user-store
    awk -v uuid="$uuid" -v hashed_password="$hashed_password" \
        -v firstName="$firstName" -v lastName="$lastName" \
        -v registered="$registered" \
        -F: -v OFS=: '
        {
            if ($4 == uuid) {
                $1 = firstName;
                $2 = lastName;
                $6 = hashed_password;
                $7 = registered
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

    while IFS=: read -r firstname lastname stored_email _ role stored_hashed_password _; do
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
        echo "null:null:$email:$uuid:Patient:null:0" >> $USER_STORE
        echo "Patient registered with email: $email and UUID: $uuid"
    fi
}

get_uuid() {
    local email=$1
    while IFS=: read -r _ _ stored_email stored_uuid _ _ _; do
        if [[ "$stored_email" == "$email" ]]; then
            echo "$stored_uuid"
            break
        fi
    done < "$USER_STORE"
}


get_patient_info() {
    local uuid=$1
    while IFS=: read -r stored_uuid dateOfBirth hasHIV diagnosisDate isOnART startedART countryISO; do
        if [[ "$stored_uuid" == "$uuid" ]]; then
            echo "$dateOfBirth:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO"
            exit 0
        fi
    done < "$PATIENT_STORE"

    echo "null"
    exit 0
}

get_country_lifespan(){
    local iso_code=$1
    local result=$(awk -F, -v iso="$iso_code" '$4 == iso || $5 == iso {print $7}' "$CSV_FILE")

    if [[ -z "$result" ]]; then
        echo 0
    else
        echo $result
    fi
}

export_user_data() {
    local input_file=$1 # user-store file
    local output_file=$2

    # Write the CSV header
    echo "FirstName,LastName,Email,UUID,Role" > "$output_file"

    echo "Data exported to $output_file"
}

# Function to export analytical data to CSV format
export_analytics() {
    local patient_file=$1 # patient file
    local user_store_file=$2 # user-store file
    local output_file=$3

    # Write the CSV header
    echo "FirstName,LastName,Email,DOB,HIVStatus,DiagnosisDate,ARTStatus,ARTStartDate,CountryISO" > "$output_file"

    echo "Data exported to $output_file"
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
        check_uuid_email "$2" "$3" ;;
    "export_user_data")
        export_user_data "$USER_STORE" "$USER_DATA_CSV" ;;
    "export_analytics")
        export_analytics "$PATIENT_STORE" "$USER_STORE" "$ANALYTICS_CSV" ;;
    "get_uuid")
        get_uuid "$2";;
    "get_patient_info")
        get_patient_info "$2";;
    "get_country_lifespan")
        get_country_lifespan "$2";;
    *)
        echo "Invalid command."
        exit 1
        ;;
esac