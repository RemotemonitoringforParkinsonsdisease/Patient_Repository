package POJOS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the system, including their personal information,
 * associated user and doctor identifiers, authentication credentials,
 * date of birth, and a list of assigned medical reports.
 *
 * A patient may be reconstructed from network data or created locally during
 * the registration process.
 */
public class Patient {
    private Integer patientId;
    private Integer userId;
    private Integer doctorId;
    private String patientPassword; //TODO encriptar
    private LocalDate dob;
    private List<Report> reports = new ArrayList<>();
    private String fullName;

    /**
     * Full constructor used when all patient information is known, typically
     * when reconstructing a patient received from the server.
     *
     * @param patientId       the unique identifier of the patient
     * @param userId          the user account identifier associated with the patient
     * @param doctorId        the identifier of the doctor assigned to the patient
     * @param patientPassword the patient's password
     * @param fullName        the full name of the patient
     * @param dob             the date of birth of the patient
     * @param reports         the list of medical reports belonging to the patient
     */
    public Patient(Integer patientId, Integer userId, Integer doctorId, String patientPassword, String fullName, LocalDate dob, List<Report> reports) {
        this.patientId = patientId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.patientPassword = patientPassword;
        this.fullName = fullName;
        this.dob = dob;
        this.reports = reports;
    }

    /**
     * Constructor used during patient registration, when only basic information
     * (password, full name, and date of birth) is required.
     *
     * @param patientPassword the password chosen by the patient
     * @param fullName        the full name of the patient
     * @param dob             the date of birth of the patient
     */
    public Patient(String patientPassword, String fullName, LocalDate dob) {
        this.patientPassword = patientPassword;
        this.fullName = fullName;
        this.dob = dob;
    }

    /**
     * @return the unique identifier of the patient
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * @return the doctor identifier associated with this patient
     */
    public Integer getDoctorId() {
        return doctorId;
    }

    /**
     * @return the patient's authentication password
     */
    public String getPatientPassword() {
        return patientPassword;
    }

    /**
     * @return the full name of the patient
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return the date of birth of the patient
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * @return the list of medical reports assigned to the patient
     */
    public List<Report> getReports() {
        return reports;
    }
}