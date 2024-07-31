#!/bin/bash

email=$1;
password=$2

# Hash the input password for comparison
hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

# Check if user exists in user-store.txt and password matches
while IFS=: read -r firstname lastname stored_email uuid role stored_hashed_password; do
    if [[ "$stored_email" == "$email" && "$stored_hashed_password" == "$hashed_password" ]]; then
        echo "$role:$firstname:$lastname"  # Return user data
        exit 0
    fi
done < "user-store.txt"
echo "null"
exit 1
