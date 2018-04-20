package project;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class TCPEndpoint {

    public OutputStream out;
    public InputStream in;

    public void sendMessage(byte[] message) throws Exception {

        String str = new String(message);//.replaceAll("\u0000.*", "");

        // Check if the message is a predefined message
        System.out.printf("SEND: %s\n", str);

        out.write(message);
        out.flush();
    }

    public void listen(Socket client) throws Exception {

        this.in = client.getInputStream();

        // Listen for incoming messages on a new thread
        Thread listener = new Thread(() -> {
            try {
                // Read the input stream
                int count;
                byte[] buffer = new byte[4096];
                while ((count = in.read(buffer)) != -1) {
                    //System.out.printf("Count: [%s].\n", count);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    out.write(buffer, 0, count);
                    onReceiveMessage(out.toByteArray());
                    // Clear buffer after message
                    buffer = new byte[4096];
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listener.start();
    }

    public abstract void onReceiveMessage(byte[] message) throws Exception;
}
