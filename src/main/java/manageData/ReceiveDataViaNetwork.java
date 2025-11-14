package manageData;

import POJOS.Doctor;
import POJOS.Patient;
import POJOS.Report;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class ReceiveDataViaNetwork {

    private DataInputStream dataInputStream;


    public ReceiveDataViaNetwork(Socket socket) {
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error al inicializar el flujo de entrada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String receiveString() throws IOException{
        return dataInputStream.readUTF();
    }


    public Doctor receiveDoctor() throws IOException{
        Doctor doctor = null;
        String fullName = dataInputStream.readUTF();
        String email = dataInputStream.readUTF();
        doctor = new Doctor(email, fullName);
        return doctor;
    }

    /*
    Hay que añadir un recieveReport
     */

    public Report recieveReport() throws IOException{
        Report report = null;
        String data = dataInputStream.readUTF();
        report = new Report();
        return report;
    }

    /*
    El primer paso es recibir los reports/el doctor/el paciente por separado para
    mas tarde unirlo en un mismo paciente
     */
    public Patient recievePatient(){
        Patient patient = null;
        try {
            String id = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();
            String password = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            patient = new Patient(id, email, fullName, password, dob);
        } catch (EOFException ex) {
            System.out.println("Todos los datos fueron leídos correctamente.");
        } catch (IOException ex) {
            System.err.println("Error al recibir datos del paciente: " + ex.getMessage());
            ex.printStackTrace();
        }
        return patient;
    }


    public int receiveInt() {
        int message = 0;
        try {
            message = dataInputStream.readInt();
        } catch (IOException ex) {
            System.err.println("Error al recibir int: " + ex.getMessage());
            ex.printStackTrace();
        }
        return message;
    }
    /**
     * Releases the resources used by the `DataInputStream`.
     */
    public void releaseResources() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        } catch (IOException ex) {
            System.err.println("Error al liberar los recursos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
