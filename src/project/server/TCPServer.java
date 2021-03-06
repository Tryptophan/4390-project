package project.server;

import project.Checksum;
import project.TCPEndpoint;
import project.MessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;

public class TCPServer extends TCPEndpoint {

    private int port;
    private ServerSocket server;

    private boolean sendingFile = false;
    private boolean sendNextFileChunk = false;

    public TCPServer(int port) throws Exception {
        this.port = port;
        this.server = new ServerSocket(this.port);

        System.out.printf("TCP server listening on port %s.\n", this.port);

        Socket client = this.server.accept();
        this.out = client.getOutputStream();
        listen(client);
    }

    public void onReceiveMessage(byte[] message) throws Exception {


        // Remove all null bytes
        String str = new String(message).replaceAll("\u0000.*", "");
        System.out.printf("RECV: %s\n", str);

        if (str.equals(MessageType.CONN)) {
            sendMessage(MessageType.ACK.getBytes());
        } else if (str.contains(MessageType.REQ_FILE)) {

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

        if (str.equals(MessageType.CHKOK)) {
            System.out.println("OK: Generated checksum matches the one from the server.");
        }

        if (str.equals(MessageType.CHKERR)) {
            System.out.println("ERR: Generated checksum does not match the one from the server!");
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
                    while (!sendNextFileChunk) {
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
