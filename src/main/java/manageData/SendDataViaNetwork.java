package manageData;

import POJOS.Patient;
import POJOS.Report;
import POJOS.Symptoms;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
            sendCSVFile(r.getSignalsFilePath());
            dataOutputStream.writeUTF(r.getPatientObservation());
            dataOutputStream.writeUTF(r.getDoctorObservation());
            dataOutputStream.flush();
        }
    }

    public void sendReport(Report r) throws IOException{
        dataOutputStream.writeInt(r.getPatientId());
        dataOutputStream.writeUTF(r.getReportDate().toString());
        sendSymptoms(r.getSymptoms());
        dataOutputStream.writeUTF(r.getPatientObservation());
        dataOutputStream.writeUTF(r.getDoctorObservation());
        sendCSVFile(r.getSignalsFilePath());
        dataOutputStream.flush();
    }

    public void sendCSVFile (String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        // 1. Enviar el nombre del archivo
        dataOutputStream.writeUTF(file.getName());

        // 2. Enviar tamaño del archivo
        dataOutputStream.writeLong(file.length());

        // 3. Enviar contenido
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }

        dataOutputStream.flush();
        fis.close();
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
}