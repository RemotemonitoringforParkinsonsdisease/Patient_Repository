package manageData;

import POJOS.Doctor;
import POJOS.Patient;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        int id = dataInputStream.readInt();
        String name = dataInputStream.readUTF();
        String surname = dataInputStream.readUTF();
        String date = dataInputStream.readUTF();
        String email = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        doctor = new Doctor(id, name, surname, dob, email);

        return doctor;
    }

    public Patient recievePatient(){
        Patient patient = null;
        try {
            int id = dataInputStream.readInt();
            String name = dataInputStream.readUTF();
            String surname = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            patient = new Patient(id, name, surname, dob, email);
        } catch (EOFException ex) {
            System.out.println("Todos los datos fueron leídos correctamente.");
        } catch (IOException ex) {
            System.err.println("Error al recibir datos del paciente: " + ex.getMessage());
            ex.printStackTrace();
        }
        return patient;
    }

    public Interpretation recieveInterpretation() throws IOException{
        Interpretation interpretation = null;
        try {
            String stringDate = dataInputStream.readUTF();
            int doctorId = dataInputStream.readInt();
            String stringEMG = dataInputStream.readUTF();
            int patientId = dataInputStream.readInt();
            String stringEDA = dataInputStream.readUTF();
            String observation = dataInputStream.readUTF();
            String interpretationText = dataInputStream.readUTF();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(stringDate, formatter);

            Signal signalEMG = new Signal(Signal.SignalType.EMG);
            signalEMG.setValuesEMG(stringEMG);

            Signal signalEDA = new Signal(Signal.SignalType.EDA);
            signalEDA.setValuesEDA(stringEDA);

            interpretation = new Interpretation(date, interpretationText, signalEMG, signalEDA, patientId, doctorId, observation);
        } catch (EOFException ex) {
            System.out.println("Todos los datos fueron leídos correctamente.");
        }
        return interpretation;
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
