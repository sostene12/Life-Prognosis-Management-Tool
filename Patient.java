import java.io.Console;
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
        Console console = System.console();
        System.out.println("\n_______________________\nCompleting Registration\n_______________________");
        // Prompt user for UUID and email
        System.out.print("Enter UUID: ");
        String uuid = Main.getUserInput();
        System.out.print("Enter Your Email: ");
        String email = Main.getUserInput();

        // Check UUID validity and registration status
        String uuidCheckResult = Main.callBashScript("user-manager.sh", "check_uuid", uuid, email);
        
        if (uuidCheckResult.equals("1")) {
            Main.clearScreen();
            System.out.println(Main.RED+"\u2139 Info: Registration already done."+Main.RESET);
            return; 
        } else if (uuidCheckResult.equals("2")) {
            Main.clearScreen();
            System.out.println(Main.RED+"\u2139 Info: Authentication failed."+Main.RESET);
            return;
        }

        // Prompt user for remaining registration details
        System.out.print("Enter Your firstname: ");
        String firstName = Main.getUserInput();

        // Validate firstname
        while (!Main.isValidName(firstName)) {
            System.out.println(Main.RED+"Invalid firstname format. "+Main.RESET+"\nPlease enter a valid firstname: ");
            firstName = Main.getUserInput();
        }

        System.out.print("Enter Your lastname: ");
        String lastName = Main.getUserInput();

        // Validate lastName
        while (!Main.isValidName(lastName)) {
            System.out.println(Main.RED+"Invalid lastname format. "+Main.RESET+"\nPlease enter a valid lastname: ");
            lastName = Main.getUserInput();
        }

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = Main.getUserInput();

        while (!Main.isValidDateOfBirth(dob)) {
            System.out.println(Main.RED + "Invalid date or date out of valid range." + Main.RESET + 
                               "\nPlease enter a real valid date of birth (YYYY-MM-DD): ");
            dob = Main.getUserInput();
        }

        System.out.print("Are you HIV positive? (yes/no): ");
        String hivStatus = Main.getUserInput();

        while (!Main.isValidInput(hivStatus)) {
            System.out.println(Main.RED + "Invalid input. Please enter 'yes' or 'no'." + Main.RESET);
            System.out.print("Are you HIV positive? (yes/no): ");
            hivStatus = Main.getUserInput();
        }

        boolean hasHIV = hivStatus.equalsIgnoreCase("yes");

        String diagnosisDate = "null";
        boolean isOnART = false;
        String startedART = "null";

        // If HIV positive, ask for diagnosis date and ART status
        if (hasHIV) {
            System.out.print("Enter diagnosis date (YYYY-MM-DD): ");
            diagnosisDate = Main.getUserInput();

            // Validate the diagnosis date
            while (!Main.isValidDiagnosisDate(diagnosisDate, dob)) {
                System.out.println(Main.RED + "Invalid diagnosis. It must be a valid date greater than the date of birth." + Main.RESET);
                System.out.print("Enter diagnosis date (YYYY-MM-DD): ");
                diagnosisDate = Main.getUserInput();
            }

            System.out.print("Are you on ART drugs? (yes/no): ");
            String artStatus = Main.getUserInput();

            while (!Main.isValidInput(artStatus)) {
                System.out.println(Main.RED + "Invalid input. Please enter 'yes' or 'no'." + Main.RESET);
                System.out.print("Are you on ART drugs? (yes/no): ");
                artStatus = Main.getUserInput();
            }

            isOnART = artStatus.equalsIgnoreCase("yes");
            if (isOnART) {
                System.out.print("Enter the date you started ART (YYYY-MM-DD): ");
                startedART = Main.getUserInput();

                // Validate the date of starting ART
                while (!Main.isValidDateOrder(startedART, diagnosisDate)) {
                    System.out.println(Main.RED + "Invalid date. It must be a valid date greater than the date of diagnosis." + Main.RESET);
                    System.out.print("Enter the date you started ART (YYYY-MM-DD): ");
                    startedART = Main.getUserInput();
                }
            }
        }

        System.out.print("Enter country ISO code: ");
        String countryISO = Main.getUserInput();

        while (!Main.isValidISOCode(countryISO)) {
            System.out.println(Main.RED + "Invalid ISO code. It cannot be empty and must be 2 or 3 uppercase letters." + Main.RESET);
            System.out.print("Enter country ISO code: ");
            countryISO = Main.getUserInput();
        }

        // Prompt for password
        System.out.print("Enter password: ");
        char[] passwordArray = System.console().readPassword();
        String password = new String(passwordArray);

        while (!Main.isValidPassword(password)) {
            System.out.println(Main.RED + "Invalid password. It cannot be empty." + Main.RESET);
            System.out.print("Enter password: ");
            passwordArray = System.console().readPassword();
            password = new String(passwordArray);
        }

        // Register patient via bash script
        String result = Main.callBashScript("user-manager.sh", "complete_registration", uuid, firstName, lastName, dob, hasHIV ? "yes" : "no", diagnosisDate, isOnART ? "yes" : "no", startedART, countryISO, password);
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
        if (patientInformationParts[0] != null && !patientInformationParts[0].isEmpty()) {
            setDateOfBirth(Main.parseDate(patientInformationParts[0]));
        }
        
        if (patientInformationParts[1] != null && !patientInformationParts[1].isEmpty()) {
            setHasHIV(patientInformationParts[1]);
        }
        
        if (patientInformationParts[2] != null && !patientInformationParts[2].isEmpty()) {
            setDiagnosisDate(Main.parseDate(patientInformationParts[2]));
        }
        
        if (patientInformationParts[3] != null && !patientInformationParts[3].isEmpty()) {
            setIsOnART(patientInformationParts[3]);
        }
        
        if (patientInformationParts[4] != null && !patientInformationParts[4].isEmpty()) {
            setStartedART(Main.parseDate(patientInformationParts[4]));
        }
        
        if (patientInformationParts[5] != null && !patientInformationParts[5].isEmpty()) {
            setCountryISO(patientInformationParts[5]);
        }        
        
        Main.clearScreen();
        // Display patient profile information
        System.out.println("\n_______________________\nMy Profile\n_______________________\n");
        System.out.println("Firstname: " +Main.GREEN+ getFirstName()+Main.RESET);
        System.out.println("Lastname: " +Main.GREEN+ getLastName()+Main.RESET);
        System.out.println("Email: " +Main.GREEN+ getEmail()+Main.RESET);
        System.out.println("Date of birth: " +Main.GREEN+ getDateOfBirth()+Main.RESET);
        System.out.println("HIV status: " +Main.GREEN+ getHasHIV()+Main.RESET);
        System.out.println("Diagnosis Date: " +Main.GREEN+ getDiagnosisDate()+Main.RESET);
        System.out.println("ART status: " +Main.GREEN+ getIsOnART()+Main.RESET);
        System.out.println("Date - ART: " +Main.GREEN+ getStartedART()+Main.RESET);
        System.out.println("Country: " +Main.GREEN+ getCountryISO()+Main.RESET);
        System.out.println("Survival rate: " +Main.GREEN+ this.calculateLifespan()+Main.RESET+" years");
        System.out.println("_______________________");
    }

    // Method to update profile, not implemented yet
    public void updateProfile() {
        Console console = System.console();
        // Retrieve patient UUID using email
        String uuid = Main.callBashScript("user-manager.sh", "get_uuid", getEmail());

        // Get patient information from the system
        String patientInformation = Main.callBashScript("user-manager.sh", "get_patient_info", uuid);
        String[] patientInformationParts = patientInformation.split(":");

        System.out.println(Main.GREEN+"Current information (press Enter to keep current value):"+Main.RESET);

        System.out.print("Enter your new first name: ");
        String newFirstName = Main.getUserInput();
        newFirstName = newFirstName.isEmpty() ? getFirstName() : newFirstName;

        // Validate firstname
        while (!Main.isValidName(newFirstName)) {
            System.out.println(Main.RED+"Invalid firstname format. "+Main.RESET+"\nPlease enter a valid firstname: ");
            newFirstName = Main.getUserInput();
            newFirstName = newFirstName.isEmpty() ? getFirstName() : newFirstName;
        }

        System.out.print("Enter your new last name: ");
        String newLastName = Main.getUserInput();
        newLastName = newLastName.isEmpty() ? getLastName() : newLastName;

        // Validate lastName
        while (!Main.isValidName(newLastName)) {
            System.out.println(Main.RED+"Invalid lastname format. "+Main.RESET+"\nPlease enter a valid lastname: ");
            newLastName = Main.getUserInput();
            newLastName = newLastName.isEmpty() ? getLastName() : newLastName;
        }

        System.out.print("Enter your new DOB (YYYY-MM-DD): ");
        String newDOB = Main.getUserInput();
        newDOB = newDOB.isEmpty() ? patientInformationParts[0] : newDOB;

        while (!Main.isValidDateOfBirth(newDOB)) {
            System.out.println(Main.RED + "Invalid date or date out of valid range." + Main.RESET + 
                               "\nPlease enter a real valid date of birth (YYYY-MM-DD): ");
                               newDOB = Main.getUserInput();
            newDOB = newDOB.isEmpty() ? patientInformationParts[0] : newDOB;
        }

        System.out.print("Are you HIV positive? (yes/no): ");
        String newHIVStatus = Main.getUserInput();
        newHIVStatus = newHIVStatus.isEmpty() ? patientInformationParts[1] : newHIVStatus;


        while (!Main.isValidInput(newHIVStatus)) {
            System.out.println(Main.RED + "Invalid input. Please enter 'yes' or 'no'." + Main.RESET);
            System.out.print("Are you HIV positive? (yes/no): ");
            newHIVStatus = Main.getUserInput();
            newHIVStatus = newHIVStatus.isEmpty() ? patientInformationParts[1] : newHIVStatus;
        }


        String newDiagnosisDate = "null", newARTStatus = "no", newStartedART = "null";

        if (newHIVStatus.equalsIgnoreCase("yes")) {
            System.out.print("Enter your new diagnosis date (YYYY-MM-DD): ");
            newDiagnosisDate = Main.getUserInput();
            newDiagnosisDate = newDiagnosisDate.isEmpty() ? patientInformationParts[2] : newDiagnosisDate;

            // Validate the diagnosis date
            while (!Main.isValidDiagnosisDate(newDiagnosisDate, newDOB)) {
                System.out.println(Main.RED + "Invalid diagnosis. It must be a valid date greater than the date of birth." + Main.RESET);
                System.out.print("Enter diagnosis date (YYYY-MM-DD): ");
                newDiagnosisDate = Main.getUserInput();
                newDiagnosisDate = newDiagnosisDate.isEmpty() ? patientInformationParts[2] : newDiagnosisDate;
            }

            System.out.print("Are you on ART drugs? (yes/no): ");
            newARTStatus = Main.getUserInput();
            newARTStatus = newARTStatus.isEmpty() ? patientInformationParts[3] : newARTStatus;

            while (!Main.isValidInput(newARTStatus)) {
                System.out.println(Main.RED + "Invalid input. Please enter 'yes' or 'no'." + Main.RESET);
                System.out.print("Are you on ART drugs? (yes/no): ");
                newARTStatus = Main.getUserInput();
                newARTStatus = newARTStatus.isEmpty() ? patientInformationParts[3] : newARTStatus;
            }

            if (newARTStatus.equalsIgnoreCase("yes")) {
                System.out.print("Enter the date you started ART (YYYY-MM-DD): ");
                newStartedART = Main.getUserInput();
                newStartedART = newStartedART.isEmpty() ? patientInformationParts[4] : newStartedART;

                // Validate the date of starting ART
                while (!Main.isValidDateOrder(newStartedART, newDiagnosisDate)) {
                    System.out.println(Main.RED + "Invalid date. It must be a valid date greater than the date of diagnosis." + Main.RESET);
                    System.out.print("Enter the date you started ART (YYYY-MM-DD): ");
                    newStartedART = Main.getUserInput();
                }

            }
        }

        System.out.print("Enter your new country ISO code: ");
        String newCountryISO = Main.getUserInput();
        newCountryISO = newCountryISO.isEmpty() ? patientInformationParts[5] : newCountryISO;

        while (!Main.isValidISOCode(newCountryISO)) {
            System.out.println(Main.RED + "Invalid ISO code. It cannot be empty and must be 2 or 3 uppercase letters." + Main.RESET);
            System.out.print("Enter country ISO code: ");
            newCountryISO = Main.getUserInput();
            newCountryISO = newCountryISO.isEmpty() ? patientInformationParts[5] : newCountryISO;
        }


        System.out.print("Enter new password:");

        char[] passwordArray = console.readPassword();
        String newPassword = new String(passwordArray);
        newPassword = newPassword.isEmpty() ? getPassword() : newPassword;

        while (!Main.isValidPassword(newPassword)) {
            System.out.println(Main.RED + "Invalid password. It cannot be empty." + Main.RESET);
            System.out.print("Enter password: ");
            passwordArray = System.console().readPassword();
            newPassword = new String(passwordArray);
            newPassword = newPassword.isEmpty() ? getPassword() : newPassword;
        }

        System.out.println();
        System.out.println("Updating your profile...\n ");
        System.out.println(this.getEmail()+"-"+ newFirstName+"-"+  newLastName+"-"+  newDOB+"-"+  newHIVStatus+"-"+  newDiagnosisDate+"-"+  newARTStatus+"-"+  newStartedART+"-"+  newCountryISO);

        String result = Main.callBashScript("user-manager.sh", "update_patient_profile", this.getEmail(), newFirstName, newLastName, newPassword, newDOB, newHIVStatus, newDiagnosisDate, newARTStatus, newStartedART, newCountryISO);
        
        if(result.equals("OK")){
            Main.clearScreen();
            System.out.println(Main.GREEN+"\u2714 Profile updated successfully"+Main.RESET);
        }else{
            System.out.println(Main.GREEN+"\u274C Failed to update profile"+Main.RESET);
        }


        this.setFirstName(newFirstName);
        this.setLastName(newLastName);
    }

    // Calculates the estimated lifespan of the patient based on HIV status and ART treatment
    public float calculateLifespan() {
        final float factor = 0.9f; // lifespan reduction factor due to delayed ART
        final int maxNotOnART = 5; // maximum expected lifespan without ART (in years)
        
        // Retrieve patient information
        String uuid = Main.callBashScript("user-manager.sh", "get_uuid", this.getEmail());
        String patientInformation = Main.callBashScript("user-manager.sh", "get_patient_info", uuid);
        String[] patientInformationParts = patientInformation.split(":");

        // Set patient attributes from retrieved information
        if (patientInformationParts[0] != null && !patientInformationParts[0].isEmpty()) {
            setDateOfBirth(Main.parseDate(patientInformationParts[0]));
        }
        
        if (patientInformationParts[1] != null && !patientInformationParts[1].isEmpty()) {
            setHasHIV(patientInformationParts[1]);
        }
        
        if (patientInformationParts[2] != null && !patientInformationParts[2].isEmpty()) {
            setDiagnosisDate(Main.parseDate(patientInformationParts[2]));
        }
        
        if (patientInformationParts[3] != null && !patientInformationParts[3].isEmpty()) {
            setIsOnART(patientInformationParts[3]);
        }
        
        if (patientInformationParts[4] != null && !patientInformationParts[4].isEmpty()) {
            setStartedART(Main.parseDate(patientInformationParts[4]));
        }
        
        if (patientInformationParts[5] != null && !patientInformationParts[5].isEmpty()) {
            setCountryISO(patientInformationParts[5]);
        } 

        // Get country-specific life expectancy and calculate default lifespan
        float countryLifeExpectancy = Float.parseFloat(Main.callBashScript("user-manager.sh", "get_country_lifespan", getCountryISO()));
        // declare the number of years that was remaining to reach the lifespan before testing HIV Positive
        float defaultLifeSpan;

        // Calculate lifespan based on HIV status and ART treatment
        if (getHasHIV().equals("yes")) {
            float calculatedLifespan;

            defaultLifeSpan = countryLifeExpectancy - Main.calculateDateDifference(getDateOfBirth(), getDiagnosisDate());
            
            if (getIsOnART().equals("yes")) {
                // calculating the number of years delayed before taking ART
                int delayBeforeART = Main.calculateDateDifference(getDiagnosisDate(), getStartedART());
                
                // calculating the number of years taking ART
                int yearsOnART = Main.calculateDateDifference(getStartedART());
                
                // calculate lifespan by applying the formula
                calculatedLifespan = (float) (defaultLifeSpan * factor * Math.pow(factor, delayBeforeART));

                // subtract years lived after testing HIV Positive
                calculatedLifespan -= yearsOnART;
            } else {
                int yearsAfterDiagnosis = Main.calculateDateDifference(getDiagnosisDate());
                if(defaultLifeSpan > maxNotOnART){
                    // There were more than 5 years to live
                    calculatedLifespan = maxNotOnART - yearsAfterDiagnosis;
                }else{
                    // There were less than 5 years to live
                    calculatedLifespan = defaultLifeSpan - yearsAfterDiagnosis;
                }
            }
            return calculatedLifespan>0?calculatedLifespan:0;
        } else {
            defaultLifeSpan = countryLifeExpectancy - Main.calculateDateDifference(getDateOfBirth());
            return defaultLifeSpan>0?defaultLifeSpan:0;
        }
    }

    // Displays the estimated lifespan of the patient
    public void viewLifespan() {
        Main.clearScreen();
        float lifespan = this.calculateLifespan();
        System.out.println("\n______________________________\n"+Main.GREEN+"Your Estimated Survival rate is " + lifespan + " years"+Main.RESET+"\n______________________________");
    }
}