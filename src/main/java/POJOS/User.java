package POJOS;

/**
 * Represents a basic user in the system. A user has an identifier
 * and an associated email address, which is typically used for login
 * and authentication purposes.
 *
 * This class is commonly extended or linked to more specific entities
 * such as {@link Patient} or doctor.
 */
public class User {
    private Integer id;
    private String email;

    /**
     * Full constructor used when creating or reconstructing a user.
     *
     * @param id    the unique identifier of the user
     * @param email the email address associated with the user
     */
    public User(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    /**
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }
}