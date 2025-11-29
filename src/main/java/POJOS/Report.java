package POJOS;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a medical report created for a specific patient.
 * A report contains the capture date, symptoms identified, observations
 * written by both patient and doctor, and the file path to the signals
 * CSV associated with the report.
 *
 * Reports can be created locally or reconstructed from data received
 * via the network.
 */
public class Report {
    private Integer reportId;
    private Integer patientId;
    private LocalDate reportDate;
    private List<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;
    private String signalsFilePath;

    /**
     * Full constructor used when all report information is known, including
     * the unique report identifier.
     *
     * @param reportId           the unique identifier of the report
     * @param patientId          the identifier of the patient this report belongs to
     * @param reportDate         the date when the report was created
     * @param signalsFilePath    the path to the CSV file containing captured signals
     * @param symptoms           the list of symptoms associated with the report
     * @param patientObservation the observation written by the patient
     * @param doctorObservation  the observation written by the doctor
     */
    public Report(Integer reportId, Integer patientId, LocalDate reportDate, String signalsFilePath, List<Symptoms> symptoms, String patientObservation, String doctorObservation) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.signalsFilePath = signalsFilePath;
        this.symptoms = symptoms;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }

    /**
     * Constructor used when creating a new report locally, before assigning
     * a unique report identifier.
     *
     * @param patientId          the identifier of the patient this report belongs to
     * @param reportDate         the date when the report was created
     * @param patientObservation the observation written by the patient
     * @param doctorObservation  the observation written by the doctor
     * @param symptoms           the list of symptoms associated with the report
     * @param signalsFilePath    the path to the CSV file containing captured signals
     */
    public Report(Integer patientId, LocalDate reportDate, String patientObservation, String doctorObservation, List<Symptoms> symptoms, String signalsFilePath) {
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signalsFilePath = signalsFilePath;
    }

    /**
     * @return the identifier of the patient associated with this report
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * @return the date the report was created
     */
    public LocalDate getReportDate() {
        return reportDate;
    }

    /**
     * @return the list of symptoms recorded in this report
     */
    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    /**
     * @return the observation written by the patient
     */
    public String getPatientObservation() {
        return patientObservation;
    }

    /**
     * @return the observation written by the doctor
     */
    public String getDoctorObservation() {
        return doctorObservation;
    }

    /**
     * @return the CSV file path where captured signals are stored
     */
    public String getSignalsFilePath() {
        return signalsFilePath;
    }

    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", patientId=" + patientId +
                ", reportDate=" + reportDate +
                ", symptoms=" + symptoms +
                ", signalFile=" + signalsFilePath +
                ", csvFilePath='" + signalsFilePath + '\'' +
                '}';
    }
}