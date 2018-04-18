package project.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private DatagramSocket socket;
    private InetAddress ip;
    private int port;

    public UDPClient(String ip, int port) throws Exception {
        socket = new DatagramSocket();
        this.ip = InetAddress.getByName(ip);
        this.port = port;
    }

    public void sendMessage(byte[] message) throws Exception {
        DatagramPacket packet = new DatagramPacket(message, message.length, this.ip, this.port);
        socket.send(packet);
    }
}
