package POJOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a physiological signal captured from a BITalino device.
 * A signal has a specific {@link SignalType}, a collection of numerical
 * values representing the sampled data, an optional sampling rate,
 * and an internal identifier.
 *
 * Signals are used to generate CSV files and are included inside
 * medical reports.
 */
public class Signal {
    private Integer signalId;
    private SignalType signalType;
    private List<Integer> values;
    private Integer samplingRate;

    /**
     * Constructor used when creating a new signal with a specific type.
     * The list of values is initialized empty and will be filled during capture.
     *
     * @param signalType the type of signal (ECG, EDA, EMG, ACC, etc.)
     */
    public Signal(SignalType signalType) {
        this.signalType = signalType;
        this.values = new ArrayList<>();
    }

    /**
     * @return the type of this signal
     */
    public SignalType getSignalType() {
        return signalType;
    }

    /**
     * @return the list of numeric samples captured for this signal
     */
    public List<Integer> getValues() {
        return values;
    }

    /**
     * Compares this signal with another object. Two signals are considered equal
     * if their IDs, types, values, and sampling rates are the same.
     *
     * @param o the object to compare with
     * @return true if both objects represent the same signal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signal signal = (Signal) o;
        return samplingRate == signal.samplingRate && Objects.equals(signalId, signal.signalId) && signalType == signal.signalType && Objects.equals(values, signal.values);
    }

    /**
     * @return a hash code computed from all signal fields
     */
    @Override
    public int hashCode() {
        return Objects.hash(signalId, signalType, values, samplingRate);
    }

    /**
     * @return a readable representation of the signal showing its type and sampling rate
     */
    @Override
    public String toString() {
        return signalType + " " + "[" + samplingRate + " Hz]";
    }
}
