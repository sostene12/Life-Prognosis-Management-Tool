#!/bin/bash

# Function to initiate patient registration by admin
initiate_registration() {
    echo "Initiating patient registration..."
    read -p "Enter patient's email: " email
    uuid=$(uuidgen)
    echo "$email:$uuid:Patient:" >> "user-store.txt"
    echo "Patient registration initiated with UUID: $uuid"
}

# Function for patient to complete registration
complete_registration() {
    echo "Completing patient registration..."
    read -p "Enter UUID: " uuid
    read -p "Enter first name: " first_name
    read -p "Enter last name: " last_name
    read -p "Enter date of birth (YYYY-MM-DD): " dob
    read -p "Are you HIV positive? (yes/no): " has_hiv

    if [[ "$has_hiv" == "yes" ]]; then
        read -p "Enter diagnosis date (YYYY-MM-DD): " diagnosis_date
        read -p "Are you on ART drugs? (yes/no): " on_art
        if [[ "$on_art" == "yes" ]]; then
            read -p "Enter the date you started ART (YYYY-MM-DD): " start_art
        fi
    fi

    read -p "Enter country ISO code: " country_iso
    read -p "Enter password: " password

    # Hash the password (for demonstration, not secure)
    hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

    # Update the patient's information in user-store.txt
    sed -i "/$uuid/s/:Patient:/:" "user-store.txt"
    echo "$uuid:$first_name:$last_name:$dob:$has_hiv:$diagnosis_date:$on_art:$start_art:$country_iso:$hashed_password" >> "user-store.txt"
    echo "Patient registration completed successfully."
}

# Function for user login
login_user() {
    echo "Logging in..."
    read -p "Enter your email: " email
    read -p "Enter your password: " password

    # Hash the input password for comparison
    hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

    # Check if user exists in user-store.txt and password matches
    while IFS=: read -r stored_email uuid role first_name last_name stored_hashed_password; do
        if [[ "$stored_email" == "$email" && "$stored_hashed_password" == "$hashed_password" ]]; then
            echo "$role:$first_name:$last_name"  # Return user data
            exit 0
        fi
    done < "user-store.txt"

    echo "Login failed: Invalid email or password."
    exit 1
}

# Main menu
while true; do
    echo "1. Initiate Patient Registration"
    echo "2. Complete Patient Registration"
    echo "3. Login User"
    echo "4. Exit"
    read -p "Choose an option: " option

    case $option in
        1) initiate_registration ;;
        2) complete_registration ;;
        3) login_user ;;
        4) exit ;;
        *) echo "Invalid option" ;;
    esac
done
