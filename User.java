public abstract class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRoles userRole;

    public User(String firstName, String lastName, String email, String password, UserRoles userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRoles getUserRole() {
        return userRole;
    }

    public static String login(String email, String password){
        return Main.callBashScript("user-manager.sh", "login_user", email, password);
    }

    public static void logout(){
        System.out.println("Admin logged out.");
    }

    public abstract User viewProfile();
}
