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

    public void completeRegistration() {
        System.out.print("Enter UUID: ");
        String uuid = getUserInput();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = getUserInput();
        System.out.print("Are you HIV positive? (yes/no): ");
        String hivStatus = getUserInput();
        boolean hasHIV = hivStatus.equalsIgnoreCase("yes");

        if (hasHIV) {
            System.out.print("Enter diagnosis date (YYYY-MM-DD): ");
            String diagnosisDate = getUserInput();
            System.out.print("Are you on ART drugs? (yes/no): ");
            String artStatus = getUserInput();
            boolean isOnART = artStatus.equalsIgnoreCase("yes");
            if (isOnART) {
                System.out.print("Enter the date you started ART (YYYY-MM-DD): ");
                String startedART = getUserInput();
            }
        }

        System.out.print("Enter country ISO code: ");
        String countryISO = getUserInput();
        System.out.print("Enter password: ");
        String password = getUserInput();

        // Hash the password (for demonstration, not secure)
        String hashedPassword = hashPassword(password);

        //callBashScript("complete_registration.sh", uuid, dob, hasHIV ? "yes" : "no", diagnosisDate, isOnART ? "yes" : "no", startedART, countryISO, hashedPassword);
    }

    private String getUserInput() {
        // Implement logic to get user input
        return null;
    }

    private void callBashScript(String scriptName, String... args) {
        // Implement logic to call Bash script and handle output
    }

    private String hashPassword(String password) {
        // Implement password hashing logic
        return null;
    }
}
