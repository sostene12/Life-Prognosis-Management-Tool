public class Admin extends User {

    // Constructor for Admin class, calling the parent User constructor
    // with Admin role
    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, UserRoles.ADMIN);
    }

    // Initializes registration
    public void initializeRegistration() {
        System.out.print("Enter patient's email: ");
        String email = Main.getUserInput();
    
        // Validate email
        while (!Main.isValidEmail(email)) {
            System.out.println(Main.RED+"Invalid email format. "+Main.RESET+"\nPlease enter a valid email: ");
            email = Main.getUserInput();
        }
    
        // Generate a unique identifier
        String uuid = Main.generateUUID();
    
        // Call bash script to register the patient
        String result = Main.callBashScript("user-manager.sh", "register_patient", email, uuid);
    
        // Print the result of the registration
        Main.clearScreen();
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
        System.out.println(Main.GREEN+"\nExporting........"+Main.RESET);
        // Call bash script to export user data
        String result = Main.callBashScript("user-manager.sh", "export_user_data");
        // Print the result of the export
        Main.clearScreen();
        System.out.println(Main.GREEN+"\u2714 "+result+""+Main.RESET);
    }

    // Exports analytics data by calling a bash script
    public void exportAnalytics() {
        System.out.println(Main.GREEN+"\nExporting........"+Main.RESET);
        // Call bash script to export analytics data
        String result = Main.callBashScript("analytics-handler.sh", "");
        // Print the result of the export
        Main.clearScreen();
        System.out.println(Main.GREEN+"\u2714 "+result+""+Main.RESET);
    }
}
