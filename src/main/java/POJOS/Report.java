package POJOS;

import java.time.LocalDate;
import java.util.List;

public class Report {
    private Integer reportId;
    private Integer patientId;
    private LocalDate reportDate;
    private List<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;
    private String signalsFilePath;

    public Report(Integer reportId, Integer patientId, LocalDate reportDate, String signalsFilePath, List<Symptoms> symptoms, String patientObservation, String doctorObservation) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.signalsFilePath = signalsFilePath;
        this.symptoms = symptoms;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }

    public Report(Integer patientId, LocalDate reportDate, String patientObservation, String doctorObservation, List<Symptoms> symptoms, String signalsFilePath) {
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signalsFilePath = signalsFilePath;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    public String getPatientObservation() {
        return patientObservation;
    }

    public String getDoctorObservation() {
        return doctorObservation;
    }

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