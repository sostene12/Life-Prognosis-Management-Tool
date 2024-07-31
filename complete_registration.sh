#!/bin/bash

uuid=$1
firstName=$2
lastName=$3
dob=$4
hasHIV=$5
diagnosisDate=$6
isOnART=$7
startedART=$8
countryISO=$9
password=${10}

# Check if UUID exists in user-store.txt
uuid_exists=false
while IFS=: read -r stored_firstname stored_lastname stored_email stored_uuid role stored_hashed_password; do
    if [[ "$stored_uuid" == "$uuid" ]]; then
        uuid_exists=true
        break
    fi
done < "user-store.txt"

if $uuid_exists; then
    # Hash the password
    hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

    # Replace null values with empty strings
    [ "$diagnosisDate" = "null" ] && diagnosisDate=""
    [ "$startedART" = "null" ] && startedART=""

    # Store patient data in patient.txt
    echo "$uuid:$dob:$hasHIV:$diagnosisDate:$isOnART:$startedART:$countryISO" >> "patient.txt"

    # Update user-store.txt
    awk -v uuid="$uuid" -v hashed_password="$hashed_password" \
        -v firstName="$firstName" -v lastName="$lastName" \
        -F: -v OFS=: \
        '{ if ($4 == uuid) { $1=firstName; $2=lastName; $6=hashed_password; } print }' user-store.txt > user-store.tmp && mv user-store.tmp user-store.txt

    echo "Registration completed for UUID: $uuid"
    exit 0
else
    echo "Info: UUID does not exist."
    exit 0
fi
