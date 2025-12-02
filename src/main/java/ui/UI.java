package ui;

import BITalino.*;
import POJOS.*;
import manageFiles.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Main User Interface class for the Patient Application.
 *
 * This class manages all interactions with the patient user through a console-based menu system.
 * It handles connection establishment, login, registration, report creation, signal acquisition from BITalino,
 * and communication with the server through a {@link Connection} object.
 *
 * The UI is stateful and remains active until the user explicitly exits.
 */
public class UI {

    /** Handles TCP communication with the server application. */
    private Connection connection;

    /** Utility class for creating and modifying CSV signal files. */
    private ManageFiles manageFiles = new ManageFiles();

    /** Utility class for capturing signals using the BITalino device. */
    private BItalinoCapture bitalinoCapture = new BItalinoCapture();

    /**
     * Entry point of the Patient Application.
     *
     * @throws IOException if an I/O error occurs during connection initialization
     */
    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        ui.startConnection();
    }

    /**
     * Establishes a TCP connection with the server application.
     *
     * The method repeatedly prompts the patient user to enter an IP address and port until a valid connection is successfully created.
     * Once connected, the UI identifies itself as a PATIENT application by sending a specific integer code.
     * If the server confirms the identity, the pre-login menu is started.
     *
     * @throws RuntimeException if the connection cannot be established
     */
    private void startConnection() {
        boolean connected = false;

        while (!connected) {
            System.out.println("""
            ╔══════════════════════════════════════════════╗
            ║         PATIENT APPLICATION - CONNECT        ║
            ║                                              ║
            ║   Please enter the server connection info    ║
            ╚══════════════════════════════════════════════╝
            """);
            String ipAddress = Utilities.readString("-> IP Address: ");
            int port = Utilities.readInteger("-> Port: ");
            System.out.println("-> Waiting server app to start... ");
            try {
                this.connection = new Connection(ipAddress, port);
                connected = true;
                System.out.print("""
                ╔══════════════════════════════════════════════╗
                ║             CONNECTION SUCCESS!              ║
                ╚══════════════════════════════════════════════╝
                """);

            } catch (Exception e) {
                System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║             CONNECTION FAILED!               ║
                ╚══════════════════════════════════════════════╝
                """);
                System.out.println("-> Could not connect to " + ipAddress + ":" + port);
                System.out.println("-> Please try it again!\n");
                System.out.println("----------------------------------------------");
            }
        }
        try {
            connection.getSendViaNetwork().sendInt(1);
            String message = connection.getReceiveViaNetwork().receiveString();

            if ("PATIENT".equals(message)) {
                this.preLoggedMenu();
            }
        } catch (IOException e) {
            System.out.println("-> Error in communication once it was connected! ");
            System.out.println("----------------------------------------------");
        }
    }

    /**
     * Displays the pre-login menu for the user, allowing: Patient registration, Patient login and Application exit.
     *
     * This menu loops indefinitely until the patient logs in or exits.
     * The option selected by the patient is forwarded to the server, and then the corresponding method is executed.
     *
     * @throws IOException if there is an error communicating with the server
     */
    private void preLoggedMenu() throws IOException {
        do {
            int option = 0;
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║    WELCOME TO PATIENT APPLICATION      ║
            ║                                        ║
            ║    1) Register                         ║
            ║    2) Log in                           ║
            ║    3) Exit                             ║
            ╚════════════════════════════════════════╝
            """);
            option = Utilities.readInteger("-> Select an option: ");
            switch (option){
                case 1:
                    connection.getSendViaNetwork().sendInt(option);
                    this.registerMenu();
                    break;
                case 2:
                    connection.getSendViaNetwork().sendInt(option);
                    this.loginMenu();
                    break;
                case 3:
                    connection.getSendViaNetwork().sendInt(option);
                    this.exitMenu();
                    break;
                default:
                    System.out.println("-> Please select a valid option! ");
                    System.out.println("----------------------------------------------");
                    break;
            }
        } while(true);
    }

    /**
     * Displays the registration menu for new patients.
     *
     * The user enters email, full name, date of birth, and password.
     * After server validation and doctor assignment, the patient is registered.
     *
     * @throws IOException if server communication fails
     */
    private void registerMenu() throws IOException {
        System.out.println("""
        ╔════════════════════════════════════════╗
        ║          PATIENT REGISTER MENU         ║
        ╚════════════════════════════════════════╝
        """);

        String email;
        boolean valid;
        do {
            email = Utilities.readString("-> Enter your email: ");
            valid = Utilities.checkEmail(email);

        }while(!valid);

        connection.getSendViaNetwork().sendStrings(email);
        String serverResponse = connection.getReceiveViaNetwork().receiveString();
        System.out.println(serverResponse);

        if (serverResponse.equals("EMAIL OK")) {
            System.out.println("-> Email accepted! ");
            System.out.println("----------------------------------------------");
            if (connection.getReceiveViaNetwork().receiveString().equals("DOCTOR ASSIGNED")) {
                String fullName;
                do {
                    fullName = Utilities.readString("-> Enter your full name: ");
                    if (fullName == null || fullName.trim().isEmpty()) {
                        System.out.println("-> Name cannot be empty! ");
                        System.out.println("----------------------------------------------");
                    }
                } while (fullName == null || fullName.trim().isEmpty());

                LocalDate dob = Utilities.readDate("-> Enter your DOB: ");

                String password;
                do {
                    password = Utilities.readString("-> Enter your password: ");
                    if (password == null || password.isEmpty()) {
                        System.out.println("-> Password cannot be empty! ");
                        System.out.println("----------------------------------------------");
                    }
                } while (password == null || password.isEmpty());

                Patient registeredPatient = new Patient(password, fullName, dob);
                connection.getSendViaNetwork().sendRegisteredPatient(registeredPatient);
                loggedMenu();
            }
             else {
                System.out.println("-> We don't have any doctor yet, you can't register, sorry! ");
                System.out.println("----------------------------------------------");
            }

        } else {
            System.out.println("-> This email is already associated with a patient! ");
            System.out.println("----------------------------------------------");
        }
    }
    /**
     * Handles the login workflow for an existing patient.
     *
     * <p>The method requests the patient's email and password, validates
     * formats locally, and sends the credentials to the server for verification.
     * If both email and password are confirmed by the server, the user is
     * redirected to the main logged menu.</p>
     *
     * <p>If the email does not exist or the password is incorrect,
     * the method returns to the previous menu.</p>
     *
     * @throws IOException if a communication error occurs during the login process
     */
    private void loginMenu() throws IOException {
        do {
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║            PATIENT LOGIN MENU          ║
            ╚════════════════════════════════════════╝
            """);
            String email;
            boolean valid;
            do {
                email = Utilities.readString("-> Enter your email: ");
                valid = Utilities.checkEmail(email);

            } while (!valid);
            connection.getSendViaNetwork().sendStrings(email);
            String emailVerification = connection.getReceiveViaNetwork().receiveString();

            if (emailVerification.equals("EMAIL OK")) {
                System.out.println("-> Email accepted! ");
                System.out.println("----------------------------------------------");
                String password;
                do {
                    password = Utilities.readString("-> Enter your password: ");
                    if (password == null || password.isEmpty()) {
                        System.out.println("-> Password cannot be empty!");
                        System.out.println("----------------------------------------------");
                    }
                } while (password == null || password.isEmpty());

                connection.getSendViaNetwork().sendStrings(password);
                String passwordVerification = connection.getReceiveViaNetwork().receiveString();

                if (passwordVerification.equals("PASSWORD OK")) {
                    System.out.println("-> Login successful! ");
                    System.out.println("----------------------------------------------");
                    this.loggedMenu();
                } else {
                    System.out.println("-> Incorrect password! ");
                    System.out.println("----------------------------------------------");
                    return;
                }
            } else {
                System.out.println("-> Email not found! ");
                System.out.println("----------------------------------------------");
                return;
            }
        } while (true);
    }

    /**
     * Displays the main patient menu after a successful login.
     *
     * <p>The menu offers the following actions:</p>
     * <ul>
     *     <li>View personal information</li>
     *     <li>View all reports associated with the patient</li>
     *     <li>Create and send a new report to the server</li>
     *     <li>Log out and return to the pre-login menu</li>
     * </ul>
     *
     * <p>The menu remains active until the patient chooses to log out.</p>
     *
     * @throws IOException if an error occurs while retrieving data or sending requests to the server
     */
    private void loggedMenu() throws IOException {
        Patient patient = connection.getReceiveViaNetwork().recievePatient();
        do{
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║          PATIENT MAIN MENU             ║
            ║                                        ║
            ║          1) View my information        ║
            ║          2) See my reports             ║
            ║          3) Create a new report        ║
            ║          4) Log out                    ║
            ╚════════════════════════════════════════╝
            """);
            System.out.println("-> Welcome " + patient.getFullName() + "!");
            System.out.println("----------------------------------------------");

            int option = Utilities.readInteger("-> Select an option: ");
            switch (option){
                case 1:
                    connection.getSendViaNetwork().sendInt(1);
                    this.seePatientInfo(patient);
                    break;
                case 2:
                    this.patientSeeReports(patient);
                    break;
                case 3:
                    connection.getSendViaNetwork().sendInt(2);
                    this.createReport(patient);
                    break;
                case 4:
                    connection.getSendViaNetwork().sendInt(3);
                    this.preLoggedMenu();
                    break;
                default:
                    System.out.println("-> Please select a valid option!");
                    System.out.println("----------------------------------------------");
                    break;
            }
        } while(true);
    }

    /**
     * Displays the patient's personal information: full name, email and date of birth.
     *
     * <p>The method requests the associated {@link User} object from the server
     * in order to obtain the patient's email. If a doctor is assigned, the server
     * also returns the doctor's full name, which is displayed to the user.</p>
     *
     * <p>The menu remains blocked until the user presses 0 to return to the
     * main menu of the logged section.</p>
     *
     * @param patient the logged-in patient whose information will be shown
     * @throws IOException if a communication problem occurs while retrieving data from the server
     */
    private void seePatientInfo(Patient patient) throws IOException {
        User patientUser = connection.getReceiveViaNetwork().recieveUser();
        System.out.println("""
        ╔════════════════════════════════════════╗
        ║            YOUR INFORMATION            ║
        ╚════════════════════════════════════════╝
        """);
        System.out.println("-> Full Name: " + patient.getFullName());
        System.out.println("-> Email: " + patientUser.getEmail());
        System.out.println("-> Date of birth: " + patient.getDob());

        if (patient.getDoctorId() != null) {
            String doctorName = connection.getReceiveViaNetwork().receiveString();
            System.out.println("-> Your assigned doctor is: " + doctorName);
            System.out.println("----------------------------------------------");
        } else {
            connection.getReceiveViaNetwork().receiveString();
            System.out.println("-> You don't have a doctor yet! ");
            System.out.println("----------------------------------------------");

        }
        do {
            int option = Utilities.readInteger("-> Press 0 to go back to the main menu: ");
            if (option == 0) {
                System.out.println("----------------------------------------------");
                return;
            } else {
                System.out.println("-> Please press 0 to go back! ");
                System.out.println("----------------------------------------------");
            }
        } while (true);
    }

    /**
     * Displays all reports previously created by the patient.
     *
     * <p>If no reports exist, the user is informed and returned immediately
     * to the main menu.</p>
     *
     * <p>When reports are available, they are sorted by date (most recent first)
     * and printed with full details: report date, patient and doctor observations,
     * and selected symptoms.</p>
     *
     * <p>The method waits for the user to press 0 before returning.</p>
     *
     * @param patient the patient whose reports will be displayed
     * @throws IOException if data processing or reading from the server fails
     */
    private void patientSeeReports(Patient patient) throws IOException {
        System.out.println("""
        ╔════════════════════════════════════════╗
        ║              YOUR REPORTS              ║
        ╚════════════════════════════════════════╝
        """);
        List<Report> reports = patient.getReports();

        if (reports == null || reports.isEmpty()) {
            System.out.println("-> You don't have any reports yet! ");
            System.out.println("----------------------------------------------");
            return;
        } else {
            reports.sort((r1, r2) -> r2.getReportDate().compareTo(r1.getReportDate()));

            for (int i = 0; i < reports.size(); i++) {
                Report report = reports.get(i);
                System.out.println("**********************************************");
                System.out.println("-> REPORT nº: " + (i+1) + " | Date: " + report.getReportDate());
                System.out.println("-> Patient Observation: " + report.getPatientObservation());
                System.out.println("-> Doctor Observation: " + report.getDoctorObservation());
                System.out.println("-> Symptoms:");
                for(int j = 0; j < report.getSymptoms().size(); j++){
                    System.out.println((j+1) + ") " + report.getSymptoms().get(j));
                }
                System.out.println("**********************************************");
            }

            do {
                int response = Utilities.readInteger("-> Press 0 to go back to the main menu: ");
                if (response == 0) {
                    System.out.println("----------------------------------------------");
                    return;
                } else {
                    System.out.println("-> Introduce a valid option! ");
                    System.out.println("----------------------------------------------");
                }
            } while (true);
        }
    }

    /**
     * Creates a new report for the given patient.
     *
     * <p>The method collects:</p>
     * <ul>
     *     <li>Current date as the report date</li>
     *     <li>Patient's written observations</li>
     *     <li>A variable-length list of symptoms chosen by the user</li>
     *     <li>BITalino signals captured and stored inside a CSV file</li>
     * </ul>
     *
     * <p>The created report is added to the patient's local report list,
     * sent to the server, and the server's confirmation is displayed.</p>
     *
     * @param patient the patient who is creating the new report
     * @throws IOException if file creation, signal capture or network communication fails
     */
    private void createReport(Patient patient) throws IOException {
        System.out.println("""
        ╔════════════════════════════════════════╗
        ║          CREATE A NEW REPORT           ║
        ╚════════════════════════════════════════╝
        """);
        LocalDate reportDate = LocalDate.now();
        String patientObservations = Utilities.readString("-> Introduce your observations: ");
        System.out.println("-> Available symptoms:");
        System.out.println("**********************************************");
        int index = 1;
        for (Symptoms symptom : Symptoms.values()) {
            System.out.println(index + ") " + symptom);
            index++;
        }
        System.out.println("**********************************************");

        List<Symptoms> selectedSymptoms = new ArrayList<>();
        while (true) {
            int choice = Utilities.readInteger("-> Please enter a symptom (0 to finish): ");
            if (choice == 0) {
                break;
            }

            if (choice < 0 || choice >= Symptoms.values().length) {
                System.out.println("-> Please enter a valid symptom (0 to finish)! ");
                System.out.println("----------------------------------------------");
                continue;
            }

            Symptoms chosenSymptom = Symptoms.values()[choice-1];
            if (!selectedSymptoms.contains(chosenSymptom)) {
                selectedSymptoms.add(chosenSymptom);
                System.out.println("-> " + chosenSymptom + " added.");
            } else {
                System.out.println("-> This symptom: " + chosenSymptom + " is already added!");
            }
        }

        String csvFilePath = manageFiles.createSignalsCSVFile(reportDate);
        System.out.println("-> CSV file created at: " + csvFilePath);
        System.out.println("----------------------------------------------");
        signalMenu(csvFilePath);

        Report report = new Report(patient.getPatientId(), reportDate, patientObservations, "", selectedSymptoms, csvFilePath);
        patient.getReports().add(report);
        System.out.println("-> Report created:");
        System.out.println("----------------------------------------------");

        connection.getSendViaNetwork().sendReport(report);
        String verificationReport = connection.getReceiveViaNetwork().receiveString();
        System.out.println("-> " + verificationReport);
        System.out.println("----------------------------------------------");
    }

    /**
     * Manages the interactive menu for capturing physiological signals using the BITalino device.
     *
     * <p>The user may repeatedly select one of the available {@link SignalType} values
     * (EMG, ECG, EDA, ACC), and each captured signal is appended to the previously created
     * CSV file using {@link ManageFiles#appendSignalToCSV(String, Signal)}.</p>
     *
     * <p>The capture process continues until the user decides to stop by selecting 0
     * or by declining to capture more signals.</p>
     *
     * @param csvFilePath the path of the CSV file where captured signals will be appended
     * @throws IOException if saving the signal data to the CSV file fails
     */
    public void signalMenu(String csvFilePath) throws IOException {
        boolean continueCapturing = true;
        while (continueCapturing) {
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║             SIGNAL CAPTURE             ║
            ╚════════════════════════════════════════╝
            """);
            int index2 = 1;
            for (SignalType type : SignalType.values()) {
                System.out.println(index2 + ") " + type);
                index2++;
            }
            System.out.println("0) Finish signal capture");
            System.out.println("----------------------------------------------");

            int choice = Utilities.readInteger("-> Please enter the signal that you want to capture (0 to finish): ");
            if (choice == 0) {
                System.out.println("-> Signal capture finished! ");
                System.out.println("----------------------------------------------");
                break;
            }

            if (choice < 1 || choice > SignalType.values().length) {
                System.out.println("-> Invalid signal. Try again! ");
                System.out.println("----------------------------------------------");
                continue;
            }

            SignalType signalType = SignalType.values()[choice - 1];
            Signal signal = bitalinoCapture.captureBitalinoSignal(signalType);

            if (signal != null) {
                manageFiles.appendSignalToCSV(csvFilePath, signal);
                System.out.println("-> " + signalType + " appended to file.");
                System.out.println("----------------------------------------------");
            }

            int answer = Utilities.readInteger("-> Do you want to capture another signal? (Press 0 to confirm and any other number to finish): ");
            System.out.println("----------------------------------------------");
            if (answer != 0) {
                continueCapturing = false;
            }
        }
    }

    /**
     * Terminates the Patient Application cleanly.
     *
     * <p>The method prints a closing message, releases all network resources
     * through {@link Connection#releaseResources()}, and exits the program.</p>
     *
     * <p>This method does not return, as it ends the entire application using {@code System.exit(0)}.</p>
     */
    private void exitMenu() {
        System.out.println("""
                ╔════════════════════════════════════════╗
                ║      EXITING PATIENT APPLICATION       ║
                ╚════════════════════════════════════════╝
                """);
        System.out.println("-> Closing Patient Application...");
        System.out.println("-> Releasing resources...");
        System.out.println("----------------------------------------------");
        connection.releaseResources();
        System.out.println("-> Goodbye!");
        System.out.println("----------------------------------------------");
        System.exit(0);
    }
}