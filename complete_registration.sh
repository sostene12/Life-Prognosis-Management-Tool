#!/bin/bash

uuid=$1
dob=$2
has_hiv=$3
diagnosis_date=$4
is_on_art=$5
started_art=$6
country_iso=$7
hashed_password=$8

# Update the patient's information in user-store.txt
sed -i "/$uuid/s/:Patient:/:" "user-store.txt"
echo "$uuid:$dob:$has_hiv:$diagnosis_date:$is_on_art:$started_art:$country_iso:$hashed_password" >> "user-store.txt"
echo "Patient registration completed successfully."
