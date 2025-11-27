package manageData;

import POJOS.*;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    String signalsFilePath = receiveCSVFile();
    List<Symptoms> symptoms = receiveSymptoms();
    String patientObservation = dataInputStream.readUTF();
    String doctorObservation = dataInputStream.readUTF();
    report = new Report(reportId, patientId, reportDate, signalsFilePath, symptoms, patientObservation, doctorObservation);
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

    public String receiveCSVFile() throws IOException {

        String fileName = dataInputStream.readUTF();
        long fileSize = dataInputStream.readLong();

        String folder = "server_files/";
        Files.createDirectories(Paths.get(folder));

        Path filePath = Paths.get(folder + fileName);
        FileOutputStream fos = new FileOutputStream(filePath.toFile());

        byte[] buffer = new byte[4096];
        long remaining = fileSize;
        int bytesRead;

        while (remaining > 0 && (bytesRead = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
            fos.write(buffer, 0, bytesRead);
            remaining -= bytesRead;
        }

        fos.close();
        return filePath.toString(); // ruta en el servidor
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
