package project;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class UDPEndpoint {
    public DatagramSocket socket;
    public byte[] buffer = new byte[4096];
    public InetAddress ip;
    public int port;

    public void sendMessage(byte[] message) throws Exception {
        DatagramPacket packet = new DatagramPacket(message, message.length, this.ip, this.port);
        socket.send(packet);
        System.out.printf("SEND: %s\n", new String(message));
    }

    public void listen() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    // Get incoming packets from client
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    onReceivePacket(packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        });

        thread.start();
    }

    public abstract void onReceivePacket(DatagramPacket packet) throws Exception;
}
