package project.server;

import project.Checksum;
import project.MessageType;
import project.UDPEndpoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;

public class UDPServer extends UDPEndpoint {

    private boolean sendNextFileChunk = false;
    private boolean sendingFile = false;

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
        } else if (str.contains(MessageType.REQ_FILE)) {
            // Check if the file exists
            String filename = str.substring(str.indexOf(':') + 1);

            File file = new File(filename);

            Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();
            System.out.printf("Current directory: [%s].\n", path);

            if (file.exists()) {

                // Send file if exists
                System.out.printf("Sending file [%s] to client.\n", filename);

                // Send the file to the client
                sendFile(file);

            } else {
                // If file doesn't exist send NACK
                sendMessage(MessageType.NACK.getBytes());
            }
        } else if (str.equals(MessageType.ACK) && sendingFile) {
            sendNextFileChunk = true;
        }
    }

    public void sendFile(File file) {
        sendingFile = true;
        Thread thread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(file)) {
                // Read the chunk of the file into the buffer and send to the client
                int count;
                byte[] buffer = new byte[4096];
                while ((count = fis.read(buffer)) != -1) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    out.write(buffer, 0, count);
                    // Write to file output stream
                    sendMessage(out.toByteArray());
                    buffer = new byte[4096];
                    out.close();
                    out.flush();
                    // Wait for an ACK to send next chunk
                    sendNextFileChunk = false;
                    while(!sendNextFileChunk) {
                        Thread.sleep(50);
                    }
                }
                fis.close();
                sendingFile = false;
                // Let the client know they have the full file
                sendMessage(MessageType.EOF.getBytes());

                // Send the checksum of the file to the client to confirm it sent correctly
                byte[] chk = (MessageType.CHK + ":").getBytes();
                byte[] checksum = Checksum.getMD5Checksum(file);

                byte[] message = Arrays.copyOf(chk, chk.length + checksum.length);
                System.arraycopy(checksum, 0, message, chk.length, checksum.length);

                sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
