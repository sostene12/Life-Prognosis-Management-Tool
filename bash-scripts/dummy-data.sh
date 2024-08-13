#!/bin/bash

# Define file paths
INPUT_FILE="data-store/patient.txt"

# Number of patients to add
num_patients=500

# Function to generate a random date within a specific range
generate_random_date() {
    local start_date=$1
    local end_date=$2
    local start_sec=$(date -d "$start_date" +%s)
    local end_sec=$(date -d "$end_date" +%s)
    local random_sec=$((start_sec + RANDOM % (end_sec - start_sec)))
    echo $(date -d @"$random_sec" +%Y-%m-%d)
}

# Function to generate a random country ISO code
generate_random_country_iso() {
    local countries=("RW" "UG" "KE" "TZ" "NG" "ZA" "GH" "SD" "EG" "SN")
    echo ${countries[RANDOM % ${#countries[@]}]}
}

# Function to generate a random UUID (simulating a unique identifier)
generate_random_uuid() {
    echo $(uuidgen)
}

# Function to generate patient data
generate_patient_data() {
    for ((i=1; i<=num_patients; i++))
    do
        uuid=$(generate_random_uuid)

        # Generate random dates with different ranges for each patient
        dob=$(generate_random_date "1950-01-01" "1995-12-31")
        diagnosisDate=$(generate_random_date "2000-01-01" "2015-12-31")
        startedART=$(generate_random_date "2005-01-01" "2024-08-13")

        hasHIV="yes"
        isOnART="yes"
        countryISO=$(generate_random_country_iso)

        # Ensure that startedART date is after diagnosisDate
        while [[ "$startedART" < "$diagnosisDate" ]]; do
            startedART=$(generate_random_date "2005-01-01" "2024-08-13")
        done

        # Write the patient data to the file
        echo "$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO" >> "$INPUT_FILE"
    done
}

# Generate and add patients
generate_patient_data

echo "$num_patients HIV-positive patients added to $INPUT_FILE."
