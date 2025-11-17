package manageData;

import POJOS.Patient;
import POJOS.Report;
import POJOS.Signal;
import POJOS.Symptoms;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Set;
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
        dataOutputStream.writeUTF(patient.getFullName());
        dataOutputStream.writeUTF(patient.getDob().toString());
        dataOutputStream.writeUTF(patient.getEmail());
        dataOutputStream.writeUTF(patient.getPassword());
        dataOutputStream.flush();

    }

    public void sendReport(Report report) throws IOException{
        dataOutputStream.writeUTF(report.getReportId());
        sendPatient(report.getPatient());
        dataOutputStream.writeUTF(report.getReportDate().toString());
        dataOutputStream.writeUTF(report.getPatientObservation());
        dataOutputStream.writeUTF(report.getDoctorObservation());
        sendSymptoms(report.getSymptoms());
        sendSignals(report.getSignals());
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

    public void sendSignals(Set<Signal> signals) throws IOException{
        dataOutputStream.writeInt(signals.size());
        for (Signal signal : signals) {
            dataOutputStream.writeUTF(signal.getSignalType().name());  // enum
            dataOutputStream.writeUTF(signal.getSignalId());           // id
            dataOutputStream.writeUTF(signal.valuesToString());        // lista de floats → string
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
