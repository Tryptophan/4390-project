package project.client;

import project.Endpoint;
import project.MessageType;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPClient extends Endpoint {

    private String ip;
    private int port;

    private boolean requestingFile = false;
    private String requestingFilename = null;

    /**
     * Create a client to connect to a TCP server.
     *
     * @param ip   IP address of TCP server we want to connect to.
     * @param port Port of the TCP server we want to connect to.
     */
    public TCPClient(String ip, int port) throws Exception {
        this.ip = ip;
        this.port = port;
        Socket client = new Socket(ip, port);
        this.out = new DataOutputStream(client.getOutputStream());

        // Send a message to the server we want to connect
        sendMessage(MessageType.CONN.getBytes());
        listen(client);
    }

    public void onReceiveMessage(byte[] message) throws Exception {

        String str = new String(message).replaceAll("\u0000.*", "");
        System.out.printf("RECV: %s\n", str);

        if (requestingFile) {

            String filename = requestingFilename;

            File file = new File("recv-" + filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileOutputStream fos = new FileOutputStream(file, true)) {
                // File is being sent to us
                // Write the chunk of the file to the file output stream
                fos.write(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestFile(String filename) throws Exception {
        requestingFile = true;
        requestingFilename = filename;
        String reqfile = MessageType.REQ_FILE + ":" + filename;
        sendMessage(reqfile.getBytes());
    }
}
