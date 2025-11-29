package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Manages a TCP connection between the client application and the server.
 *
 * This class establishes a socket connection, initializes input and output
 * data streams, and provides access to helper classes
 * {@link SendDataViaNetwork} and {@link ReceiveDataViaNetwork} for structured
 * communication.
 *
 * The connection is created upon instantiation and can be closed using {@link #releaseResources()}.
 */
public class Connection {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final SendDataViaNetwork send;
    private final ReceiveDataViaNetwork receive;

    /**
     * Creates a new TCP connection to the given server address and port.
     *
     * @param ipAddress the IP address of the server
     * @param port      the port number on which the server is listening
     * @throws RuntimeException if the connection cannot be established
     */
    public Connection(String ipAddress, int port) {
        try {
            this.socket = new Socket(ipAddress, port);
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            this.send = new SendDataViaNetwork(out);
            this.receive = new ReceiveDataViaNetwork(in);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the helper class used to send structured data to the server
     */
    public SendDataViaNetwork getSendViaNetwork() {
        return send;
    }

    /**
     * @return the helper class used to receive structured data from the server
     */
    public ReceiveDataViaNetwork getReceiveViaNetwork() {
        return receive;
    }

    /**
     * Closes the socket and associated I/O streams.
     * This method should be called when the connection is no longer needed.
     */
    public void releaseResources() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ignored){
            ignored.printStackTrace();
        }
    }
}