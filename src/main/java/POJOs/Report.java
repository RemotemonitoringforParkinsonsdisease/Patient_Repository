package POJOs;
import java.time.LocalDate;
import java.util.Set;

public class Report { //Cuando un paciente manda un report al doctor, hay q ver si el doctor se comunica a través del mismo report

    private Integer reportId;
    private Integer patientId;
    private Integer doctorId;
    private LocalDate reportDate;
    private String patientText; //El texto que le manda el paciente al doctor, podemos hacer booleans como dijo arecha también
    private Set<String> signal; //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino
    private boolean isSeen; //Ha sido visto por doctor, alomejor habría que implementar si ha sido respondido

    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, String patientText) { //Report solo con texto
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.patientText = patientText;
        this.isSeen = false;
    }
    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, Set<String> signal) { //Report solo con señal
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.signal = signal;
        this.isSeen = false;
    }
    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, String patientText, String signal) { //Report con ambas
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.patientText = patientText;
        this.isSeen = false;
    }
    public Integer getReportId() {
        return reportId;
    }
    public Integer getPatientId() {
        return patientId;
    }
    public String getPatientText() {
        return patientText;
    }
    public Set<String> getSignal() {
        return signal;
    }
    public boolean getIsSeen() {
        return isSeen;
    }
    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
}
