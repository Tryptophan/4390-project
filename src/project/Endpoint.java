package project;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Endpoint {

    public OutputStream out;
    public InputStream in;

    public void sendMessage(byte[] message) throws Exception {

        String str = new String(message).replaceAll("\u0000.*", "");

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
                byte[] buffer = new byte[4096];
                while (in.read(buffer) > 0) {
                    onReceiveMessage(buffer);
                    // Clear buffer after message
                    buffer = new byte[4096];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listener.start();
    }

    public abstract void onReceiveMessage(byte[] message) throws Exception;
}
