package POJOS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Report {
    private Integer reportId;
    private Integer patientId;
    private LocalDate reportDate;
    private List<Signal> signals;
    private List<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;

    public Report(Integer reportId, Integer patientId, LocalDate reportDate, List<Signal> signals, List<Symptoms> symptoms, String patientObservation, String doctorObservation) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.signals = signals;
        this.symptoms = symptoms;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }

    public Report(Integer patientId, LocalDate reportDate, String patientObservation, String doctorObservation, List<Symptoms> symptoms, List<Signal> signals) {
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }

    public Report(Integer reportId, Integer patientId, LocalDate reportDate, String patientObservation, String doctorObservation) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
    }

    public Integer getReportId() { return reportId; }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public List<Signal> getSignals() {
        return signals;
    }

    public void setSignals(List<Signal> signals) {
        this.signals = signals;
    }

    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    public String getPatientObservation() {
        return patientObservation;
    }

    public void setPatientObservation(String patientObservation) {
        this.patientObservation = patientObservation;
    }

    public String getDoctorObservation() {
        return doctorObservation;
    }

    public void setDoctorObservation(String doctorObservation) {
        this.doctorObservation = doctorObservation;
    }

    public Signal getSignalByType(SignalType signalType){
        for(Signal signal : signals){
            if(signal.getSignalType() ==  signalType){
                return signal;
            }
        }
        return null;
    }
}