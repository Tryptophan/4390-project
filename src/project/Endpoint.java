package project;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public abstract class Endpoint {

    public DataOutputStream out;

    public void sendMessage(byte[] message) throws Exception {

        String str = new String(message);

        // Check if the message doesn't have a delimiter
        if (!str.contains("\n")) {
            str += "\n";
        }

        System.out.printf("SEND: %s", str);
        out.write(str.getBytes());
        out.flush();
    }

    public void listen(Socket client) throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        // Listen for incoming messages on a new thread
        Thread listener = new Thread(() -> {
            try {
                String message = null;
                while ((message = in.readLine()) != null) {
                    onReceiveMessage(message.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listener.start();
    }

    public abstract void onReceiveMessage(byte[] message) throws Exception;
}
