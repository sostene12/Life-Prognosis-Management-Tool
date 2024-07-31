import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static User currentUser = null;

    public static void main(String[] args) {

        // Initialize user store with admin user if it doesn't exist
        callBashScript("initialize_user_store.sh");

        while (true) {
            if (currentUser == null) {
                System.out.println("1. Login User");
                System.out.println("2. Complete Registration");
                System.out.println("3. Exit");
                System.out.print("___________________________\nChoose an option: ");
                int option = Integer.parseInt(Main.getUserInput());
                System.out.println("_______________________");

                switch (option) {
                    case 1:
                        currentUser = loginUser();
                        break;
                    case 2:
                        Patient.completeRegistration();
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Invalid option");
                }
            } else {
                if (currentUser.getUserRole() == UserRoles.ADMIN) {
                    adminMenu();
                } else {
                    Main.patientMenu((Patient) currentUser);
                }
            }
        }
    }

    private static User loginUser() {
        System.out.print("Enter your email: ");
        String email = Main.getUserInput();
        System.out.print("Enter your password: ");
        String password = Main.getUserInput();

        // Call Bash script to authenticate user
        String userData = callBashScript("login_user.sh", email, password);

        if (!userData.equals("null")){
            String[] parts = userData.split(":");

            String userRole = parts[0];
            String firstName = parts[1];
            String lastName = parts[2];

            if (userRole.equals("Admin")) {
                return new Admin(firstName, lastName, email, password);
            } else {
                return new Patient(firstName, lastName, email, password);
            }
        } else {
            System.out.println("Incorrect email or password.");
            return null;
        }
    }

    private static void completeRegistration() {
        System.out.print("Enter UUID: ");
        String uuid = Main.getUserInput();
        callBashScript("complete_registration.sh", uuid);
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Initiate Patient Registration");
            System.out.println("2. Export User Data");
            System.out.println("3. Export Analytics");
            System.out.println("4. Logout");
            System.out.print("_______________________\nChoose an option: ");
            int option = Integer.parseInt(Main.getUserInput());
            System.out.println("_______________________");

            switch (option) {
                case 1:
                    ((Admin) currentUser).initializeRegistration();
                    break;
//                case 2:
//                    ((Admin) currentUser).exportUserData();
//                    break;
//                case 3:
//                    ((Admin) currentUser).exportAnalytics();
//                    break;
                case 4:
                    currentUser = null;
                    System.out.println("Logged out.");
                    return; // Return to the main loop
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void patientMenu(Patient patient) {
        while (true) {
            System.out.println("Patient Menu:");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Lifespan");
            System.out.println("4. Logout");
            System.out.print("_______________________\nChoose an option: ");
            int option = Integer.parseInt(Main.getUserInput());
            System.out.println("_______________________");

            switch (option) {
                case 1:
                    patient.viewProfile();
                    break;
//                case 2:
//                    patient.updateProfile();
//                    break;
//                case 3:
//                    patient.viewLifespan();
//                    break;
                case 4:
                    currentUser = null;
                    System.out.println("Logged out.");
                    return; // Return to the main loop
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static String callBashScript(String scriptName, String... args) {
        StringBuilder output = new StringBuilder();
        try {
            // Prepare command
            List<String> command = new ArrayList<>();
            command.add("./" + scriptName);
            for (String arg : args) {
                command.add(arg);
            }

            // Execute command
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error calling Bash script: " + e.getMessage());
        }
        return output.toString().trim();
    }

    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine(); // Read input from the user
    }

    public static  String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

}
