public class Admin extends User {

    // Constructor for Admin class, calling the parent User constructor
    // with Admin role
    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, UserRoles.ADMIN);
    }

    // Initializes registration
    public void initializeRegistration() {
        System.out.print("Enter patient's email: ");
        // Get user input for email
        String email = Main.getUserInput();
        // Generate a unique identifier
        String uuid = Main.generateUUID();
        // Call bash script to register the patient
        String result = Main.callBashScript("user-manager.sh", "register_patient", email, uuid);
        // Print the result of the registration
        System.out.println(result);
    }

    // Displays the profile information of the admin
    public void viewProfile() {
        // Print profile header
        System.out.println("My Profile\n______________________________\n");
        // Print admin's first name
        System.out.println("Firstname: " + getFirstName());
        // Print admin's last name
        System.out.println("Lastname: " + getLastName());
        // Print admin's email
        System.out.println("Email: " + getEmail());
        // Print profile footer
        System.out.println("_______________________");
    }

    // Exports user data by calling a bash script
    public void exportUserData() {
        // Call bash script to export user data
        String result = Main.callBashScript("user-manager.sh", "export_user_data");
        // Print the result of the export
        Main.clearScreen();
        System.out.println(Main.GREEN+"\u2714 "+result+""+Main.RESET);
    }

    // Exports analytics data by calling a bash script
    public void exportAnalytics() {
        // Call bash script to export analytics data
        String result = Main.callBashScript("user-manager.sh", "export_analytics");
        // Print the result of the export
        Main.clearScreen();
        System.out.println(Main.GREEN+"\u2714 "+result+""+Main.RESET);
    }
}
