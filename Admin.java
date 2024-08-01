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
        String result = Main.callBashScript("user-manager.sh", "export_user_data");
        System.out.println(result);
        return;
    }

    public void exportAnalytics() {
        String result = Main.callBashScript("user-manager.sh", "export_analytics");
        System.out.println(result);
        return;
    }
}
