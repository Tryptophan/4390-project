package project.client;

import project.MessageType;
import project.server.UDPServer;

import java.util.Scanner;

public class Client {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.println("Type in the protocol for the client (TCP/UDP):");
        String protocol = input.next();

        // TODO: Get ip/port

        if (protocol.equals("TCP")) {
            startTCP();
        } else {
            try {
                UDPClient client = new UDPClient("127.0.0.1", 8080);
                client.sendMessage("TEST".getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: pass in user input here
    public static void startTCP() {
        try {
            TCPClient client = new TCPClient("127.0.0.1", 8080) {
                @Override
                public void requestNewFile() throws Exception {
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
