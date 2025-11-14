package manageData;

import POJOS.Patient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendDataViaNetwork {
    private DataOutputStream dataOutputStream;

    public SendDataViaNetwork(Socket socket) {
        try{
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Output stream could not be created: " + e.getMessage());
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, e);
        }

    }
    public void sendStrings(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }

    public void sendInt(int message) throws IOException{
        dataOutputStream.writeInt(message);
        dataOutputStream.flush();
    }

    public void sendPatient(Patient patient) throws IOException{
        dataOutputStream.writeUTF(patient.getFullName());
        dataOutputStream.writeUTF(patient.getDob().toString());
        dataOutputStream.writeUTF(patient.getEmail());
        dataOutputStream.writeUTF(patient.getPassword());
        dataOutputStream.flush();

    }

    /*public void sendReport(Report report) throws IOException{

    }*/
    public void releaseResources() {
        try {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        } catch (IOException ex) {
            System.err.println("Error releasing resources: " + ex.getMessage());
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
