package bond;

/**
 * Represents a recoverable error caused by invalid user input.
 */
public class BondException extends Exception {

    private final String correction;

    /**
     * Creates an exception with the error cause and its correction.
     *
     * @param message Explanation of the error.
     * @param correction Instruction for correcting the error.
     */
    public BondException(String message, String correction) {
        super(message);
        this.correction = correction;
    }

    /**
     * Returns the instruction for correcting the error.
     *
     * @return Instruction for correcting the error.
     */
    public String getCorrection() {
        return correction;
    }
}
