#!/bin/bash

# File to store user data
USER_STORE="user-store.txt"
PATIENT_DATA="patients.txt"

# Variables to track login status and user role
is_logged_in=false
user_role=""
current_patient_email=""

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

    # Hash the password (for demonstration, not secure)
    hashed_password=$(echo -n "$password" | sha256sum | awk '{print $1}')

    # Store patient data
    echo "$uuid:$first_name:$last_name:$dob:$has_hiv:$diagnosis_date:$on_art:$start_art:$country_iso:$hashed_password" >> "$PATIENT_DATA"
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
    while IFS=: read -r stored_email stored_hashed_password; do
        if [[ "$stored_email" == "$email" && "$stored_hashed_password" == "$hashed_password" ]]; then
            echo "Login successful!"
            is_logged_in=true
            user_role="Patient"
            current_patient_email="$email"
            return
        fi
    done < "$PATIENT_DATA"

    echo "Login failed: Invalid email or password."
}

# Function to export user data
export_user_data() {
    echo "Exporting user data..."
    cp "$PATIENT_DATA" "exported_patients.csv"
    echo "User data exported to exported_patients.csv."
}

# Function to calculate lifespan
calculate_lifespan() {
    # Placeholder for lifespan calculation logic
    # This should be based on the patient's data in the patients.txt file
    # Here we will just return a dummy value for demonstration
    echo "Calculating lifespan for patient: $current_patient_email"
    # Example calculation logic
    while IFS=: read -r uuid first_name last_name dob has_hiv diagnosis_date on_art start_art country_iso stored_password; do
        if [[ "$current_patient_email" == "$first_name" ]]; then
            # Example lifespan calculation based on dummy logic
            lifespan=69  # Assume average lifespan is 69 years
            echo "Your expected lifespan is approximately: $lifespan years."
            return
        fi
    done < "$PATIENT_DATA"
    echo "No data found for the current patient."
}

# Function to display admin menu
admin_menu() {
    while true; do
        echo "Admin Menu:"
        echo "1. Initialize Patient Registration"
        echo "2. Export User Data"
        echo "3. Logout"
        read -p "Choose an option: " option

        case $option in
            1) register_user ;;
            2) export_user_data ;;
            3) is_logged_in=false; user_role=""; echo "Logged out." ; break ;;
            *) echo "Invalid option" ;;
        esac
    done
}

# Function to display patient menu
patient_menu() {
    while true; do
        echo "Patient Menu:"
        echo "1. Complete Patient Registration"
        echo "2. View Lifespan"
        echo "3. View Profile"
        echo "4. Logout"
        read -p "Choose an option: " option

        case $option in
            1) complete_registration ;;
            2) calculate_lifespan ;;
            3) echo "Viewing profile is not implemented yet." ;;
            4) is_logged_in=false; user_role=""; echo "Logged out." ; break ;;
            *) echo "Invalid option" ;;
        esac
    done
}

# Main menu
while true; do
    if [ "$is_logged_in" = false ]; then
        echo "Welcome to Life Prognosis Management Tool"
        echo "_________________________________________"
        echo "1. Login"
        echo "2. Complete Registration"
        echo "3. Exit"
        read -p "Choose an option: " option

        case $option in
            1) login_user ;;
            2) complete_registration ;;
            3) exit ;;
            *) echo "Invalid option" ;;
        esac
    else
        if [ "$user_role" == "Admin" ]; then
            admin_menu
        elif [ "$user_role" == "Patient" ]; then
            patient_menu
        fi
    fi
done