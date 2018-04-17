package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

    private String ip;
    private int port;
    private Socket client;

    /**
     * Create a client to connect to a TCP server.
     *
     * @param ip IP address of TCP server we want to connect to.
     * @param port Port of the TCP server we want to connect to.
     */
    public TCPClient(String ip, int port) throws Exception {

        BufferedReader inBuffer = new BufferedReader(new InputStreamReader(System.in));

        this.client = new Socket(ip, port);
        DataOutputStream outBuffer = new DataOutputStream(client.getOutputStream());

        String data = inBuffer.readLine();

        outBuffer.writeBytes(data + '\n');
        client.close();
    }

    public void requestFile(String filename) {

    }

    public void sendMessage(String message) {

    }

    public void onReceivedMessage(String message) {

    }
}
