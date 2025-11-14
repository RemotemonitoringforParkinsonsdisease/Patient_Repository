package POJOS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Report {
    private static List<String> idList = new ArrayList<String>();
    private String reportId;
    private Patient patient;
    private LocalDate reportDate;
    private String patientObservation;//El texto que le manda el paciente al doctor
    private String doctorObservation;
    private Set<Symptoms> symptoms; //Sintomas de una lista cerrada
    private Set<Signal> signals; //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino


    public Report(Patient patient, LocalDate reportDate, String patientObservation, String doctorObservation, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }

    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, String doctorObservation, Set<Symptoms> symptoms) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String doctorObservation, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }


}
