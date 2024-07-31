#!/bin/bash

uuid=$1

# Check if UUID exists
uuid_exists=false
while IFS=: read -r _ _ _ stored_uuid _ _; do
    if [[ "$stored_uuid" == "$uuid" ]]; then
        uuid_exists=true
        break
    fi
done < "user-store.txt"

if $uuid_exists; then
    echo 1 # uuid exists
    exit 0
else
    echo 0 # uuid does not exists
    exit 0
fi
