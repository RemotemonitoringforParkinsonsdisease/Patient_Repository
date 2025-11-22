package POJOS;

public class Doctor {
    private Integer doctorId;
    private Integer userId;
    private String fullName;

    public Doctor(Integer doctorId, Integer userId, String fullName) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.fullName = fullName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
