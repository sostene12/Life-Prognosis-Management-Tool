#!/bin/bash

email=$1
uuid=$2

# Logic to register the patient in the user-store.txt
echo "$email:$uuid:Patient:" >> "user-store.txt"
echo "Patient registered with email: $email and UUID: $uuid"
