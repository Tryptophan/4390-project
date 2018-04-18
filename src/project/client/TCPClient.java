package project.client;

import project.Endpoint;
import project.MessageType;

import java.io.DataOutputStream;
import java.net.Socket;

public class TCPClient extends Endpoint {

    private String ip;
    private int port;

    /**
     * Create a client to connect to a TCP server.
     *
     * @param ip   IP address of TCP server we want to connect to.
     * @param port Port of the TCP server we want to connect to.
     */
    public TCPClient(String ip, int port) throws Exception {
        this.ip = ip;
        this.port = port;
        Socket client = new Socket(ip, port);
        this.out = new DataOutputStream(client.getOutputStream());

        // Send a message to the server we want to connect
        sendMessage(MessageType.CONN.getBytes());
        listen(client);
    }

    public void sendMessage(byte[] message) throws Exception {

        String str = new String(message);

        // Check if the message doesn't have a delimiter
        if (!str.contains("\n")) {
            str += "\n";
        }

        System.out.printf("SEND: %s", str);
        out.write(str.getBytes());
        out.flush();
    }

    public void onReceiveMessage(byte[] message) {
        String str = new String(message);
        System.out.printf("RECV: %s\n", str);

        // TODO: Decide what to do for each message
    }
}
