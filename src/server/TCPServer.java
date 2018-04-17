package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private int port;
    private boolean running;
    private ServerSocket server;

    public TCPServer(int port) throws Exception {
        this.port = port;
        this.server = new ServerSocket(this.port);
    }

    /**
     * Starts the web socket and listens on port.
     */
    public void listen() throws Exception {
        System.out.println("TCP server listening on port: " + this.port);

        // Accept connections from incoming clients
        Socket client = this.server.accept();

        // Get the stream of data from the client as a buffer
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String data = null;
        while ((data = in.readLine()) != null) {
            System.out.printf("RECV: %s\n", data);
        }
    }

    public void onReceivedMessage(byte[] message) {
        System.out.printf("Received message: %s", new String(message));

    }

    /**
     * Stops the web socket if already listening on port.
     */
    public void stop() {
        // Check if the server is already running
        if (!this.running) {

        }
    }
}
