#!/bin/bash

# Function to calculate the difference in years between two dates
calculate_date_difference() {
    local date1=$1
    local date2=$2
    local d1=$(date -d "$date1" +%s)
    local d2=$(date -d "$date2" +%s)
    local diff=$(( (d2 - d1) / 86400 / 365 )) # Convert seconds to years
    echo "$diff"
}

# Function to calculate the estimated lifespan
calculate_lifespan() {
    local uuid=$1

    # Get patient information from user-manager.sh
    local patientInformation=$(bash-scripts/user-manager.sh get_patient_info "$uuid")

    IFS=":" read -r dob hasHIV diagnosisDate isOnART startedART countryISO <<< "$patientInformation"

    # Set default values
    local factor=0.9
    local maxNotOnART=5 # Maximum expected lifespan without ART (in years)

    # Retrieve country-specific life expectancy
    local countryLifeExpectancy=$(bash-scripts/user-manager.sh get_country_lifespan "$countryISO")

    # Default lifespan calculation
    local defaultLifeSpan=$(echo "scale=2; $countryLifeExpectancy - $(calculate_date_difference "$dob" "$diagnosisDate")" | bc)

    if [ "$hasHIV" == "yes" ]; then
        local calculatedLifespan

        if [ "$isOnART" == "yes" ]; then
            local delayBeforeART=$(calculate_date_difference "$diagnosisDate" "$startedART")
            local yearsOnART=$(calculate_date_difference "$startedART" "$(date +%Y-%m-%d)")
            local delayFactor=$(echo "scale=10; e($delayBeforeART * l($factor))" | bc -l)
            calculatedLifespan=$(echo "scale=2; $defaultLifeSpan * $factor * $delayFactor - $yearsOnART" | bc -l)
        else
            local yearsAfterDiagnosis=$(calculate_date_difference "$diagnosisDate" "$(date +%Y-%m-%d)")
            if [ "$(echo "$defaultLifeSpan > $maxNotOnART" | bc)" -eq 1 ]; then
                calculatedLifespan=$(echo "scale=2; $maxNotOnART - $yearsAfterDiagnosis" | bc)
            else
                calculatedLifespan=$(echo "scale=2; $defaultLifeSpan - $yearsAfterDiagnosis" | bc)
            fi
        fi
        echo $(echo "scale=2; $calculatedLifespan" | bc)
    else
        echo $(echo "scale=2; $defaultLifeSpan" | bc)
    fi
}

# Define timestamp
timestamp=$(date +"%Y-%m-%d_%H:%M:%S")

# Define file paths
INPUT_FILE="data-store/patient.txt"
TEMP_FILE="data-store/lifespan_temp.txt"
OUTPUT_FILE="exports/analytics_$timestamp.csv"

# Initialize the CSV file
echo "Metric,Value" > "$OUTPUT_FILE"

# Total number of records
total_records=$(wc -l < "$INPUT_FILE")
echo "Total records,$total_records" >> "$OUTPUT_FILE"

# Count records with HIV
count_hiv_yes=$(grep ":yes:" "$INPUT_FILE" | wc -l)
count_hiv_no=$(grep ":no:" "$INPUT_FILE" | wc -l)
echo "HIV status: yes,$count_hiv_yes" >> "$OUTPUT_FILE"
echo "HIV status: no,$count_hiv_no" >> "$OUTPUT_FILE"

# Count records where isOnART is yes or no
count_art_yes=$(grep ":yes:$" "$INPUT_FILE" | wc -l)
count_art_no=$(grep ":no:$" "$INPUT_FILE" | wc -l)
echo "ART status: yes,$count_art_yes" >> "$OUTPUT_FILE"
echo "ART status: no,$count_art_no" >> "$OUTPUT_FILE"

# Count records by countryISO for HIV positive cases only
echo "Country ISO (HIV positive),Count" >> "$OUTPUT_FILE"
awk -F: '$3 == "yes" {print $7}' "$INPUT_FILE" | sort | uniq -c | awk '{print "Country ISO: " $2 "," $1}' >> "$OUTPUT_FILE"

# Calculate average lifespan by country
echo "Country ISO,Average Lifespan (years)" >> "$OUTPUT_FILE"

# Extract records and calculate lifespan
while IFS=: read -r uuid dob hasHIV diagnosisDate isOnART startedART countryISO; do
    # Skip lines with missing data
    if [ -z "$startedART" ] || [ -z "$diagnosisDate" ]; then
        continue
    fi

    # Only process HIV positive cases
    if [ "$hasHIV" == "yes" ]; then
        # Calculate lifespan
        lifespan=$(calculate_lifespan "$uuid")

        # Accumulate lifespan by country
        echo "$countryISO,$lifespan" >> "$TEMP_FILE"
    fi
done < "$INPUT_FILE"

# Calculate average lifespan by country
awk -F, '
{
    country[$1] += $2
    count[$1]++
}
END {
    for (c in country) {
        print c "," (country[c] / count[c])
    }
}
' "$TEMP_FILE" >> "$OUTPUT_FILE"

# Clean up temporary files
rm "$TEMP_FILE"

echo "Analytics have been written to $OUTPUT_FILE."
