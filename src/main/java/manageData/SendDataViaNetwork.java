package manageData;

import POJOS.Patient;
import POJOS.Report;
import POJOS.Symptoms;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Class responsible for sending structured data over a network connection
 * using a {@link DataOutputStream}. It serializes objects such as {@link Patient},
 * {@link Report}, and lists of {@link Symptoms} following the expected protocol
 * of the receiving server.
 */
public class SendDataViaNetwork {
    private DataOutputStream dataOutputStream;

    /**
     * Constructor of the class.
     *
     * @param dos the output stream used to send serialized data to the server
     */
    public SendDataViaNetwork(DataOutputStream dos) {
        this.dataOutputStream = dos;
    }

    /**
     * Sends a UTF-encoded string over the network.
     *
     * @param message the string to send
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void sendStrings(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }

    /**
     * Sends an integer value over the network.
     *
     * @param message the integer to send
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void sendInt(int message) throws IOException{
        dataOutputStream.writeInt(message);
        dataOutputStream.flush();
    }

    /**
     * Sends the registration fields of a {@link Patient}, including:
     * (password, fullName, date of birth).
     *
     * @param patient the patient whose information will be sent
     * @throws IOException if an error occurs during transmission
     */
    public void sendRegisteredPatient(Patient patient) throws IOException{
        dataOutputStream.writeUTF(patient.getPatientPassword());
        dataOutputStream.writeUTF(patient.getFullName());
        dataOutputStream.writeUTF(patient.getDob().toString());
        dataOutputStream.flush();
    }

    /**
     * Sends the fields of a {@link Report} in the following order:
     * (patientId, reportDate, symptoms, patientObservation,
     * doctorObservation, CSV file path).
     *
     * @param r the report to send
     * @throws IOException if an I/O error occurs during transmission
     */
    public void sendReport(Report r) throws IOException{
        dataOutputStream.writeInt(r.getPatientId());
        dataOutputStream.writeUTF(r.getReportDate().toString());
        sendSymptoms(r.getSymptoms());
        dataOutputStream.writeUTF(r.getPatientObservation());
        dataOutputStream.writeUTF(r.getDoctorObservation());
        sendCSVFile(r.getSignalsFilePath());
        dataOutputStream.flush();
    }

    /**
     * Sends a CSV file through the network. The transmission includes: fileName, fileSize in bytes and the file content
     *
     * @param filePath the path to the CSV file to send
     * @throws IOException if an I/O error occurs while reading or sending the file
     */
    public void sendCSVFile (String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        dataOutputStream.writeUTF(file.getName());
        dataOutputStream.writeLong(file.length());

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }

        dataOutputStream.flush();
        fis.close();
    }

    /**
     * Sends a list of {@link Symptoms} as a comma-separated UTF string.
     * Example: {@code "TREMOR,ANXIETY,SOFT_VOICE"}.
     *
     * @param symptoms the list of symptoms to send
     * @throws IOException if an I/O error occurs during transmission
     */
    public void sendSymptoms(List<Symptoms> symptoms) throws IOException{
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < symptoms.size(); i++) {
            sb.append(symptoms.get(i).name());
            if (i < symptoms.size() - 1) {
                sb.append(",");
            }
        }
        dataOutputStream.writeUTF(sb.toString());
    }
}