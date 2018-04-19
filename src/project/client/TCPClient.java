package project.client;

import project.TCPEndpoint;
import project.MessageType;

import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

public abstract class TCPClient extends TCPEndpoint {

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
        this.out = client.getOutputStream();

        // Send a message to the server we want to connect
        sendMessage(MessageType.CONN.getBytes());
        listen(client);
    }

    public void onReceiveMessage(byte[] message) throws Exception {

        String str = new String(message).replaceAll("\u0000.*", "");
        System.out.printf("RECV: %s\n", str);

        if (str.equals(MessageType.NACK)) {
            fileComplete();
        }

        else if (str.equals(MessageType.EOF)) {
            fileComplete();
        }

        else if (requestingFile) {

            String filename = requestingFilename;

            File file = new File("recv-" + filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileOutputStream fos = new FileOutputStream(file, true)) {
                // File is being sent to us
                // Write the chunk of the file to the file output stream
                fos.write(message);
                fos.flush();
                fos.close();
                sendMessage(MessageType.ACK.getBytes());
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

    private void fileComplete() throws Exception {
        requestingFile = false;
        requestingFilename = null;
        onFileComplete();
    }

    public abstract void onFileComplete() throws Exception;
}
