package project.server;

import project.Endpoint;
import project.MessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class TCPServer extends Endpoint {

    private int port;
    private ServerSocket server;

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
        }
    }

    public void sendFile(File file) {

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
            }
            // Let the client know they have the full file
            sendMessage(MessageType.EOF.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
