package BITalino;

/**
 * Exception class used to represent errors related to BITalino operations.
 * Each exception is associated with a specific {@link BITalinoErrorTypes}
 * which contains both an error message and an integer error code.
 *
 * This exception extends {@link RuntimeException} and is thrown when issues
 * occur during connection, acquisition, decoding, or communication with the BITalino device.
 */
public class BITalinoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Numerical error code corresponding to the {@link BITalinoErrorTypes}.
     */
    public int code;

    /**
     * Creates a new BITalinoException based on a specific error type.
     * The message of the exception is taken from the error type description.
     *
     * @param errorType the specific BITalino error that occurred
     */
    public BITalinoException(BITalinoErrorTypes errorType) {
        super(errorType.getName());
        code = errorType.getValue();
    }
}
