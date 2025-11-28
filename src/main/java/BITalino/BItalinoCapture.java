package BITalino;

import POJOS.Signal;
import POJOS.SignalType;

public class BItalinoCapture {
    public BItalinoCapture() {
    }

    public Signal captureBitalinoSignal(SignalType type) {
        //TODO mirar excepciones posibles
        System.out.println("\nConnecting to BITalino...");
        String mac = "BC:33:AC:AB:AE:E5";  //Cambiar por el nuestro
        //String mac = "98:D3:91:FD:69:4F";  //Cambiar por el nuestro

        //Canal del BItalino real
        int channel = mapSignalTypeToChannel(type);
        Signal signal = new Signal(type);
        try {

            BITalino device = new BITalino();

            //Abrir conexión
            device.open(mac, 100);

            //Iniciar adquisición SOLO del canal elegido
            device.start(new int[]{channel});

            System.out.println("Recording " + type + " signal...");

            //Lee 100 muestras (nuestro samplingRate)
            Frame[] frames = device.read(100);

            //Guarda valores en la señal
            //TODO revisar como pasar datos a enteros desde BITalino
            for (Frame frame : frames) {
                int value = frame.analog[0];
                signal.getValues().add(value);
            }

            device.stop();
            device.close();

            System.out.println("Signal captured successfully!\n");
            return signal;

        } catch (Exception e) {
            System.out.println("Error capturing " + type + " signal: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    //Los tipos de señal están asignados a los canales de la placa de bitalino
    private int mapSignalTypeToChannel(SignalType type) {
        switch (type) {
            case EMG: return 0;
            case ECG: return 1;
            case EDA: return 2;
            case ACC: return 3; //eje X, por ejemplo
            default: return 444; //Gestionar esto
        }
    }
}