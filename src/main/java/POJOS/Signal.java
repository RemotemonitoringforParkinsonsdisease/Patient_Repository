package POJOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Signal {
    private Integer signalId;
    private SignalType signalType;
    private List<Integer> values;
    private Integer samplingRate;

    public Signal(SignalType signalType) {
        this.signalType = signalType;
        this.values = new ArrayList<>();
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public List<Integer> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signal signal = (Signal) o;
        return samplingRate == signal.samplingRate && Objects.equals(signalId, signal.signalId) && signalType == signal.signalType && Objects.equals(values, signal.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signalId, signalType, values, samplingRate);
    }

    @Override
    public String toString() {
        return  signalType + " " + "[" + samplingRate + " Hz]";
    }

}
