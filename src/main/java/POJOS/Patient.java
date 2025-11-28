package POJOS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Patient {
    private Integer patientId;
    private Integer userId;
    private Integer doctorId;
    private String patientPassword; //TODO encriptar
    private LocalDate dob;
    private List<Report> reports = new ArrayList<>();
    private String fullName;

    //Constructor entero de PATIENT
    public Patient(Integer patientId, Integer userId, Integer doctorId, String patientPassword, String fullName, LocalDate dob, List<Report> reports) {
        this.patientId = patientId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.patientPassword = patientPassword;
        this.fullName = fullName;
        this.dob = dob;
        this.reports = reports;
    }

    public Patient(String patientPassword, String fullName, LocalDate dob) {
        this.patientPassword = patientPassword;
        this.fullName = fullName;
        this.dob = dob;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public List<Report> getReports() {
        return reports;
    }

}