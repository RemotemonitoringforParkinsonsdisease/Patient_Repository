package POJOs;

import java.time.LocalDate;
import java.util.Set;

public class Patient {
    private Integer patientId;
    private String fullName;
    private LocalDate dob;
    private Doctor doctor;
    private Set <Report> reports;
    private User user;

    public Patient(User user, int patientId, String fullName, LocalDate dob) {
        this.user = user;
        this.patientId = patientId; //Or generated automatically from userId
        this.fullName = fullName;
        this.dob = dob;
    }

    public Integer getPatientId() {
        return patientId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public Set <Report> getReports() {
        return reports;
    }
    public void setReports(Set <Report> reports) {
        this.reports = reports;
    }
}

