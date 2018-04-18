package project.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[256];
    private int port;

    public UDPServer(int port) throws Exception {
        this.port = port;
        socket = new DatagramSocket(port);
    }

    public void start() throws Exception {
        running = true;

        while (running) {
            // Get incoming packets from client
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            System.out.println(new String(packet.getData()));

            //packet = new DatagramPacket(buffer, buffer.length, address, port);
            //String received = new String(packet.getData(), 0, packet.getLength());
            //socket.send(packet);
        }

        socket.close();
    }

    public void stop() {
        running = false;
    }

}
