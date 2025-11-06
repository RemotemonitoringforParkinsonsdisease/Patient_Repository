package BITalino;

public class BITalinoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BITalinoException(BITalinoErrorTypes errorType) {
        super(errorType.getName());
        code = errorType.getValue();
    }

    public int code;
}
