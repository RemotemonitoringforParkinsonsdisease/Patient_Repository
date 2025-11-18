package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private SendDataViaNetwork sendDataViaNetwork;
    private ReceiveDataViaNetwork receiveDataViaNetwork;

    public Connection(String ipAddress, int port) {
        try {
            this.socket = new Socket(ipAddress, port);
            this. sendDataViaNetwork = new SendDataViaNetwork(socket);
            this. receiveDataViaNetwork = new ReceiveDataViaNetwork(socket);
        } catch (Exception e) {
            System.out.println("Error establishing connection to " + ipAddress + " on port " + port); //TODO: Revisar excepciones
        }
    }


}
