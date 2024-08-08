public abstract class User {
    // User attributes
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRoles userRole;

    // Constructor for User class, initializes user attributes
    public User(String firstName, String lastName, String email, String password, UserRoles userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    // Getter for first name
    public String getFirstName() {
        return firstName;
    }

    // Getter for last name
    public String getLastName() {
        return lastName;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for firstName
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Setter for lastName
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Setter for userRole
    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Getter for user role
    public UserRoles getUserRole() {
        return userRole;
    }

    // Static method for user login, calls a bash script to authenticate
    public static String login(String email, String password) {
        return Main.callBashScript("user-manager.sh", "login_user", email, password);
    }

    // Static method for logging out
    public static void logout() {
        System.out.println("Logged out.");
    }

    // Abstract method to be implemented by subclasses for viewing profile
    public abstract void viewProfile();
}