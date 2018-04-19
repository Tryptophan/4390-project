package project.client;

import project.UDPEndpoint;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient extends UDPEndpoint {

    public UDPClient(String ip, int port) throws Exception {
        socket = new DatagramSocket();
        this.ip = InetAddress.getByName(ip);
        this.port = port;
        listen();
    }

    @Override
    public void onReceivePacket(DatagramPacket packet) throws Exception {

        // Read in the packet and prune the null bytes
        byte[] data = new byte[packet.getLength()];
        System.arraycopy(buffer, 0, data, 0, packet.getLength());

        String str = new String(data);
        System.out.printf("RECV: %s\n", str);
    }
}
