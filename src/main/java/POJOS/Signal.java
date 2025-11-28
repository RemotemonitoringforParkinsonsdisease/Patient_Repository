package POJOS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Signal {
    private Integer signalId;
    private SignalType signalType;
    private List<Integer> values;
    private Integer samplingRate;

    public Signal(SignalType signalType) {
        this.signalType = signalType;
        this.values = new ArrayList<>();
    }

    public Signal(Integer signalId, SignalType signalType, Integer samplingRate, List<Integer> values) {
        this.signalId = signalId;
        this.signalType = signalType;
        this.samplingRate = samplingRate;
        this.values = values;
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
