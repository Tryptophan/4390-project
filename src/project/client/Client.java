package project.client;

import project.MessageType;

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
            startUDP();
        }
    }

    // TODO: pass in user input here
    public static void startTCP() {
        try {
            TCPClient client = new TCPClient("127.0.0.1", 8080) {
                @Override
                public void onFileComplete() throws Exception {
                    getTCPFile(this);
                }
            };

            getTCPFile(client);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startUDP() {
        try {
            UDPClient client = new UDPClient("127.0.0.1", 8080) {
                @Override
                public void onFileComplete() throws Exception {
                    getUDPFile(this);
                }
            };
            client.sendMessage(MessageType.CONN.getBytes());

            Scanner input = new Scanner(System.in);
            System.out.println("To request a file from the server type in a file name: ");

            // Request a file using input
            client.requestFile(input.next());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTCPFile(TCPClient client) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("To request a file from the server type in a file name:");

        // Request the file from the server
        client.requestFile(scanner.next());
    }

    public static void getUDPFile(UDPClient client) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("To request a file from the server type in a file name:");

        // Request the file from the server
        client.requestFile(scanner.next());
    }
}
