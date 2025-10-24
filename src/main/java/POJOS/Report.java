package POJOS;

import java.time.LocalDate;
import java.util.Set;

public class Report {
    private Integer reportId;
    private Integer patientId;
    private Integer doctorId;
    private LocalDate reportDate;
    private String patientText; //El texto que le manda el paciente al doctor, podemos hacer booleans como dijo arecha también
    private Set<String> signal; //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino

    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, String patientText) { //Report solo con texto
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.patientText = patientText;
    }
    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, Set<String> signal) { //Report solo con señal
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.signal = signal;
    }
    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, String patientText, String signal) { //Report con ambas
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.patientText = patientText;
    }

}
