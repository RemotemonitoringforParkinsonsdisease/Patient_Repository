package POJOS;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Patient extends User{
    private String password;
    private Doctor doctor;
    private LocalDate dob;
    private Set <Report> reports = new HashSet<>();

    //Constructor para mandar/recuperar un paciente completo a/desde SERVER
    public Patient(String id, String email, String fullName, String password, Doctor doctor, LocalDate dob, Set<Report> reports) {
        super(id, email, fullName);
        this.password = password;
        this.doctor = doctor;
        this.dob = dob;
        this.reports = reports;
    }

    //Constructor para REGISTRARSE
    public Patient(String email, String password, String fullName, LocalDate dob) {
        super(email, fullName);
        this.dob = dob;
        this.password = password;
    }

    //Constructor para LOGIN
    public Patient(String email, String password) {
        super(email);
        this.password = password;
    }
}