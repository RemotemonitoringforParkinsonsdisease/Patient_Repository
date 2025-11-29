package ui;

import BITalino.*;
import POJOS.*;
import manageFiles.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UI {
    private Connection connection;
    private ManageFiles manageFiles = new ManageFiles();
    private BItalinoCapture bitalinoCapture = new BItalinoCapture();

    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        ui.startConnection();
    }

    private void startConnection() {
        boolean connected = false;

        while (!connected) {
            System.out.println("""
            ╔══════════════════════════════════════════════╗
            ║          PATIENT APPLICATION - CONNECT       ║
            ║                                              ║
            ║   Please enter the server connection info    ║
            ╚══════════════════════════════════════════════╝
            """);
            String ipAddress = Utilities.readString("-> IP Address: ");
            int port = Utilities.readInteger("-> Port: ");
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
            System.out.println("----------------------------------------------");        }
    }

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

        if (serverResponse.equals("EMAIL OK")) {
            System.out.println("-> Email accepted! ");
            System.out.println("----------------------------------------------");
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
            if (connection.getReceiveViaNetwork().receiveString().equals("DOCTOR ASSIGNED")) {
                connection.getSendViaNetwork().sendRegisteredPatient(registeredPatient);
                loggedMenu();
            } else {
                System.out.println("-> We don't have any doctor yet, you can't register, sorry! ");
                System.out.println("----------------------------------------------");
            }

        } else {
            System.out.println("-> This email is already associated with a patient! ");
            System.out.println("----------------------------------------------");
            registerMenu();
        }
    }

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
        System.out.println(report);
        System.out.println("----------------------------------------------");

        connection.getSendViaNetwork().sendReport(report);
        String verificationReport = connection.getReceiveViaNetwork().receiveString();
        System.out.println("-> " + verificationReport);
        System.out.println("----------------------------------------------");
    }

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