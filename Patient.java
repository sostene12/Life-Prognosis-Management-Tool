import java.util.Date;

public class Patient extends User {
    private Date dateOfBirth;
    private boolean hasHIV;
    private Date diagnosisDate;
    private boolean isOnART;
    private Date startedART;
    private String countryISO;

    public Patient(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, UserRoles.PATIENT);
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

        String diagnosisDate = "null";
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
        return this; // Patient profile
    }

    public void updateProfile(){
        return;
    }

    public void viewLifespan(){
        return;
    }
}