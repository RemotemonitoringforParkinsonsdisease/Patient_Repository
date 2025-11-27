package manageData;

import POJOS.Patient;
import POJOS.Report;
import POJOS.Signal;
import POJOS.Symptoms;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;


public class SendDataViaNetwork {
    private DataOutputStream dataOutputStream;

    public SendDataViaNetwork(DataOutputStream dos) {
        this.dataOutputStream = dos;
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

    public void sendRegisteredPatient(Patient patient) throws IOException{
        dataOutputStream.writeUTF(patient.getPatientPassword());
        dataOutputStream.writeUTF(patient.getFullName());
        dataOutputStream.writeUTF(patient.getDob().toString());
        dataOutputStream.flush();
    }

    public void sendReports(List<Report> reports) throws IOException{
        for (Report r : reports) {
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
            dataOutputStream.writeUTF(signal.getSignalType().name());
            dataOutputStream.writeInt(signal.getSamplingRate());
            sendListOfIntegerValues(signal.getValues());
        }
    }

    public void sendListOfIntegerValues(List<Integer> values) throws IOException{
        dataOutputStream.writeInt(values.size());
        for (Integer value : values) {
            dataOutputStream.writeInt(value);
        }
    }

}