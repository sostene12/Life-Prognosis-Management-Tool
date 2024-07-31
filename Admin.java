public class Admin implements User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRoles userRole;

    public Admin(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password; // Should be hashed
        this.userRole = UserRoles.ADMIN;
    }

    // Implement User interface methods
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRoles getUserRole() { return userRole; }

    public boolean login(String email, String password) {
        // Implement login logic
        return this.email.equals(email) && this.password.equals(password);
    }

    public void logout() {
        // Implement logout logic
    }

    public User viewProfile() {
        return this; // Admin profile
    }

    public void initializeRegistration(String email) {
        // Logic to initialize patient registration
    }

    public void exportUserData() {
        // Logic to export user data
    }

    public void exportAnalytics() {
        // Logic to export analytics
    }
}