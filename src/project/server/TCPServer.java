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
        this.out = new DataOutputStream(client.getOutputStream());
        listen(client);
    }

    public void onReceiveMessage(byte[] message) throws Exception {

        String str = new String(message);
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
                System.out.printf("Found file [%s].\n", filename);

                // Send the file to the client
                sendFile(file);

            } else {
                // If file doesn't exist send NACK
                sendMessage(MessageType.NACK.getBytes());
            }
        }
    }

    public void sendFile(File file) {

        FileInputStream fis = null;
        Reader r = null;
        BufferedReader br = null;

        try {

            // Read in the file
            fis = new FileInputStream(file);
            r = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(r);

            String line = null;
            while ((line = br.readLine()) != null) {
                sendMessage((MessageType.FILE_PART + ":" + file.getName() + ":" + line).getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the stream readers
                if (fis != null) {
                    fis.close();
                }
                if (r != null) {
                    r.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
