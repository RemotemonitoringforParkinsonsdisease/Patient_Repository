package manageData;

import POJOS.*;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    /*
   El primer paso es recibir los reports/el doctor/el paciente por separado para
   mas tarde unirlo en un mismo paciente
    */
    public Patient recievePatient(){
        Patient patient = null;
        try {
            Integer patientId = dataInputStream.readInt();
            Integer userId = dataInputStream.readInt();
            Integer doctorId = dataInputStream.readInt();
            String patientPassword = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            List<Report> reports = receiveReports();

            patient = new Patient(patientId, userId, doctorId, patientPassword, fullName, dob, reports);
        } catch (EOFException ex) {
            System.out.println("Todos los datos fueron leídos correctamente.");
        } catch (IOException ex) {
            System.err.println("Error al recibir datos del paciente: " + ex.getMessage());
            ex.printStackTrace();
        }
        return patient;
    }

    public User recieveUser(){
        User user = null;
        try {
            Integer userId = dataInputStream.readInt();
            String email = dataInputStream.readUTF();

            user = new User(userId,email);
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return user;
    }

    public Report receiveReport() throws IOException{
        Report report = null;

        try {
            Integer reportId = dataInputStream.readInt();
            Integer patientId = dataInputStream.readInt();
            String date = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate reportDate = LocalDate.parse(date, formatter);
            List<Signal> signals = receiveSignals();
            List<Symptoms> symptoms = receiveSymptoms();
            String patientObservation = dataInputStream.readUTF();
            String doctorObservation = dataInputStream.readUTF();

            report = new Report(reportId, patientId, reportDate, signals, symptoms, patientObservation, doctorObservation);
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return report;
    }

    public List<Report> receiveReports() throws IOException{
        List<Report> reports = new ArrayList<>();
        try {
            int numberOfReports = dataInputStream.readInt();
            if (numberOfReports == 0) {
                return reports; //devolvemos lista vacía sin intentar leer nada
            }
            for (int i = 0; i < numberOfReports; i++) {
                reports.add(receiveReport());
            }
            return reports;
        } catch (IOException e){
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return reports;
    }

    public List<Signal> receiveSignals() throws IOException{
        List<Signal> signals = new ArrayList<>();
        try {
            int numSignals = dataInputStream.readInt();
            if (numSignals == 0) {
                return signals;
            }

            for (int i = 0; i < numSignals; i++) {
                Integer signalId = dataInputStream.readInt();
                String typeSignal = dataInputStream.readUTF();
                SignalType signalType = SignalType.valueOf(typeSignal);
                Integer samplingRate = dataInputStream.readInt();
                List<Integer> values = receiveListOfIntegerValues();
                Signal signal = new Signal(signalId, signalType, samplingRate, values);
                signals.add(signal);
            }
            return signals;
        } catch (IOException e) {
            System.out.println("Error al leer el flujo de entrada " + e.getMessage());
        }
        return signals;
    }

    public List<Integer> receiveListOfIntegerValues() throws IOException {
        List<Integer> values = new ArrayList<>();
        int numValues = dataInputStream.readInt();
        if (numValues == 0) {
            return values;
        }
        for (int i = 0; i < numValues; i++) {
            Integer value = dataInputStream.readInt();
            values.add(value);
        }
        return values;
    }

    public List<Symptoms> receiveSymptoms() throws IOException{
        List<Symptoms> symptoms = new ArrayList<>();
        try {
            String symptomsLine = dataInputStream.readUTF();
            if (symptomsLine == null) {
                return symptoms;
            }
            for (String s : symptomsLine.split(",")) {
                symptoms.add(Symptoms.valueOf(s.trim()));
            }
            return symptoms;
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return symptoms;
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
