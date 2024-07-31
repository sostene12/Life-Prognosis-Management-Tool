public class Admin implements User {
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Should be hashed
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
        // Password should be hashed before comparison
        return this.email.equals(email) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("Admin logged out.");
    }

    public User viewProfile() {
        return this; // Admin profile
    }

    public void initializeRegistration() {
        System.out.print("Enter patient's email: ");
        String email = Main.getUserInput();
        String uuid = Main.generateUUID();
        String result = Main.callBashScript("user-manager.sh", "register_patient", email, uuid);
        System.out.println(result);
    }
}
