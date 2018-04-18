package project.client;

import project.MessageType;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) {
        try {
            TCPClient client = new TCPClient("127.0.0.1", 8080) {
                @Override
                public void onFileComplete() throws Exception {
                    getFileFromUser(this);
                }
            };

            getFileFromUser(client);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFileFromUser(TCPClient client) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("To request a file from the server type in a file name:");

        // Request the file from the server
        client.requestFile(scanner.next());
    }
}
