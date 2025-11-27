package manageData;

import POJOS.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReceiveDataViaNetwork {

    private DataInputStream dataInputStream;


    public ReceiveDataViaNetwork(DataInputStream dis) {
        this.dataInputStream = dis;
    }


    public String receiveString() throws IOException{
        return dataInputStream.readUTF();
    }

    /*
   El primer paso es recibir los reports/el doctor/el paciente por separado para
   mas tarde unirlo en un mismo paciente
    */
    public Patient recievePatient() throws IOException{
        Patient patient = null;
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
        return patient;
    }

    public User recieveUser() throws IOException{
        User user = null;
        Integer userId = dataInputStream.readInt();
        String email = dataInputStream.readUTF();
        user = new User(userId,email);
        return user;
    }

    public Report receiveReport() throws IOException{
    Report report = null;
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
    return report;
    }

    public List<Report> receiveReports() throws IOException{
    List<Report> reports = new ArrayList<>();
    int numberOfReports = dataInputStream.readInt();
    if (numberOfReports == 0) {
        return reports; //devolvemos lista vacía sin intentar leer nada
    }
    for (int i = 0; i < numberOfReports; i++) {
        reports.add(receiveReport());
    }
    return reports;
    }

    public List<Signal> receiveSignals() throws IOException{
    List<Signal> signals = new ArrayList<>();
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
        String symptomsLine = dataInputStream.readUTF();
        if (symptomsLine == null) {
            return symptoms;
        }
        for (String s : symptomsLine.split(",")) {
            symptoms.add(Symptoms.valueOf(s.trim()));
        }
        return symptoms;
    }

}
