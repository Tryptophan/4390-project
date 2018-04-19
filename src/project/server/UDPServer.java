package project.server;

import project.MessageType;
import project.UDPEndpoint;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer extends UDPEndpoint {

    public UDPServer(int port) throws Exception {
        socket = new DatagramSocket(port);
        listen();
        System.out.printf("UDP server listening on port %s.\n", port);
    }

    public void onReceivePacket(DatagramPacket packet) throws Exception {

        // Read in the packet and prune the null bytes
        byte[] data = new byte[packet.getLength()];
        System.arraycopy(buffer, 0, data, 0, packet.getLength());

        String str = new String(data);
        System.out.printf("RECV: %s\n", str);

        // Check if client is trying to connect
        if (str.equals(MessageType.CONN)) {
            // Set the client's ip and port
            this.ip = packet.getAddress();
            this.port = packet.getPort();
            // Send an ACK back to the client that they're connected
            sendMessage(MessageType.ACK.getBytes());
        }
    }
}
