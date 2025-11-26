package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    private final SendDataViaNetwork send;
    private final ReceiveDataViaNetwork receive;

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

    public SendDataViaNetwork getSendViaNetwork() {
        return send;
    }

    public ReceiveDataViaNetwork getReceiveViaNetwork() {
        return receive;
    }

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