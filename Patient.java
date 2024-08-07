import java.util.Date;

public class Patient extends User {
    // Patient-specific attributes
    private Date dateOfBirth;
    private String hasHIV;
    private Date diagnosisDate;
    private String isOnART;
    private Date startedART;
    private String countryISO;

    // Constructor for Patient class, initializing the user with the Patient role
    public Patient(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, UserRoles.PATIENT);
    }

    // Getter and setter for date of birth
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter and setter for HIV status
    public String getHasHIV() {
        return hasHIV;
    }

    public void setHasHIV(String hasHIV) {
        this.hasHIV = hasHIV;
    }

    // Getter and setter for diagnosis date
    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    // Getter and setter for ART status
    public String getIsOnART() {
        return isOnART;
    }

    public void setIsOnART(String isOnART) {
        this.isOnART = isOnART;
    }

    // Getter and setter for ART start date
    public Date getStartedART() {
        return startedART;
    }

    public void setStartedART(Date startedART) {
        this.startedART = startedART;
    }

    // Getter and setter for country ISO code
    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    // Completes the registration process for a patient
    public static void completeRegistration() {
        // Prompt user for UUID and email
        System.out.print("Enter UUID: ");
        String uuid = Main.getUserInput();
        System.out.print("Enter Your Email: ");
        String email = Main.getUserInput();

        // Check UUID validity and registration status
        String uuidCheckResult = Main.callBashScript("user-manager.sh", "check_uuid", uuid, email);
        if (uuidCheckResult.equals("1")) {
            System.out.println("Info: Registration already done.");
            return;
        } else if (uuidCheckResult.equals("2")) {
            System.out.println("Info: Authentication failed.");
            return;
        }

        // Prompt user for remaining registration details
        System.out.print("Enter Your firstname: ");
        String firstName = Main.getUserInput();
        System.out.print("Enter Your lastname: ");
        String lastName = Main.getUserInput();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = Main.getUserInput();
        System.out.print("Are you HIV positive? (yes/no): ");
        String hivStatus = Main.getUserInput();
        boolean hasHIV = hivStatus.equalsIgnoreCase("yes");

        String diagnosisDate = "null";
        boolean isOnART = false;
        String startedART = "null";

        // If HIV positive, ask for diagnosis date and ART status
        if (hasHIV) {
            System.out.print("Enter diagnosis date (YYYY-MM-DD): ");
            diagnosisDate = Main.getUserInput();
            System.out.print("Are you on ART drugs? (yes/no): ");
            String artStatus = Main.getUserInput();
            isOnART = artStatus.equalsIgnoreCase("yes");
            if (isOnART) {
                System.out.print("Enter the date you started ART (YYYY-MM-DD): ");
                startedART = Main.getUserInput();
            }
        }

        // Prompt for country ISO code and password
        System.out.print("Enter country ISO code: ");
        String countryISO = Main.getUserInput();
        System.out.print("Enter password: ");
        String password = Main.getUserInput();

        // Register patient via bash script
        String result = Main.callBashScript("user-manager.sh", "complete_registration", uuid, firstName, lastName, dob,
                hasHIV ? "yes" : "no", diagnosisDate, isOnART ? "yes" : "no", startedART, countryISO, password);
        System.out.println(result);
    }

    // Displays the patient's profile information
    public void viewProfile() {
        // Retrieve patient UUID using email
        String uuid = Main.callBashScript("user-manager.sh", "get_uuid", getEmail());

        // Get patient information from the system
        String patientInformation = Main.callBashScript("user-manager.sh", "get_patient_info", uuid);
        String[] patientInformationParts = patientInformation.split(":");

        // Set patient attributes from retrieved information
        setDateOfBirth(Main.parseDate(patientInformationParts[0]));
        setHasHIV(patientInformationParts[1]);
        setDiagnosisDate(Main.parseDate(patientInformationParts[2]));
        setIsOnART(patientInformationParts[3]);
        setStartedART(Main.parseDate(patientInformationParts[4]));
        setCountryISO(patientInformationParts[5]);

        // Display patient profile information
        System.out.println("My Profile\n______________________________\n");
        System.out.println("Firstname: " + getFirstName());
        System.out.println("Lastname: " + getLastName());
        System.out.println("Email: " + getEmail());
        System.out.println("HIV status: " + getHasHIV());
        System.out.println("Diagnosis Date: " + getDiagnosisDate());
        System.out.println("ART status: " + getIsOnART());
        System.out.println("Date - ART: " + getStartedART());
        System.out.println("Country: " + getCountryISO());
        System.out.println("_______________________");
    }

    public void updateProfile() {
        System.out.print("Enter your new first name: ");
        String newFirstName = Main.getUserInput();
        System.out.print("Enter your new last name: ");
        String newLastName = Main.getUserInput();
        System.out.print("Enter your new DOB (YYYY-MM-DD): ");
        String newDOB = Main.getUserInput();
        System.out.print("Enter your new HIV status (yes/no): ");
        String newHIVStatus = Main.getUserInput();
        System.out.print("Update on diagnosis date:");
        String newDiagnosisDate = Main.getUserInput();
        System.out.print("update on ART status (yes/no):");
        String newARTStatus = Main.getUserInput();
        System.out.print("Update on day you started ART (YYYY-MM-DD):");
        String newStartedART = Main.getUserInput();
        System.out.print("Enter your new country ISO code: ");
        String newCountryISO = Main.getUserInput();
        System.out.println();
        System.out.println("Updating your profile...");
        String result = Main.callBashScript("update-profile.sh", "update_patient_profile", this.getEmail(),
                newFirstName,
                newLastName, newDOB, newHIVStatus, newDiagnosisDate, newARTStatus, newStartedART, newCountryISO);
        System.out.println(result);
    }

    // Calculates the estimated lifespan of the patient based on HIV status and ART
    // treatment
    public Double calculateLifespan() {
        final Float factor = 0.9f; // lifespan reduction factor due to delayed ART
        final int maxNotOnART = 5; // maximum expected lifespan without ART (in years)

        // Retrieve patient information
        String uuid = Main.callBashScript("user-manager.sh", "get_uuid", this.getEmail());
        String patientInformation = Main.callBashScript("user-manager.sh", "get_patient_info", uuid);
        String[] patientInformationParts = patientInformation.split(":");

        // Set patient attributes from retrieved information
        setDateOfBirth(Main.parseDate(patientInformationParts[0]));
        setHasHIV(patientInformationParts[1]);
        setDiagnosisDate(Main.parseDate(patientInformationParts[2]));
        setIsOnART(patientInformationParts[3]);
        setStartedART(Main.parseDate(patientInformationParts[4]));
        setCountryISO(patientInformationParts[5]);

        // Get country-specific life expectancy and calculate default lifespan
        Float countryLifeExpectancy = Float
                .parseFloat(Main.callBashScript("user-manager.sh", "get_country_lifespan", getCountryISO()));
        Float defaultLifeSpan = countryLifeExpectancy - Main.calculateDateDifference(getDateOfBirth());

        // Calculate lifespan based on HIV status and ART treatment
        if (getHasHIV().equals("yes")) {
            Double calculatedLifespan = null;
            if (getIsOnART().equals("yes")) {
                defaultLifeSpan = (float) Main.calculateDateDifference(getDateOfBirth(), getDiagnosisDate());
                int delayBeforeART = Main.calculateDateDifference(getDiagnosisDate(), getStartedART());
                calculatedLifespan = defaultLifeSpan * Math.pow(factor, delayBeforeART);
            } else {
                calculatedLifespan = (double) (maxNotOnART - Main.calculateDateDifference(getDiagnosisDate()));
            }
            return calculatedLifespan;
        } else {
            return (double) defaultLifeSpan;
        }
    }

    // Displays the estimated lifespan of the patient
    public void viewLifespan() {
        Double lifespan = this.calculateLifespan();
        System.out.println("______________________________\nYour Estimated Lifespan is " + lifespan
                + " years\n______________________________");
    }
}