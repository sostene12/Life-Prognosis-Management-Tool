import java.util.Date;

public class Patient implements User {
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Should be hashed
    private UserRoles userRole;
    private Date dateOfBirth;
    private boolean hasHIV;
    private Date diagnosisDate;
    private boolean isOnART;
    private Date startedART;
    private String countryISO;

    public Patient(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password; // Should be hashed
        this.userRole = UserRoles.PATIENT;
    }

    // Implement User interface methods
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRoles getUserRole() { return userRole; }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("Patient logged out.");
    }

    public User viewProfile() {
        return this; // Patient profile
    }

    public static void completeRegistration() {
        System.out.print("Enter UUID: ");
        String uuid = Main.getUserInput();

        int uuidCheckResult = Integer.parseInt(Main.callBashScript("check_uuid.sh", uuid));

        if (uuidCheckResult == 0) {
            System.out.println("Error: UUID does not exist.");
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


        String result = Main.callBashScript("complete_registration.sh", uuid, firstName, lastName, dob, hasHIV ? "yes" : "no", diagnosisDate, isOnART ? "yes" : "no", startedART, countryISO, password);
        System.out.println(result);
    }
}