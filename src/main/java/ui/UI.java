package ui;

import BITalino.*;
import POJOS.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UI {
    private Connection connection;

    public static void main(String[] args) throws IOException {
        UI ui = new UI();

        ui.startConnection();
        ui.preLoggedMenu();
    }

    private void startConnection() throws IOException { //TODO: Iterar hasta conexión correcta
        System.out.println("Select IP Address and Port to connect to :");
        String ipAddress = Utilities.readString("IP Address: ");
        int port = Utilities.readInteger("Port: ");
        this.connection = new Connection(ipAddress, port);
        connection.getSendViaNetwork().sendInt(1);
        String message = connection.getReceiveViaNetwork().receiveString();
        System.out.println(message);
        if(message.equals("PATIENT")){
            this.preLoggedMenu();
        }
    }

    private void preLoggedMenu() throws IOException {
        System.out.println("\n\n-----WELCOME TO THE PATIENT APPLICATION-----\n\n");
        int option = 0;
        do {
            System.out.println("\n1) Log-in"
                    + "\n2) Register"
                    + "\n3) Exit"
            );
            option = Utilities.readInteger("\n\nSelect an option: ");
            switch (option){
                case 1:
                    connection.getSendViaNetwork().sendInt(1);
                    this.loginMenu();
                    break;
                case 2:
                    connection.getSendViaNetwork().sendInt(2);
                    this.registerMenu();
                    break;
                case 3:
                    connection.getSendViaNetwork().sendInt(3);
                    this.exitMenu();
                    break;
                default:
                    System.out.println("\nPlease select a valid option.\n");
                    break;
            }
        } while(true);
    }

    private void registerMenu() throws IOException {
        System.out.println("\n-----REGISTER MENU-----\n");

        String email;
        boolean valid;

        do {
            email = Utilities.readString("Enter your email: ");
            valid = Utilities.checkEmail(email);

            if (!valid) {
                System.out.println("Please follow the email format: example@example.com\n");
            }
        } while (!valid);

        connection.getSendViaNetwork().sendStrings(email);
        String serverResponse = connection.getReceiveViaNetwork().receiveString();

        if (serverResponse.equals("EMAIL OK")) {
            String fullName;
            do {
                fullName = Utilities.readString("Enter your full name: ");
                if (fullName == null || fullName.trim().isEmpty()) {
                    System.out.println("Name cannot be empty.\n");
                }
            } while (fullName == null || fullName.trim().isEmpty());

            LocalDate dob = Utilities.readDate("Enter your DOB: ");

            String password;
            do {
                password = Utilities.readString("Enter your password: ");
                if (password == null || password.isEmpty()) {
                    System.out.println("Password cannot be empty.\n");
                }
            } while (password == null || password.isEmpty());

            Patient registeredPatient = new Patient(password, fullName, dob);
            connection.getSendViaNetwork().sendRegisteredPatient(registeredPatient);
            connection.getSendViaNetwork().sendStrings("PATIENT REGISTERED");
            loggedMenu();

        } else {
            System.out.println("Register failed. Email already exists!\n");
            registerMenu();
        }
    }

    private void loginMenu() throws IOException {
        System.out.println("\n-----PATIENT LOGIN MENU-----\n");

        String email;
        boolean valid;
        do {
            email = Utilities.readString("Enter your email: ");
            valid = Utilities.checkEmail(email);

            if (!valid) {
                System.out.println("Please follow the email format: example@example.com\n");
            }
        } while (!valid);

        connection.getSendViaNetwork().sendStrings(email);
        String emailVerification = connection.getReceiveViaNetwork().receiveString();

        if (emailVerification.equals("EMAIL OK")) {
            String password;
            do {
                password = Utilities.readString("Enter your password: ");
                if (password == null || password.isEmpty()) {
                    System.out.println("Password cannot be empty.\n");
                }
            } while (password == null || password.isEmpty());

            connection.getSendViaNetwork().sendStrings(password);
            String passwordVerification = connection.getReceiveViaNetwork().receiveString();

            if (passwordVerification.equals("PASSWORD OK")) {
                System.out.println("Login successful!\n");
                loggedMenu();
            } else {
                System.out.println("Login failed. Incorrect email or password.\n");
                loginMenu();
            }
        }
    }

    private void loggedMenu() throws IOException {
        String check = connection.getReceiveViaNetwork().receiveString();
        if (check.equals("PATIENT LOGGED")) {
            Patient patient = connection.getReceiveViaNetwork().recievePatient();
            System.out.println("Welcome " + patient.getFullName() + "!\n");

            System.out.println("\n-----PATIENT MAIN MENU-----");
            int option = 0;
            do{
                System.out.println("\n1) View my information" +
                        "\n2) See my reports" +
                        "\n3) Create a new report " +
                        "\n4) Exit"
                );
                switch (option = Utilities.readInteger("Select an option: ")){
                    case 1:
                        connection.getSendViaNetwork().sendInt(1);
                        this.patientSeeInfo(patient);
                        break;
                    case 2:
                        this.patientSeeReports(patient);
                        break;
                    case 3:
                        connection.getSendViaNetwork().sendInt(2);
                        this.createReport(patient);
                        break;
                    case 4:
                        this.exitMenu();
                        break;
                    default:
                        System.out.println("Please select a valid option.\n");
                        break;
                }
            } while(true);
        } else {
            System.out.println("Login failed");
            loginMenu();
        }
    }

    //TODO
    private void patientSeeInfo(Patient patient) throws IOException {
        User patientUser = connection.getReceiveViaNetwork().recieveUser();
        System.out.println("\n-----YOUR INFORMATION-----\n");
        System.out.println("Full Name: " + patient.getFullName());
        System.out.println("Email: " + patientUser.getEmail());
        System.out.println("Date of birth: " + patient.getDob());

        if (patient.getDoctorId() != null) {
            //Doctor
            System.out.println("Your assigned doctor: " + patient.getDoctor().getFullName());
        }
        System.out.println("\nPress ENTER to go back to the main menu...");
        Utilities.readString("");
        this.loggedMenu(patient);
    }

    //TODO
    private void patientSeeReports(Patient patient) throws IOException {
        System.out.println("\n-----YOUR REPORTS-----\n");
        List<Report> reports = patient.getReports();

        if (reports == null || reports.isEmpty()) {
            System.out.println("You don't have any reports yet!\n");
            System.out.println("\nPress ENTER to go back to the main menu...");
            Utilities.readString("");
            this.loggedMenu(patient);
        }

        //ordenamos por fechas, pero en verdad ya estarán ordenadas no??
        reports.sort((r1, r2) -> r2.getReportDate().compareTo(r1.getReportDate()));

        for (int i = 0; i < reports.size(); i++) {
            Report report = reports.get(i);
            System.out.println("\n-----REPORT nº: " + (i+1) + " with date: " + report.getReportDate() + "-----\n");
            System.out.println("Patient Observation: " + report.getPatientObservation());
            System.out.println("Doctor Observation: " + report.getDoctorObservation());
            System.out.println("Symptoms:");

            for (int j = 0; j < report.getSymptoms().size(); j++) {
                System.out.println(" - " + report.getSymptoms().get(i));
            }
        }
        System.out.println("\nPress ENTER to go back...");
        Utilities.readString("");
        this.loggedMenu(patient);
    }

    private void createReport(Patient patient) throws IOException {
        System.out.println("\n-----CREATE NEW REPORT-----\n");
        LocalDate reportDate = LocalDate.now();
        String patientObservations = Utilities.readString("Introduce your observations: ");
        System.out.println("Select your symptoms (type 0 to finish):");
        System.out.println("Available symptoms:");

        int index = 1;
        for (Symptoms symptom : Symptoms.values()) {
            System.out.println(index + ") " + symptom);
            index++;
        }

        List<Symptoms> selectedSymptoms = new ArrayList<>();
        while (true) {
            int choice = Utilities.readInteger("Please enter a symptom (0 to finish): ");

            if (choice == 0) {
                break;
            }

            if (choice < 0 || choice >= Symptoms.values().length) {
                System.out.println("Please enter a valid symptom (0 to finish): ");
                continue;
            }
            //Accede a todos los valores del enum como un array (abajo)
            //Resta 1 porque el menú empieza en 1 pero el array en 0
            Symptoms chosenSymptom = Symptoms.values()[choice-1];
            selectedSymptoms.add(chosenSymptom);

            System.out.println(chosenSymptom + ", added.");
        }

        System.out.println("\n-----SIGNAL CAPTURE-----");
        Set<Signal> signals = new HashSet<>();

        int index2 = 1;
        for (SignalType type : SignalType.values()) {
            System.out.println(index2 + ") " + type);
            index2++;
        }

        while (true) {
            int choice = Utilities.readInteger("Please enter a signal (0 to finish): ");

            if (choice == 0) {
                break;
            }

            if (choice < 0 || choice >= SignalType.values().length) {
                System.out.println("Please enter a valid signal (0 to finish): ");
                continue;
            }

            SignalType signalType = SignalType.values()[choice-1];
            Signal signal = captureBitalinoSignal(signalType);

            if (signal != null) {
                signals.add(signal);
            }
        }

        Report report = new Report(patient, reportDate, patientObservations, null, selectedSymptoms, signals);
        connection.getSendViaNetwork().sendReports(report);
    }

    private Signal captureBitalinoSignal(SignalType type) {

        System.out.println("\nConnecting to BITalino...");
        String mac = "...";  //Cambiar por el nuestro

        //Canal traducido del tipo de señal que le pasamos
        int channel = mapSignalTypeToChannel(type);

        //Crear ID único para la señal (NO ENTIENDO Q POLLAS HACE ESTO)
        String signalId = type + "_" + System.currentTimeMillis();
        Signal signal = new Signal(type, signalId);

        try {

            BITalino device = new BITalino();

            //Abrir conexión
            device.open(mac, 100);

            //Iniciar adquisición SOLO del canal elegido
            device.start(new int[]{channel});

            System.out.println("Recording " + type + " signal...");

            //Lee 100 muestras (nuestro samplingRate)
            Frame[] frames = device.read(100);

            //Guarda valores en la señal
            for (Frame frame : frames) {
                float value = frame.analog[channel];
                signal.getValues().add(value);
            }

            device.stop();
            device.close();

            System.out.println("Signal captured successfully!\n");
            return signal;

        } catch (Exception e) {
            System.out.println("Error capturing " + type + " signal: " + e.getMessage());
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    //Los tipos de señal están asignados a los canales de la placa de bitalino
    private int mapSignalTypeToChannel(SignalType type) {
        switch (type) {
            case ECG: return 0;
            case EMG: return 1;
            case EDA: return 2;
            case ACC: return 3; // eje X, por ejemplo
            default: return 0;
        }
    }

    private void exitMenu(){
        System.out.println("\nClosing the application...");
        System.out.println("Goodbye!");
        connection.releaseResources();
        System.exit(0);
    }
}