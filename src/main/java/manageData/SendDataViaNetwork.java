package manageData;

import POJOS.Patient;
import POJOS.Report;
import POJOS.Signal;
import POJOS.Symptoms;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendDataViaNetwork {
    private DataOutputStream dataOutputStream;

    public SendDataViaNetwork(Socket socket) {
        try{
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Output stream could not be created: " + e.getMessage());
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, e);
        }

    }
    public void sendStrings(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }

    public void sendInt(int message) throws IOException{
        dataOutputStream.writeInt(message);
        dataOutputStream.flush();
    }

    public void sendPatient(Patient patient) throws IOException{
        dataOutputStream.writeInt(patient.getPatientId());
        dataOutputStream.writeInt(patient.getUserId());
        dataOutputStream.writeInt(patient.getDoctorId());
        dataOutputStream.writeUTF(patient.getPatientPassword());
        dataOutputStream.writeUTF(patient.getFullName());
        dataOutputStream.writeUTF(patient.getDob().toString());
        sendReports(patient.getReports());
        dataOutputStream.flush();
    }

    public void sendPatientRegister(Patient patient) throws IOException{
        dataOutputStream.writeUTF(patient.getPatientPassword());
        dataOutputStream.writeUTF(patient.getFullName());
        dataOutputStream.writeUTF(patient.getDob().toString());
        dataOutputStream.flush();
    }

    public void sendReports(List<Report> reports) throws IOException{
        for (Report r : reports) {
            dataOutputStream.writeInt(r.getReportId());
            dataOutputStream.writeInt(r.getPatientId());
            dataOutputStream.writeUTF(r.getReportDate().toString());
            sendSymptoms(r.getSymptoms());
            sendSignals(r.getSignals());
            dataOutputStream.writeUTF(r.getPatientObservation());
            dataOutputStream.writeUTF(r.getDoctorObservation());
            dataOutputStream.flush();
        }
    }

    public void sendReport(Report r) throws IOException{
        dataOutputStream.writeInt(r.getReportId());
        dataOutputStream.writeInt(r.getPatientId());
        dataOutputStream.writeUTF(r.getReportDate().toString());
        sendSymptoms(r.getSymptoms());
        sendSignals(r.getSignals());
        dataOutputStream.writeUTF(r.getPatientObservation());
        dataOutputStream.writeUTF(r.getDoctorObservation());
        dataOutputStream.flush();
    }


    public void sendSymptoms(List<Symptoms> symptoms) throws IOException{
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < symptoms.size(); i++) {
            sb.append(symptoms.get(i).name());
            if (i < symptoms.size() - 1) {
                sb.append(",");  // Añadir coma excepto en el último
            }
        }

        dataOutputStream.writeUTF(sb.toString());
    }

    public void sendSignals(List<Signal> signals) throws IOException{
        dataOutputStream.writeInt(signals.size());

        for (Signal signal : signals) {
            dataOutputStream.writeInt(signal.getSignalId());
            dataOutputStream.writeUTF(signal.getSignalType().name());
            sendListOfIntegerValues(signal.getValues());
        }
    }

    public void sendListOfIntegerValues(List<Integer> values) throws IOException{
        dataOutputStream.writeInt(values.size());
        for (Integer value : values) {
            dataOutputStream.writeInt(value);
        }
    }

    public void releaseResources() {
        try {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        } catch (IOException ex) {
            System.err.println("Error releasing resources: " + ex.getMessage());
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}