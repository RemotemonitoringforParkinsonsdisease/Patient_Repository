package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Doctor {
    private Integer doctorId;
    private String fullName;
    private LocalDate dob;
    private User user;
    private Set<User> patients;

    public Doctor(User user, int doctorId, String fullName, LocalDate dob) {
        this.user = user;
        this.doctorId = doctorId; //Or generated automatically from userId
        this.fullName = fullName;
        this.dob = dob;
        this.patients = new HashSet<>();
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
    public Set<User> getPatients() {
        return patients;
    }
    public void setPatients(Set<User> patients) {
        this.patients = patients;
    }
}
