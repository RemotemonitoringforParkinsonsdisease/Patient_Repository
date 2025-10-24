package POJOs;

import java.time.LocalDate;
import java.util.Set;

public class Patient extends User{
    private String password; //TODO: Encriptar;
    private Doctor doctor;
    private LocalDate dob;
    private Set <Report> reports;

    public Patient(String email, String password, String fullName, LocalDate dob) {
        super(email, fullName);
        this.password = password;
        this.dob = dob;
    }
}

