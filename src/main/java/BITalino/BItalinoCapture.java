package BITalino;

import POJOS.Signal;
import POJOS.SignalType;

/**
 * Provides functionality for capturing physiological signals from a BITalino device.
 * This class manages the connection, acquisition, and conversion of raw BITalino
 * frames into {@link Signal} objects.
 *
 * A specific signal type (ECG, EMG, EDA, ACC) is mapped to a BITalino analog input
 * channel, and a fixed number of samples are acquired and returned.
 */
public class BItalinoCapture {
    public BItalinoCapture() {
    }

    /**
     * Captures a physiological signal from a BITalino device according to the provided signal type.
     * The method establishes a Bluetooth connection, starts acquisition on the appropriate channel,
     * reads 100 samples, converts them into integer values, and returns a {@link Signal} object.
     *
     * @param type the type of physiological signal to capture (EMG, ECG, EDA, ACC)
     * @return a {@link Signal} containing the captured values, or null if an error occurs
     */
    public Signal captureBitalinoSignal(SignalType type) {
        System.out.println("\nConnecting to BITalino...");
        String mac = "BC:33:AC:AB:AE:E5";
        //String mac = "98:D3:91:FD:69:4F";

        int channel = mapSignalTypeToChannel(type);
        Signal signal = new Signal(type);

        try {
            BITalino device = new BITalino();
            device.open(mac, 100);
            device.start(new int[]{channel});
            System.out.println("-> Recording " + type + " signal...");

            Frame[] frames = device.read(100);
            for (Frame frame : frames) {
                int value = frame.analog[0];
                signal.getValues().add(value);
            }

            device.stop();
            device.close();

            System.out.println("-> Signal captured successfully!\n");
            return signal;

        } catch (Exception e) {
            System.out.println("-> Error capturing " + type + " signal: " + e.getMessage());
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Maps a {@link SignalType} to the corresponding BITalino analog channel index.
     *
     * @param type the type of signal to map
     * @return the BITalino channel index associated with the signal type
     */
    private int mapSignalTypeToChannel(SignalType type) {
        switch (type) {
            case EMG:
                return 0;
            case ECG:
                return 1;
            case EDA:
                return 2;
            case ACC:
                return 3;
            default:
                throw new IllegalArgumentException("-> Unsupported SignalType: " + type);
        }
    }
}