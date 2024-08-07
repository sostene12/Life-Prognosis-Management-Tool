import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Main {
    private static User currentUser = null;

    public static void main(String[] args) {

        // Initialize user store with admin user if it doesn't exist
        callBashScript("user-manager.sh", "initialize_user_store");

        while (true) {
            if (currentUser == null) {
                System.out.println("______________________________\nLife Prognosis Management Tool \nBy BISOKE TEAM 3\n______________________________");
                System.out.println("1. Login");
                System.out.println("2. Complete Registration");
                System.out.println("3. Exit");
                System.out.print("___________________________\nChoose an option: ");
                String option = Main.getUserInput();
                System.out.println("_______________________");

                switch (option) {
                    case "1":
                        currentUser = loginUser();
                        break;
                    case "2":
                        Patient.completeRegistration();
                        break;
                    case "3":
                        System.exit(0);
                    default:
                        System.out.println("Invalid option");
                }
            } else {
                if (currentUser.getUserRole() == UserRoles.ADMIN) {
                    adminMenu((Admin) currentUser);
                } else {
                    patientMenu((Patient) currentUser);
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
        String userData = User.login(email, password);

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

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("______________________________\nLife Prognosis Management Tool \nBy BISOKE TEAM 3\n______________________________");
            System.out.println("Hello, "+admin.getFirstName());
            System.out.println("1. Initiate Patient Registration");
            System.out.println("2. Export User Data");
            System.out.println("3. Export Analytics");
            System.out.println("4. Logout");
            System.out.print("_______________________\nChoose an option: ");
            String option = Main.getUserInput();
            System.out.println("_______________________");

            switch (option) {
                case "1":
                    ((Admin) currentUser).initializeRegistration();
                    break;
                case "2":
                    ((Admin) currentUser).exportUserData();
                    break;
                case "3":
                    ((Admin) currentUser).exportAnalytics();
                    break;
                case "4":
                    currentUser = null;
                    User.logout();
                    return; // Return to the main loop
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void patientMenu(Patient patient) {
        while (true) {
            System.out.println("______________________________\nLife Prognosis Management Tool \nBy BISOKE TEAM 3\n______________________________");
            System.out.println("Hello, "+patient.getFirstName());
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Lifespan");
            System.out.println("4. Logout");
            System.out.print("_______________________\nChoose an option: ");
            String option = Main.getUserInput();
            System.out.println("_______________________");

            switch (option) {
                case "1":
                    patient.viewProfile();
                    break;
                case "2":
                    patient.updateProfile();
                    break;
                case "3":
                    patient.viewLifespan();
                    break;
                case "4":
                    currentUser = null;
                    User.logout();
                    return; // Return to the main loop
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static String callBashScript(String scriptName, String option, String... args) {
        StringBuilder output = new StringBuilder();
        try {
            // Prepare command
            List<String> command = new ArrayList<>();
            command.add("./bash-scripts/" + scriptName);
            command.add(option);
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

    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parsedDate = dateFormat.parse(date);
            return parsedDate;
        } catch (ParseException e) {
            return null;
        }
    }

    public static int calculateDateDifference(Date initialDate){
        // Convert Date to LocalDate
        LocalDate initialLocalDate = initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Get today's date
        LocalDate today = LocalDate.now();

        // Calculate the period between the two dates
        Period period = Period.between(initialLocalDate, today);

        // Get the difference in years
        int diff = period.getYears();

        // Check if there are any remaining months or days to round up
        if (period.getMonths() > 0 || period.getDays() > 0) {
            diff++;
        }
        
        return diff;
    }

    public static int calculateDateDifference(Date initialDate, Date finalDate){
        // Convert Date to LocalDate
        LocalDate initialLocalDate = initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate finalLocalDate = initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculate the period between the two dates
        Period period = Period.between(initialLocalDate, finalLocalDate);

        // Get the difference in years
        int diff = period.getYears();

        // Check if there are any remaining months or days to round up
        if (period.getMonths() > 0 || period.getDays() > 0) {
            diff++;
        }
        
        return diff;
    }
}
