package client;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class TCPClient {

    private String ip;
    private int port;
    private Socket client;

    private DataOutputStream out;

    /**
     * Create a client to connect to a TCP server.
     *
     * @param ip IP address of TCP server we want to connect to.
     * @param port Port of the TCP server we want to connect to.
     */
    public TCPClient(String ip, int port) throws Exception {
        this.ip = ip;
        this.port = port;
        this.client = new Socket(ip, port);
        this.out = new DataOutputStream(this.client.getOutputStream());
    }

    public void requestFile(String filename) {

    }

    public void sendMessage(byte[] message) throws Exception {
        System.out.printf("SEND: %s", new String(message));
        out.write(message);
        out.flush();
    }

    public void onReceivedMessage(byte[] message) {

    }

    public void close() throws Exception {
        client.close();
    }
}
