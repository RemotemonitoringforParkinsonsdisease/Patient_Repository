package POJOS;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

public class Report {
    private String reportId;
    private Integer patientId;
    private Integer doctorId;
    private LocalDate reportDate;
    private String patientObservation;//El texto que le manda el paciente al doctor
    private String doctorObservation;
    private Set<Symptoms> symptoms; //Sintomas de una lista cerrada
    private Set<Signal> signals; //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino

    public Report(String reportId, Integer patientId, Integer doctorId, LocalDate reportDate, String patientObservation, String doctorObservation, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }

    /*
    public String createReportId() {
        String identifier = "R";
        final int idLength = 9;
        for (int i = 0; i < idLength; i++) {
            Random rand = new Random();
            identifier += rand.nextInt(10);
        }
        if(idList.contains(identifier)){
            return createId();
        }
        return identifier;
    }
     */
}
