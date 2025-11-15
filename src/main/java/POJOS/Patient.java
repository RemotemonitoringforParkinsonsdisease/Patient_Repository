package POJOS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Patient extends User{
    private String password;
    private Doctor doctor;
    private LocalDate dob;
    private List<Report> reports = new ArrayList<>();

    //Constructor parea crear paciente desde Servidor
    public Patient(String id, String email, String fullName, String password,  LocalDate dob, Doctor doctor, List<Report> reports) {
        super(id, email, fullName);
        this.password = password;
        this.doctor = doctor;
        this.dob = dob;
        this.reports = reports;
    }

    //Controctor para RecieveDataViaNetwork

    public Patient (String id, String email, String fullName, String password, LocalDate dob) {
        super(id, email, fullName);
        this.password = password;
        this.dob = dob;
    }

    //Constructor para REGISTRARSE (crea el paciente que se envia al servidor)
    public Patient(String email, String fullName, String password, LocalDate dob) {
        super(email, fullName);
        this.dob = dob;
        this.password = password;
    }

    //Constructor para LOGIN antes de envíar a servidor
    public Patient(String email, String password) {
        super(email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}