package project.server;

import project.Endpoint;
import project.MessageType;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Endpoint {

    private int port;
    private ServerSocket server;

    public TCPServer(int port) throws Exception {
        this.port = port;
        this.server = new ServerSocket(this.port);

        System.out.printf("TCP server listening on port %s.\n", this.port);

        Socket client = this.server.accept();
        this.out = new DataOutputStream(client.getOutputStream());
        listen(client);
    }

    public void onReceiveMessage(byte[] message) throws Exception {

        String str = new String(message);
        System.out.printf("RECV: %s\n", str);

        if (str.equals(MessageType.CONN)) {
            sendMessage(MessageType.ACK.getBytes());
        } else if (str.contains(MessageType.REQFILE)) {
            // TODO: Send file if exists

            // TODO: If file doesn't exist send NACK
        }
    }
}
