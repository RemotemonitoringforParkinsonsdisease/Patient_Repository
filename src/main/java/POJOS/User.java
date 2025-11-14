package POJOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private String id;
    private String email;
    private String fullName;

    //Constructor para REGISTRO y LOGIN
    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    //Constructor de USER completo
    public User(String id, String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.id = id;
    }

    //Constructor para LOGIN (antes de enviar a servidor)
    public User(String email){
        this.email = email;
    }


}