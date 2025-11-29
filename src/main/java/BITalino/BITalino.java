package BITalino;
/**
 \mainpage

 The %BITalino Java API (available at http://www.bitalino.com/API/API_Java.zip) is a BlueCove-powered library which enables Java applications to communicate with a %BITalino device through a simple interface.
 The API is composed of implementation files (BITalino.java and Frame.java), a set of auxiliary files to handle errors and exception (BITalinoErrorTypes.java and BITalinoException.java), and an auxiliary file for device discovery (DeviceDiscoverer.java).
 A sample test application in Java (test.java()) is also provided.

 This code base has been designed to enable direct Bluetooth connection using the device Bluetooth MAC address (Windows and Mac OS);

 The API exposes the class BITalino, and each instance of this class represents a connection to a %BITalino device. The connection is established with the BITalino.open(...) method and released with the BITalino.close() method.

 \section sampleapp About the sample application

 The sample application (test.java) creates an instance to a %BITalino device.
 Then it opens the connection, starts acquiring channels 1 and 5 on the device at 1000 Hz, reads 300 samples and toggles the digital outputs (green LED should turn on). Afterwards, the acquisition is stopped and the connection closed.

 The BITalino.open() method must be used to connect to the device.
 The string passed to the constructor should be a Bluetooth MAC address including the ':' delimiter (to use the sample application you must change the MAC address therein).

 \section configuration Configuring the IDE

 To use the library and sample application:
 - launch your IDE;
 - make sure that you have the Eclipse Integration Plugin installed (if you haven't, go to "File > Settings > Plugins > Install JetBrains Plugin…" and install the plugin);
 - to import the Java API project go to "File > New… > From Existing Sources…", then select your project folder in the dialog window, choose the .project file and press "OK";
 - make sure that the “Select Eclipse projects directory:” field is the actual path of your project folder and then press "Next";
 - if unselected, select the project “API_BITalino” in order to import it and press "Next";
 - select your project SDK and press "Finish";
 - as the file .userlibraries is not available in the project, when the IDE asks for it just press "Cancel";
 - now that the project has been successfully imported, go to "File > Project Structure…", under "Project Settings", select "Modules" and click on API_BITALINO;
 - at this point, in the "Dependencies" tab, make sure that the Module SDK is the same that you choose to your project;
 - then remove the “Referenced Libraries” by selecting and pressing the minus icon on the left of the window;
 - press "Apply" and then "OK" in the bottom of the dialog;
 - under the API_BITALINO folder, select the "src" folder.
 - click on the test.java file with the right button of your mouse and select the “Run ‘test.main()’”.
 */

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * Represents a BITalino device, allowing a Java application to connect, configure,
 * start and stop acquisition, and read frames from the device.
 *
 * Each instance of this class manages its own Bluetooth connection to a
 * BITalino device through the BlueCove library.
 *
 * The connection is opened using {@link #open(String, int)} and released with {@link #close()}.
 */
public class BITalino {

    /**
     * List of analog channels selected for acquisition.
     */
    private int[] analogChannels = null;

    /**
     * Number of bytes expected in each frame sent from the device.
     */
    private int number_bytes = 0;

    /**
     * Bluetooth socket connection established with the device.
     */
    private StreamConnection hSocket = null;

    /**
     * Input data stream from the BITalino device.
     */
    private DataInputStream iStream = null;

    /**
     * Output data stream to the BITalino device.
     */
    private DataOutputStream oStream = null;

    public BITalino() {
    }

    /**
     * Connects to a BITalino device using its Bluetooth MAC address.
     *
     * @param macAdd       the MAC address of the device ("xx:xx:xx:xx:xx:xx")
     * @param samplingRate sampling rate in Hz (1, 10, 100 or 1000)
     *
     * @throws BITalinoException if the MAC address is invalid or sampling rate is incorrect
     * @throws IllegalArgumentException if an invalid parameter is provided
     * @throws IOException if connection cannot be established
     * @throws SecurityException if permissions are insufficient
     */
    public void open(String macAdd, int samplingRate) throws BITalinoException {
        if (macAdd.split(":").length > 1) {
            macAdd = macAdd.replace(":", "");
        }
        if (macAdd.length() != 12) {
            throw new BITalinoException(BITalinoErrorTypes.MACADDRESS_NOT_VALID);
        }

        try {
            hSocket = (StreamConnection) Connector.open("btspp://" + macAdd + ":1", Connector.READ_WRITE);
            iStream = hSocket.openDataInputStream();
            oStream = hSocket.openDataOutputStream();
            Thread.sleep(2000);

        } catch (Exception e) {
            close();
        }

        try {
            int variableToSend = 0;

            switch (samplingRate) {
                case 1000:
                    variableToSend = 0x3;
                    break;
                case 100:
                    variableToSend = 0x2;
                    break;
                case 10:
                    variableToSend = 0x1;
                    break;
                case 1:
                    variableToSend = 0x0;
                    break;
                default:
                    close();
            }
            variableToSend = (variableToSend << 6) | 0x03;
            Write(variableToSend);
        } catch (Exception e) {
            throw new BITalinoException(BITalinoErrorTypes.SAMPLING_RATE_NOT_DEFINED);
        }
    }

    /**
     * Starts signal acquisition for the selected analog channels.
     *
     * @param anChannels array of channel indexes (0–5) to acquire
     *
     * @throws BITalinoException if channels are invalid or device is not connected
     */
    public void start(int[] anChannels) throws Throwable {
        analogChannels = anChannels;
        if (analogChannels.length > 6 | analogChannels.length == 0) {
            throw new BITalinoException(BITalinoErrorTypes.ANALOG_CHANNELS_NOT_VALID);
        } else {
            int bit = 1;
            for (int i : anChannels) {
                if (i < 0 | i > 5) {
                    throw new BITalinoException(BITalinoErrorTypes.ANALOG_CHANNELS_NOT_VALID);
                } else {
                    bit = bit | 1 << (2 + i);
                }
            }
            int nChannels = analogChannels.length;
            if (nChannels <= 4) {
                number_bytes = (int) Math.ceil(((float) 12 + (float) 10 * nChannels) / 8);
            } else {
                number_bytes = (int) Math.ceil(((float) 52 + (float) 6 * (nChannels - 4)) / 8);
            }
            try {
                Write(bit);
            } catch (Exception e) {
                throw new BITalinoException(BITalinoErrorTypes.BT_DEVICE_NOT_CONNECTED);
            }
        }
    }

    /**
     * Stops the ongoing signal acquisition.
     *
     * @throws BITalinoException if the device is not connected
     */
    public void stop() throws BITalinoException {
        try {
            Write(0);
        } catch (Exception e) {
            throw new BITalinoException(BITalinoErrorTypes.BT_DEVICE_NOT_CONNECTED);
        }
    }

    /**
     * Closes the connection to the device and releases all resources.
     *
     * @throws BITalinoException if the device is not connected or cannot be closed
     */
    public void close() throws BITalinoException {
        try {
            hSocket.close();
            iStream.close();
            oStream.close();
            hSocket = null;
            iStream = null;
            oStream = null;
        } catch (Exception e) {
            throw new BITalinoException(BITalinoErrorTypes.BT_DEVICE_NOT_CONNECTED);
        }
    }

    /**
     * Sends a raw command byte to the BITalino device.
     *
     * @param data byte representing the command
     *
     * @throws BITalinoException if communication is lost
     */
    public void Write(int data) throws BITalinoException {
        try {
            oStream.write(data);
            oStream.flush();
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new BITalinoException(BITalinoErrorTypes.LOST_COMMUNICATION);
        }
    }

    /**
     * Decodes a raw byte buffer into a frame array.
     *
     * @param buffer the byte buffer read from the device
     * @return an array containing the decoded frame(s)
     *
     * @throws IOException if reading fails
     * @throws BITalinoException if decoding fails
     */
    private Frame[] decode(byte[] buffer) throws IOException, BITalinoException {
        try {
            Frame[] frames = new Frame[1];
            int j = (number_bytes - 1), i = 0, CRC = 0, x0 = 0, x1 = 0, x2 = 0, x3 = 0, out = 0, inp = 0;
            CRC = (buffer[j - 0] & 0x0F) & 0xFF;

            for (int bytes = 0; bytes < number_bytes; bytes++) {
                for (int bit = 7; bit > -1; bit--) {
                    inp = (buffer[bytes]) >> bit & 0x01;
                    if (bytes == (number_bytes - 1) && bit < 4) {
                        inp = 0;
                    }
                    out = x3;
                    x3 = x2;
                    x2 = x1;
                    x1 = out ^ x0;
                    x0 = inp ^ out;
                }
            }

            if (CRC == ((x3 << 3) | (x2 << 2) | (x1 << 1) | x0)) {
                frames[i] = new Frame();
                frames[i].seq = (short) ((buffer[j - 0] & 0xF0) >> 4) & 0xf;
                frames[i].digital[0] = (short) ((buffer[j - 1] >> 7) & 0x01);
                frames[i].digital[1] = (short) ((buffer[j - 1] >> 6) & 0x01);
                frames[i].digital[2] = (short) ((buffer[j - 1] >> 5) & 0x01);
                frames[i].digital[3] = (short) ((buffer[j - 1] >> 4) & 0x01);

                switch (analogChannels.length - 1) {

                    case 5:
                        frames[i].analog[5] = (short) ((buffer[j - 7] & 0x3F));
                    case 4:

                        frames[i].analog[4] = (short) ((((buffer[j - 6] & 0x0F) << 2) | ((buffer[j - 7] & 0xc0) >> 6)) & 0x3f);
                    case 3:

                        frames[i].analog[3] = (short) ((((buffer[j - 5] & 0x3F) << 4) | ((buffer[j - 6] & 0xf0) >> 4)) & 0x3ff);
                    case 2:

                        frames[i].analog[2] = (short) ((((buffer[j - 4] & 0xff) << 2) | (((buffer[j - 5] & 0xc0) >> 6))) & 0x3ff);
                    case 1:

                        frames[i].analog[1] = (short) ((((buffer[j - 2] & 0x3) << 8) | (buffer[j - 3]) & 0xff) & 0x3ff);
                    case 0:

                        frames[i].analog[0] = (short) ((((buffer[j - 1] & 0xF) << 6) | ((buffer[j - 2] & 0XFC) >> 2)) & 0x3ff);
                }
            } else {
                frames[i] = new Frame();
                frames[i].seq = -1;
            }
            return frames;
        } catch (Exception e) {
            throw new BITalinoException(BITalinoErrorTypes.INCORRECT_DECODE);
        }
    }

    /**
     * Reads a given number of frames from the BITalino device.
     *
     * @param nSamples number of frames to read
     * @return an array of acquired frames
     *
     * @throws BITalinoException if communication fails
     */
    public Frame[] read(int nSamples) throws BITalinoException {

        try {
            Frame[] frames = new Frame[nSamples];
            byte[] buffer = new byte[number_bytes];
            byte[] bTemp = new byte[1];
            int i = 0;
            while (i < nSamples) {
                iStream.readFully(buffer, 0, number_bytes);
                Frame[] f = decode(buffer);
                if (f[0].seq == -1) {
                    while (f[0].seq == -1) {
                        iStream.readFully(bTemp, 0, 1);
                        for (int j = number_bytes - 2; j >= 0; j--) {
                            buffer[j + 1] = buffer[j];
                        }
                        buffer[0] = bTemp[0];
                        f = decode(buffer);
                    }
                    frames[i] = f[0];
                } else {

                    frames[i] = f[0];
                }
                i++;
            }
            return frames;
        } catch (Exception e) {
            throw new BITalinoException(BITalinoErrorTypes.LOST_COMMUNICATION);
        }
    }
}