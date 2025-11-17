package POJOS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Signal {
    private String signalId;
    private SignalType signalType;
    private List<Float> values;
    private final int samplingRate = 100;
    private String signalFilename;


    public Signal(SignalType signalType, String signalId){
        this.values = new LinkedList<>();
        this.signalId = signalId;
        this.signalType = signalType;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public int getSamplingRate() {
        return samplingRate;
    }
    public void storeSignalInFile() {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String ruta=null;
        try {
            if(this.signalType==SignalType.EDA) {
                ruta = "MeasurementsEDA\\" + signalFilename;

            }else{
                if(this.signalType==SignalType.EMG) {
                    ruta = "MeasurementsEMG\\" + signalFilename;
                }else{
                    if(this.signalType==SignalType.ECG) {
                        ruta = "MeasurementsECG\\" + signalFilename;
                    }else{
                        if(this.signalType==SignalType.ACC) {
                            ruta = "MeasurementsACC\\" + signalFilename;
                        }
                    }

                }
            }
            String contenido = getSignalValues(samplingRate).toString();
            File file = new File(ruta);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(contenido);

        } catch (IOException ex) {
            Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public LinkedList<Float> getSignalValues(int samplingRate) {
        LinkedList<Float> result = new LinkedList<>();
        for (int j = 0; j < samplingRate; j++) {
            int blockSize = samplingRate;
            // Si necesitas esta información visual, puedes guardarla en otro lugar.
            for (int i = 0; i < blockSize; i++) {
                int value = j * blockSize + i;
                result.add(values.get(value));  // Agregar los valores a la lista.
            }
        }
        return result;
    }

    public String valuesToString() {
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

    public List<Float> stringToValues(String str) {
        values.clear(); // Limpiamos la lista antes de agregar nuevos valores.
        String[] tokens = str.split(" "); // Dividimos el String por el espacio.
        int size = tokens.length;
        if(size>2) {
            for (int i = 0; i < size; i++) {
                try {
                    values.add(Float.parseFloat(tokens[i])); // Convertimos cada fragmento a Float y lo agregamos a la LinkedList.
                } catch (NumberFormatException e) {
                    // Manejo de error si algún valor no es un Integer válido.
                    System.out.println("Error al convertir el valor: " + tokens[i]);
                }
            }
        }
        return values;
    }
    /**
     * Adds a list of new signal values to the existing signal.
     * @param values a list of new signal values to add.
     */
    public void addValues(LinkedList<Float> values){
        this.values.addAll(values);
    }


}
