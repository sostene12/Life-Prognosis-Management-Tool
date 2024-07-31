#!/bin/bash

# File to store user data
USER_STORE="user-store.txt"

# Function to register a new user
register_user() {
    echo "Registering new user..."
    read -p "Enter user's email: " email
    UUID=$(uuidgen)

    # Check if the email already exists
    if grep -q "$email" "$USER_STORE"; then
        echo "Email already registered. Please use a different email."
        return
    fi

    # Store user info (email and UUID)
    echo "$email:$UUID:Patient" >> "$USER_STORE"
    echo "User registered successfully with UUID: $UUID"
}

# Function for patient registration completion
complete_registration() {
    echo "Completing registration for patient..."
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

    # Here you would hash the password before storing it
    hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

    # Store patient data in a separate file or append to user-store.txt
    echo "$uuid:$first_name:$last_name:$dob:$has_hiv:$diagnosis_date:$on_art:$start_art:$country_iso:$hashed_password" >> "patients.txt"
    echo "Patient registration completed successfully."
}

# Function to login a user
login_user() {
    echo "Logging in..."
    read -p "Enter your email: " email
    read -p "Enter your password: " password

    # Hash the input password for comparison
    hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

    # Check if user exists and password matches
    while IFS=: read -r stored_email uuid first_name last_name dob has_hiv diagnosis_date on_art start_art country_iso stored_password; do
        if [[ "$stored_email" == "$email" && "$stored_password" == "$hashed_password" ]]; then
            echo "Login successful! Welcome, $first_name $last_name."
            return
        fi
    done < "patients.txt"

    echo "Login failed: Invalid email or password."
}

# Function to export user data
export_user_data() {
    echo "Exporting user data..."
    cp "patients.txt" "exported_patients.csv"
    echo "User data exported to exported_patients.csv."
}

# Main menu
while true; do
    echo "1. Register User"
    echo "2. Complete Patient Registration"
    echo "3. Login User"
    echo "4. Export User Data"
    echo "5. Exit"
    read -p "Choose an option: " option

    case $option in
        1) register_user ;;
        2) complete_registration ;;
        3) login_user ;;
        4) export_user_data ;;
        5) exit ;;
        *) echo "Invalid option" ;;
    esac
done