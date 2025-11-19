package ui;

import POJOS.Patient;
import POJOS.Report;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class UI {
    private Connection connection;

    public static void main(String[] args) throws IOException {
        UI ui = new UI();

        ui.startConnection();
        ui.preLoggedMenu();
    }

    private void startConnection() { //TODO: Iterar hasta conexión correcta
        System.out.println("Select IP Address and Port to connect to :");
        String ipAddress = Utilities.readString("IP Address: ");
        int port = Utilities.readInteger("Port: ");
        this.connection = new Connection(ipAddress, port);
    }

    private void preLoggedMenu() throws IOException {
        System.out.println("\n\n-----WELCOME TO THE PATIENT APPLICATION-----\n\n");
        int option = 0;
        do {
            System.out.println("\n1) Login"
                    + "\n2) Register"
                    + "\n3) Exit"
            );
            option = Utilities.readInteger("\n\nSelect an option: ");
            switch (option){
                case 1:
                    this.loginMenu();
                    break;
                case 2:
                    this.registerMenu();
                    break;
                case 3:
                    this.exitMenu();
                    break;
                default:
                    System.out.println("\nPlease select a valid option.\n");
                    break;
            }
        } while(true);
    }

    private void registerMenu(){
        System.out.println("\n-----REGISTER MENU-----\n");
        String fullName = Utilities.readString("Enter your full name: ");
        LocalDate dob = Utilities.readDate("Enter your DOB: ");
        String email = Utilities.readString("Enter your email: ");
        String password = Utilities.readString("Enter your password: ");
        //Enviar al servidor los datos para registrar
        //Se carga el propio doctor y sus pacientes
        //Una vez este confirmado
        //this.loggedMenu();
    }

    private void loginMenu() throws IOException {
        System.out.println("\n-----LOGIN MENU-----\n");

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

        String password;
        do {
            password = Utilities.readString("Enter your password: ");
            if (password == null || password.isEmpty()) {
                System.out.println("Password cannot be empty.\n");
            }
        } while (password == null || password.isEmpty());
        connection.getSendViaNetwork().sendStrings(password);

        String serverResponse = connection.getReceiveViaNetwork().receiveString();

        if (serverResponse.equals("OK")) {
            System.out.println("Login successful!\n");
            Patient loggedPatient = connection.getReceiveViaNetwork().recievePatient();
            System.out.println("Welcome " + loggedPatient.getFullName() + "!\n");
            this.loggedMenu(loggedPatient);
        } else {
            System.out.println("Login failed. Incorrect email or password.\n");
            loginMenu();
        }
    }

    private void loggedMenu(Patient patient){
        System.out.println("\nMAIN MENU");
        int option = 0;
        do{
            System.out.println("\n1) View my information" +
                    "\n2) See my reports" +
                    "\n3) Create a new report " +
                    "\n4) Exit"
            );
            switch (option = Utilities.readInteger("Select an option: ")){
                case 1:
                    this.patientSeeInfo(patient);
                    break;
                case 2:
                    this.patientSeeReports(patient);
                    break;
                case 3:
                    this.createReport();
                    break;
                case 4:
                    this.exitMenu();
                    break;
                default:
                    System.out.println("Please select a valid option.\n");
                    break;
            }
        } while(true);
    }

    private void patientSeeInfo(Patient patient){
        System.out.println("\n-----YOUR INFORMATION-----\n");
        System.out.println("Full Name: " + patient.getFullName());
        System.out.println("Email: " + patient.getEmail());
        System.out.println("Date of birth: " + patient.getDob());

        if (patient.getDoctor() != null) {
            System.out.println("Your assigned doctor: " + patient.getDoctor().getFullName());
        }
        System.out.println("\nPress ENTER to go back to the main menu...");
        Utilities.readString("");
        this.loggedMenu(patient);
    }

    private void patientSeeReports(Patient patient){
        System.out.println("\n-----YOUR REPORTS-----\n");
        List<Report> reports = patient.getReports();

        if (reports == null || reports.isEmpty()) {
            System.out.println("You don't have any reports yet!\n");
            System.out.println("\nPress ENTER to go back to the main menu...");
            Utilities.readString("");
            this.loggedMenu(patient);
        }

        //ordenamos por fechas
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

    private void createReport(){

    }
    private void exitMenu(){

    }
}
