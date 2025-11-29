package POJOS;

/**
 * Enumeration representing the different types of physiological signals
 * that can be captured from a BITalino device.
 *
 * Each value corresponds to a specific sensor modality used in medical
 * and physiological monitoring.
 */
public enum SignalType {
    /** Electrodermal activity signal (skin conductance). */
    EDA,

    /** Electromyography signal (muscle electrical activity). */
    EMG,

    /** Accelerometer signal (physical movement or acceleration). */
    ACC,

    /** Electrocardiography signal (heart electrical activity). */
    ECG,
}