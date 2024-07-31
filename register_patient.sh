#!/bin/bash

email=$1
uuid=$2

# Check if email already exists
email_exists=false
while IFS=: read -r stored_email; do
    if [[ "$stored_email" == "$email" ]]; then
        email_exists=true
        break
    fi
done < <(cut -d: -f3 user-store.txt)

if $email_exists; then
    echo "Info: Email already exists."
    exit 1
else
    echo "null:null:$email:$uuid:Patient:null" >> "user-store.txt"
    echo "Patient registered  email: $email and UUID: $uuid"
    exit 0
fi
