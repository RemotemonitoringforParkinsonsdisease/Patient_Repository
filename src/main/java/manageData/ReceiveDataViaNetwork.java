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

/**
 * Handles the reception of structured data from a network connection using a {@link DataInputStream}.
 * This class is responsible for reconstructing {@link Patient}, {@link User}, {@link Report},
 * and other POJO objects from serialized data sent over the network.
 *
 * The methods in this class assume the data is received in a specific order and format,
 * matching the protocol defined by the server.
 */
public class ReceiveDataViaNetwork {

    private DataInputStream dataInputStream;

    /**
     * Constructor of the class.
     *
     * @param dis the input data stream used to read objects from the server
     */
    public ReceiveDataViaNetwork(DataInputStream dis) {
        this.dataInputStream = dis;
    }

    /**
     * Receives a UTF-encoded string sent over the network.
     *
     * @return the received string
     * @throws IOException if an I/O error occurs while reading the stream
     */
    public String receiveString() throws IOException{
        return dataInputStream.readUTF();
    }

    /**
     * Reconstructs a {@link Patient} object from the received data
     * (patientId, userId, doctorId, patientPassword, fullName, date of birth, list of reports).
     *
     * @return the reconstructed Patient
     * @throws IOException if an I/O error occurs while reading the stream
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

    /**
     * Reconstructs a {@link User} object from the received data (userId, email).
     *
     * @return the reconstructed User
     * @throws IOException if an error occurs during reading
     */
    public User recieveUser() throws IOException{
        User user = null;
        Integer userId = dataInputStream.readInt();
        String email = dataInputStream.readUTF();
        user = new User(userId,email);
        return user;
    }

    /**
     * Reconstructs a {@link Report} object from the received data
     * (reportId, patientId, reportDate, signalsFilePath, symptoms, patientObservation, doctorObservation).
     *
     * @return the reconstructed Report
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Receives a list of reports. It first reads the number of reports,
     * and then reconstructs each report individually.
     *
     * @return a list of received {@link Report} objects
     * @throws IOException if an error occurs during reading
     */
    public List<Report> receiveReports() throws IOException{
    List<Report> reports = new ArrayList<>();
    int numberOfReports = dataInputStream.readInt();
    if (numberOfReports == 0) {
        return reports;
    }
    for (int i = 0; i < numberOfReports; i++) {
        reports.add(receiveReport());
    }
    return reports;
    }

    /**
     * Receives a CSV file sent over the network and saves it in the signals_recived/ directory.
     *
     * @return the full path to the saved file
     * @throws IOException if an error occurs while writing the file or reading the stream
     */
    public String receiveCSVFile() throws IOException {
        String fileName = dataInputStream.readUTF();
        long fileSize = dataInputStream.readLong();

        String folder = "signals_recived/";
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
        return filePath.toString();
    }

    /**
     * Receives a list of symptoms encoded as a comma-separated string.
     * Example: {@code "TREMOR,ANXIETY"}.
     *
     * @return a list of {@link Symptoms} values
     * @throws IOException if an I/O error occurs
     */
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
