package POJOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private Integer id;
    private String email;

    //Constructor de USER completo
    public User(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String email){
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}