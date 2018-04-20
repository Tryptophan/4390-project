package project.client;

import project.Checksum;
import project.TCPEndpoint;
import project.MessageType;

import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Arrays;

public abstract class TCPClient extends TCPEndpoint {

    private String ip;
    private int port;

    private boolean requestingFile = false;
    private String requestingFilename = null;

    private byte[] clientChecksum = null;

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
        } else if (str.equals(MessageType.EOF)) {
            // Stop reading the file in
            requestingFile = false;
            // Check that the file sent correctly with the checksum sent
            // Get the checksum of the file sent
            clientChecksum = Checksum.getMD5Checksum(new File("recv-" + requestingFilename));
            requestingFilename = null;
        } else if (requestingFile) {

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

        // Start reading in the checksum
        else if (str.contains(MessageType.CHK)) {
            byte[] serverChecksum = new byte[message.length - 4];
            System.arraycopy(message, 4, serverChecksum, 0, serverChecksum.length);
            System.out.printf("Server checksum: [%s], Client checksum: [%s].\n", new String(serverChecksum), new String(clientChecksum));
            // Get if the two checksums are equal
            boolean checksumMatched = Arrays.equals(serverChecksum, clientChecksum);
            if (checksumMatched) {
                System.out.println("OK: Generated checksum matches the one from the server.");
            } else {
                System.out.println("ERR: Generated checksum does not match the one from the server!");
            }
            fileComplete();
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
