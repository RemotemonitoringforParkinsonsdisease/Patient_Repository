package ui;

import POJOS.Patient;

import java.io.IOException;
import java.time.LocalDate;

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
        System.out.println("\n\nWELCOME TO THE PATIENT APPLICATION\n\n");
        int option = 0;
        do {
            System.out.println("\n1) Login"
                    + "\n2) Register"
                    + "\n3) Exit"
            );
            option = Utilities.readInteger("\n\nSelect an option: ");
            switch (option){
                case 1: this.loginMenu();
                    break;
                case 2: this.registerMenu();
                    break;
                case 3: this.exitMenu();
                    break;
                default:
                    System.out.println("\nPlease select a valid option.\n");
                    break;
            }
        } while(true);
    }

    private void registerMenu(){
        System.out.println("\nREGISTER MENU");
        String fullName = Utilities.readString("Enter your full name: ");
        LocalDate dob = Utilities.readDate("Enter your DOB: ");
        String email = Utilities.readString("Enter your email: ");
        String password = Utilities.readString("Enter your password: ");
        //Enviar al servidor los datos para registrar
        //Se carga el propio doctor y sus pacientes
        //Una vez este confirmado
        this.loggedMenu();
    }

    private void loginMenu() throws IOException {
        System.out.println("\nLOGIN MENU");

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
            this.loggedMenu();
        } else {
            System.out.println("Login failed. Incorrect email or password.\n");
            loginMenu();
        }
        this.loggedMenu();
    }

    private void loggedMenu(){
        System.out.println("\nMAIN MENU");
        int option = 0;
        do{
            System.out.println("\n1) View my information" +
                    "\n2)See my reports" +
                    "\n3)Create a new report " +
                    "\n4)Exit"
            );
            switch (option = Utilities.readInteger("Select an option: ")){
                case 1:
                    this.patientSeeInfo();
                    break;
                case 2:
                    this.patientSeeReports();
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
    private void patientSeeInfo(){
        System.out.println("\nPATIENT LIST MENU");


    }
    private void patientSeeReports(){

    }
    private void createReport(){

    }
    private void exitMenu(){

    }
}
