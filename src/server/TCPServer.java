package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private int port;
    private boolean running;
    private ServerSocket server;

    public TCPServer(int port) {
        this.port = port;
    }

    /**
     * Starts the web socket and listens on port.
     */
    public void start() {
        try {
            // Start socket on port
            this.server = new ServerSocket(this.port);
            System.out.println("TCP server listening on port: " + this.port);

            while (true) {

                // Accept connections from incoming clients
                Socket client = this.server.accept();

                // Get the stream of data from the client as a buffer
                BufferedReader buffer = new BufferedReader(new InputStreamReader(client.getInputStream()));

                System.out.println(buffer.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
