package POJOS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Report {
    private String reportId;
    private Patient patient;
    private LocalDate reportDate;
    private String patientObservation;//El texto que le manda el paciente al doctor
    private List<Symptoms> symptoms; //Sintomas de una lista cerrada
    private Set<Signal> signals; //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino
    private String doctorObservation;


    public Report(String reportId, Patient patient, LocalDate reportDate, String patientObservation, List<Symptoms> symptoms, Set<Signal> signals,  String doctorObservation) {
        this.reportId = reportId;
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }

    public Report(Patient patient, LocalDate reportDate, String patientObservation, List<Symptoms> symptoms, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.doctorObservation = doctorObservation;

    }
    public Report(Patient patient, LocalDate reportDate, List<Symptoms> symptoms, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, List<Symptoms> symptoms, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, List<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }

    public Report(Patient patient, LocalDate reportDate, String patientObservation, List<Symptoms> symptoms) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
    }
    public Report(Patient patient, LocalDate reportDate, List<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, List<Symptoms> symptoms) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getPatientObservation() {
        return patientObservation;
    }

    public void setPatientObservation(String patientObservation) {
        this.patientObservation = patientObservation;
    }

    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    public Set<Signal> getSignals() {
        return signals;
    }

    public void setSignals(Set<Signal> signals) {
        this.signals = signals;
    }

    public String getDoctorObservation() {
        return doctorObservation;
    }

    public void setDoctorObservation(String doctorObservation) {
        this.doctorObservation = doctorObservation;
    }
}