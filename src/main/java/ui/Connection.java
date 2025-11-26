package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private SendDataViaNetwork sendDataViaNetwork;
    private ReceiveDataViaNetwork receiveDataViaNetwork;

    public Connection(String ipAddress, int port) {
        try {
            this.socket = new Socket(ipAddress, port);
            this.sendDataViaNetwork = new SendDataViaNetwork(socket);
            this.receiveDataViaNetwork = new ReceiveDataViaNetwork(socket);
        } catch (IOException e) {
            System.out.println("Error establishing connection to " + ipAddress + " on port " + port);
            e.printStackTrace(); // Muestra más detalles sobre el error (como el tipo exacto de excepción)
        }
    }

    public SendDataViaNetwork getSendViaNetwork() {
        return sendDataViaNetwork;
    }

    public ReceiveDataViaNetwork getReceiveViaNetwork() {
        return receiveDataViaNetwork;
    }

    public void releaseResources() {
        try {
            if (sendDataViaNetwork != null) {
                sendDataViaNetwork.releaseResources();  // o close(), según lo hayas llamado
            }
        } catch (Exception e) {
            System.out.println("Error releasing sendDataViaNetwork: " + e.getMessage());
        }

        try {
            if (receiveDataViaNetwork != null) {
                receiveDataViaNetwork.releaseResources(); // o close()
            }
        } catch (Exception e) {
            System.out.println("Error releasing receiveDataViaNetwork: " + e.getMessage());
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing socket: " + e.getMessage());
        }
    }
}
