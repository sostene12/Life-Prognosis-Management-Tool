import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Console;

public class Main {
    private static User currentUser = null;
    static final String RESET = "\033[0m";
    static final String RED = "\033[0;31m";
    static final String GREEN = "\033[0;32m";
    public static void main(String[] args) {
        Main.clearScreen();
        // Initialize user store with admin user if it doesn't exist
        callBashScript("user-manager.sh", "initialize_user_store");

        // Main loop for the application
        while (true) {
            if (currentUser == null) {
                // Display main menu options
                // showLogo();
                // System.out.println();
                System.out.println("______________________________\nLife Prognosis Management Tool \nBy BISOKE TEAM 3\n"+Main.GREEN+">>HOME"+Main.RESET+"\n______________________________");
                System.out.println("1. Login");
                System.out.println("2. Complete Registration");
                System.out.println("3. Exit");
                System.out.print("_______________________\n"+Main.GREEN+"Choose an option: "+Main.RESET);
                String option = Main.getUserInput();
                System.out.println("_______________________");

                // Process user input
                switch (option) {
                    case "1":
                        currentUser = loginUser();
                        break;
                    case "2":
                        Patient.completeRegistration();
                        break;
                    case "3":
                        System.exit(0); // Exit the application
                    default:
                        System.out.println(Main.RED+"\u274C Invalid option"+Main.RESET);
                }
            } else {
                // Redirect to appropriate menu based on user role
                if (currentUser.getUserRole() == UserRoles.ADMIN) {
                    adminMenu((Admin) currentUser);
                } else {
                    patientMenu((Patient) currentUser);
                }
            }
        }
    }

    private static User loginUser() {
        System.out.println("\n________ Log in ________");
        Console console = System.console();
        System.out.print("Enter your email: ");
        String email = Main.getUserInput();
        System.out.print("Enter your password: ");
        char[] passwordArray = console.readPassword();
        String password = new String(passwordArray);

        // Call Bash script to authenticate user
        String userData = User.login(email, password);

        if (!userData.equals("null")) {
            // Parse user data returned from the script
            String[] parts = userData.split(":");
            String userRole = parts[0];
            String firstName = parts[1];
            String lastName = parts[2];

            
            // Create and return user object based on role
            if (userRole.equals("Admin")) {
                Main.clearScreen();
                return new Admin(firstName, lastName, email, password);
            } else {
                Main.clearScreen();
                return new Patient(firstName, lastName, email, password);
            }
        } else {
            Main.clearScreen();
            System.out.println(Main.RED+"\u274C Incorrect email or password."+Main.RESET);
            return null;
        }
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            // Display admin menu options
            System.out.println("______________________________\nLife Prognosis Management Tool \nBy BISOKE TEAM 3\n"+Main.GREEN+"ADMIN,"+admin.getFirstName()+""+Main.RESET+"\n______________________________");
            System.out.println("1. Initiate Patient Registration");
            System.out.println("2. Export User Data");
            System.out.println("3. Export Analytics");
            System.out.println("4. Logout");
            System.out.print("_______________________\n"+Main.GREEN+"Choose an option: "+Main.RESET);
            String option = Main.getUserInput();
            System.out.println("_______________________");

            // Process admin menu input
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
                {
                    Main.clearScreen();
                    System.out.println(Main.RED+"\u274C Invalid option"+Main.RESET);
                }
            }
        }
    }

    public static void patientMenu(Patient patient) {
        while (true) {
            // Display patient menu options
            System.out.println("______________________________\nLife Prognosis Management Tool \nBy BISOKE TEAM 3\n"+Main.GREEN+"PATIENT,"+patient.getFirstName()+""+Main.RESET+"\n______________________________");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Lifespan");
            System.out.println("4. Logout");
            System.out.print("_______________________\n"+Main.GREEN+"Choose an option: "+Main.RESET);
            String option = Main.getUserInput();
            System.out.println("_______________________");

            // Process patient menu input
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
                System.out.println(Main.RED+"\u274C Invalid option"+Main.RESET);
            }
        }
    }

    public static String callBashScript(String scriptName, String option, String... args) {
        StringBuilder output = new StringBuilder();
        try {
            // Prepare command to execute the Bash script
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

            // Read output from the script
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

    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString(); // Generate a unique identifier
    }

    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Parse the date string into a Date object
            Date parsedDate = dateFormat.parse(date);
            return parsedDate;
        } catch (ParseException e) {
            return null; // Return null if parsing fails
        }
    }

    public static int calculateDateDifference(Date initialDate) {
        // Convert Date to LocalDate
        LocalDate initialLocalDate = initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Get today's date
        LocalDate today = LocalDate.now();

        // Calculate the period between the years
        int diff = today.getYear() - initialLocalDate.getYear();
        return diff;
    }

    public static int calculateDateDifference(Date initialDate, Date finalDate) {
        // Convert Date to LocalDate
        LocalDate initialLocalDate = initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate finalLocalDate = finalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculate the period between the years
        int diff = finalLocalDate.getYear() - initialLocalDate.getYear();
        return diff;
    }

    // function to show system logo
    public static void showLogo() {
        String teamLogo = "\n" +
                "  ____    _____    _____    ____    _  __  ______     ____  \n" +
                " |  _ \\  |_   _|  / ____|  / __ \\  | |/ / |  ____|   |___ \\ \n" +
                " | |_) |   | |   | (___   | |  | | | ' /  | |__        __) |\n" +
                " |  _ <    | |    \\___ \\  | |  | | |  <   |  __|      |__ < \n" +
                " | |_) |  _| |_   ____) | | |__| | | . \\  | |____     ___) |\n" +
                " |____/  |_____| |_____/   \\____/  |_|\\_\\ |______|   |____/ \n" +
                "                                                            \n" +
                "                                                            ";
        System.out.println(teamLogo);
    }

    // function to clear screen
    public static void clearScreen() {  
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error clearing screen: " + e.getMessage());
        }
    } 

    // function to validate email
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // function to validate names
    public static boolean isValidName(String nameString) {
        return nameString != null && !nameString.trim().isEmpty() && nameString.matches("[a-zA-Z]+");
    }

    // function to validate date of birth
    public static boolean isValidDateOfBirth(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            return false;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate parsedDate = LocalDate.parse(dob, dateFormatter);
            LocalDate today = LocalDate.now();
            LocalDate hundredYearsAgo = today.minusYears(100);

            // The date must be within the last 100 years and not in the future
            return !parsedDate.isAfter(today) && !parsedDate.isBefore(hundredYearsAgo);
        } catch (DateTimeParseException e) {
            return false; // Invalid format or invalid date
        }
    }

    // function to validate yes or no inputs
    public static boolean isValidInput(String hivStatus) {
        return "yes".equalsIgnoreCase(hivStatus) || "no".equalsIgnoreCase(hivStatus);
    }
    
    // function to validate diagnosis date
    public static boolean isValidDiagnosisDate(String diagnosisDate, String dob) {
        LocalDate dobDate = LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return isValidDate(diagnosisDate, dobDate, LocalDate.now());
    }
    
    // date comparator
    private static boolean isValidDate(String date, LocalDate minDate, LocalDate maxDate) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate parsedDate = LocalDate.parse(date, dateFormatter);

            if (minDate != null && parsedDate.isBefore(minDate)) {
                return false;
            }

            if (maxDate != null && parsedDate.isAfter(maxDate)) {
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // function to validate date order
    public static boolean isValidDateOrder(String startedART, String diagnosisDate) {
        LocalDate diagnosisDDate = LocalDate.parse(diagnosisDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return isValidDate(startedART, diagnosisDDate, LocalDate.now());
    }

    // function to validate country's ISO code
    public static boolean isValidISOCode(String countryISO) {
        String ISOCODE = Main.callBashScript("validation-functions.sh", "validateISO", countryISO);
        return countryISO != null && countryISO.trim().matches("[A-Z]{2,3}") && countryISO.equals(ISOCODE);
    }

    // function to validate password
    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty();
    }
}