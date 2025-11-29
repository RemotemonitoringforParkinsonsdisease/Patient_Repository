package BITalino;

/**
 * Represents a single data frame returned by {@link BITalino#read(int)}.
 * A frame contains the sequence number, digital inputs/outputs, and
 * analog signal values captured from the BITalino device.
 *
 * Frames are used to reconstruct physiological signal samples during acquisition.
 */
public class Frame {

    /**
     * Frame sequence number (0–15).
     * This value increments by 1 for each consecutive frame and overflows to 0 after 15
     * since it is represented as a 4-bit number.
     *
     * The sequence number can be used to detect dropped frames during data transmission.
     */
    public int seq;

    /**
     * Array containing analog input values acquired from the device.
     * <ul>
     *   <li>Channels 0–3: values range 0–1023</li>
     *   <li>Channels 4–5: values range 0–63</li>
     * </ul>
     *
     * BITalino supports up to 6 analog channels.
     */
    public int [] analog = new int[6];

    /**
     * Array containing the states of the digital ports.
     * <ul>
     *   <li>On original BITalino: I1, I2, I3, I4</li>
     *   <li>On BITalino (r)evolution: I1, I2, O1, O2</li>
     * </ul>
     *
     * Each value is 0 (low) or 1 (high).
     */
    public int [] digital = new int[4];
}
