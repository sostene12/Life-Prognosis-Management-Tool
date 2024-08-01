public class Admin extends User {
    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, UserRoles.ADMIN);
    }

    public void initializeRegistration() {
        System.out.print("Enter patient's email: ");
        String email = Main.getUserInput();
        String uuid = Main.generateUUID();
        String result = Main.callBashScript("user-manager.sh", "register_patient", email, uuid);
        System.out.println(result);
    }

    public User viewProfile() {
        return this; // Admin profile
    }

    public void exportUserData(){
        return;
    }

    public void exportAnalytics() {
        return;
    }
}
