#!/bin/bash

# Hash the admin password (for demonstration, not secure)
admin_password="password123"
hashed_password=$(echo -n "$admin_password" | sha256sum | awk '{print $1}')

# Check if user-store.txt exists
if [ ! -f user-store.txt ]; then
    echo "Creating user-store.txt and initializing with admin user."
    echo "hirwa:jc:admin@admin.com:$(uuidgen):Admin:$hashed_password" > user-store.txt
    echo "Admin user initialized."
else
    echo "user-store.txt already exists."
fi
