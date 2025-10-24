package POJOs;

public class User {
    private String email;
    private String password; //TODO: Encriptar
    private String role;
    private Integer userId;
    //IMPORTANTE: El id puede ser tanto de Doctor como de Paciente dependiendo del rol, por lo que podríamos hacerlo como String, y añadir una letra delante (P/D)
    //Otra forma para mantenerlo como int es tener un id diferente de usuario al de paciente/doctor
    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Integer getUserId() {
        return userId;
    }
}
