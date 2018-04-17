package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
    public static void main(String args[]) {
        try {

            BufferedReader inBuffer = new BufferedReader(new InputStreamReader(System.in));
            TCPClient client = new TCPClient("127.0.0.1", 8080);

            while (true) {
                client.sendMessage((inBuffer.readLine() + '\n').getBytes());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
