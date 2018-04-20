package project.client;

import project.MessageType;
import project.UDPEndpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class UDPClient extends UDPEndpoint {

    private boolean requestingFile = false;
    private String requestingFilename;

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

        else if (str.equals(MessageType.EOF)) {
            fileComplete();
        }

        if (requestingFile) {
            String filename = requestingFilename;

            File file = new File("recv-" + filename);

            file.createNewFile();

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
