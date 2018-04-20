package project.client;

import project.Checksum;
import project.MessageType;
import project.UDPEndpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public abstract class UDPClient extends UDPEndpoint {

    private boolean requestingFile = false;
    private String requestingFilename;
    private byte[] clientChecksum = null;

    public UDPClient(String ip, int port) throws Exception {
        socket = new DatagramSocket();
        this.ip = InetAddress.getByName(ip);
        this.port = port;
        listen();
    }

    public void onReceivePacket(DatagramPacket packet) throws Exception {

        // Read in the packet and prune the null bytes
        byte[] data = new byte[packet.getLength()];
        System.arraycopy(buffer, 0, data, 0, packet.getLength());

        String str = new String(data);
        System.out.printf("RECV: %s\n", str);

        if (str.equals(MessageType.NACK)) {
            fileComplete();
        }

        if (str.equals(MessageType.EOF)) {
            // Stop reading the file in
            requestingFile = false;
            // Check that the file sent correctly with the checksum sent
            // Get the checksum of the file sent
            clientChecksum = Checksum.getMD5Checksum(new File("recv-" + requestingFilename));
            requestingFilename = null;
        }

        if (requestingFile) {
            String filename = requestingFilename;

            File file = new File("recv-" + filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileOutputStream fos = new FileOutputStream(file, true)) {
                // File is being sent to us
                // Write the chunk of the file to the file output stream
                fos.write(data);
                fos.flush();
                fos.close();
                sendMessage(MessageType.ACK.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Start reading in the checksum
        if (str.contains(MessageType.CHK)) {
            byte[] serverChecksum = new byte[data.length - 4];
            System.arraycopy(data, 4, serverChecksum, 0, serverChecksum.length);
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
