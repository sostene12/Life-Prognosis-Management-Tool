import java.util.Date;
import java.math.*;

public class Patient extends User {
    private Date dateOfBirth;
    private String hasHIV;
    private Date diagnosisDate;
    private String isOnART;
    private Date startedART;
    private String countryISO;

    public Patient(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, UserRoles.PATIENT);
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHasHIV() {
        return hasHIV;
    }

    public void setHasHIV(String hasHIV) {
        this.hasHIV = hasHIV;
    }

    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public String getIsOnART() {
        return isOnART;
    }

    public void setIsOnART(String isOnART) {
        this.isOnART = isOnART;
    }

    public Date getStartedART() {
        return startedART;
    }

    public void setStartedART(Date startedART) {
        this.startedART = startedART;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public static void completeRegistration() {
        System.out.print("Enter UUID: ");
        String uuid = Main.getUserInput();

        System.out.print("Enter Your Email: ");
        String email = Main.getUserInput();

        String uuidCheckResult = Main.callBashScript("user-manager.sh", "check_uuid", uuid, email);
        if (uuidCheckResult.equals("1")) {
            System.out.println("Info: Registration already done.");
            return; 
        }
        else if (uuidCheckResult.equals("2")) {
            System.out.println("Info: Authentication failed.");
            return;
        }

        System.out.print("Enter Your firstname: ");
        String firstName = Main.getUserInput();
        System.out.print("Enter Your lastname: ");
        String lastName = Main.getUserInput();


        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = Main.getUserInput();
        System.out.print("Are you HIV positive? (yes/no): ");
        String hivStatus = Main.getUserInput();
        boolean hasHIV = hivStatus.equalsIgnoreCase("yes");

        String diagnosisDate =  "null";
        boolean isOnART = false;
        String startedART = "null";

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

        System.out.print("Enter country ISO code: ");
        String countryISO = Main.getUserInput();
        System.out.print("Enter password: ");
        String password = Main.getUserInput();

        String result = Main.callBashScript("user-manager.sh", "complete_registration",  uuid, firstName, lastName, dob, hasHIV ? "yes" : "no", diagnosisDate, isOnART ? "yes" : "no", startedART, countryISO, password);
        System.out.println(result);
    }

    public User viewProfile() {
        return this;
    }

    public void updateProfile(){
        return;
    }

    public Double calculateLifespan(){
        final Float factor = 0.9f;
        final int maxNotOnART = 5;
        String uuid = Main.callBashScript("user-manager.sh", "get_uuid", this.getEmail());

        String PatientInformation = Main.callBashScript("user-manager.sh", "get_patient_info", uuid);
        String[] PatientInformationParts = PatientInformation.split(":");

        setDateOfBirth(Main.parseDate(PatientInformationParts[0]));
        setHasHIV(PatientInformationParts[1]);
        setDiagnosisDate(Main.parseDate(PatientInformationParts[2]));
        setIsOnART(PatientInformationParts[3]);
        setStartedART(Main.parseDate(PatientInformationParts[4]));
        setCountryISO(PatientInformationParts[5]);

        Float countryLifeExpectancy = Float.parseFloat(Main.callBashScript("user-manager.sh", "get_country_lifespan", getCountryISO()));
        Float defaultLifeSpan = countryLifeExpectancy - Main.calculateDateDifference(getDateOfBirth());

        if(getHasHIV() == "yes"){
            Double calculatedLifespan = null;

            if(getIsOnART() == "yes"){
                defaultLifeSpan = (float) Main.calculateDateDifference(getDateOfBirth(), getDiagnosisDate());
                int delayBeforeART = Main.calculateDateDifference(getDiagnosisDate(), getStartedART());
                calculatedLifespan = defaultLifeSpan * Math.pow(factor, delayBeforeART);
            } else {
                calculatedLifespan = (double) (maxNotOnART - Main.calculateDateDifference(getDiagnosisDate()));
            }

            return calculatedLifespan;
        } else{
            return (double) defaultLifeSpan;
        }
    }

    public void viewLifespan(){
        Double lifespan = this.calculateLifespan();
        System.out.println("______________________________\nYour Estimated Lifespan is "+lifespan+" years\n______________________________");
    }
}