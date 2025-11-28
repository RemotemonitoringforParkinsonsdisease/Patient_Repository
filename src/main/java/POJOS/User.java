package POJOS;

public class User {
    private Integer id;
    private String email;

    //Constructor de USER completo
    public User(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}