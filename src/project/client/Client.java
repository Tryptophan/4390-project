package project.client;

import project.MessageType;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) {
        try {
            TCPClient client = new TCPClient("127.0.0.1", 8080);

            Scanner scanner = new Scanner(System.in);
            System.out.println("To request a file from the server type in a file name:");

            String reqfile = MessageType.REQ_FILE + ":" + "send/" + scanner.next();
            client.sendMessage(reqfile.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
