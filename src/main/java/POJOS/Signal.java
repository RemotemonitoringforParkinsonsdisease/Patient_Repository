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

    public Signal(Integer signalId, SignalType signalType) {
        this.signalId = signalId;
        this.signalType = signalType;
    }

    public Signal(Integer signalId, SignalType signalType, List<Integer> values) {
        this.signalId = signalId;
        this.signalType = signalType;
        this.values = values;
    }


    public Integer getSignalId() {
        return signalId;
    }

    public void setSignalId(Integer signalId) {
        this.signalId = signalId;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }


    public ArrayList<Integer> getSignalValues(int samplingRate) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int j = 0; j < samplingRate; j++) {
            int blockSize = samplingRate;
            for (int i = 0; i < blockSize; i++) {
                int value = j * blockSize + i;
                result.add(values.get(value));
            }
        }
        return result;
    }

    public String intValuesToString() {
        StringBuilder message = new StringBuilder();
        String separator = " ";

        for (int i = 0; i < values.size(); i++) {
            message.append(values.get(i));
            if (i < values.size() - 1) {
                message.append(separator);
            }
        }
        return message.toString();
    }


    public List<Integer> stringToIntValues(String str) {
        values.clear(); // Limpiamos la lista antes de agregar nuevos valores.
        String[] tokens = str.split(" "); // Dividimos el String por el espacio.
        int size = tokens.length;
        if(size>2) {
            for (int i = 0; i < size; i++) {
                try {
                    values.add(Integer.parseInt(tokens[i])); // Convertimos cada fragmento a Integer y lo agregamos a la ArrayList.
                } catch (NumberFormatException e) {
                    // Manejo de error si algún valor no es un Integer válido.
                    System.out.println("Error al convertir el valor: " + tokens[i]);
                }
            }
        }
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
