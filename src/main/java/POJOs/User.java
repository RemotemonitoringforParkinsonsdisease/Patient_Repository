package POJOs;

public class User {
    private String email;
    private String password; //TODO: Encriptar
    private String role;
    private Integer userId;
    //Yo haría que Patient y Doctor hereden de User, así no hay que andar pasando User como atributo
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
