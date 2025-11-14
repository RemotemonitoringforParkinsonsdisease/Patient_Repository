package POJOS;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Patient extends User{
    private String password;
    private Doctor doctor;
    private LocalDate dob;
    private Set <Report> reports = new HashSet<>();

    //Constructor parea crear paciente desde Servidor
    public Patient(String id, String email, String fullName, String password,  LocalDate dob, Doctor doctor, Set<Report> reports) {
        super(id, email, fullName);
        this.password = password;
        this.doctor = doctor;
        this.dob = dob;
        this.reports = reports;
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

}