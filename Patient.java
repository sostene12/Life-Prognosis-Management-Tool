import java.util.Date;

public class Patient implements User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRoles userRole;
    private Date dateOfBirth;
    private boolean hasHIV;
    private Date diagnosisDate;
    private boolean isOnART;
    private Date startedART;
    private String countryISO;

    public Patient(String firstName, String lastName, String email, String password, Date dateOfBirth, boolean hasHIV, Date diagnosisDate, boolean isOnART, Date startedART, String countryISO) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password; // Should be hashed
        this.userRole = UserRoles.PATIENT;
        this.dateOfBirth = dateOfBirth;
        this.hasHIV = hasHIV;
        this.diagnosisDate = diagnosisDate;
        this.isOnART = isOnART;
        this.startedART = startedART;
        this.countryISO = countryISO;
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
        // Implement logout logic
    }

    public User viewProfile() {
        return this; // Patient profile
    }

    public void updateProfile() {
        // Logic to update patient profile
    }

    public int viewLifeSpan() {
        // Logic to calculate and return lifespan
        return 0; // Placeholder return
    }
}
