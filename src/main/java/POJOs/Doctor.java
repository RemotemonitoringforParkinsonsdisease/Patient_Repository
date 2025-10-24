package POJOs;

import java.util.Set;

public class Doctor {
    private Integer doctorId;
    private String fullName;
    private User user;
    private Set<Patient> patients;

    public Doctor(User user, int doctorId, String fullName) {
        this.user = user;
        this.doctorId = doctorId; //Or generated automatically from userId
        this.fullName = fullName;
    }
    public Integer getDoctorId() {
        return doctorId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public Set<Patient> getPatients() {
        return patients;
    }
    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }
}