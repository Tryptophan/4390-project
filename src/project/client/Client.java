package project.client;

import project.MessageType;

import java.util.Scanner;

public class Client {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.println("Type in the protocol for the client (TCP/UDP):");
        String protocol = input.next();

        // Get ip/port of server from user
        System.out.println("Type in the address the server is running at [ip:port]:");
        String host = input.next();
        String ip = host.substring(0, host.indexOf(":"));
        int port = Integer.parseInt(host.substring(host.indexOf(":") + 1));

        if (protocol.equals("TCP")) {
            startTCP(ip, port);
        } else {
            startUDP(ip, port);
        }
    }

    // TODO: pass in user input here
    public static void startTCP(String ip, int port) {
        try {
            TCPClient client = new TCPClient(ip, port) {
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

    public static void startUDP(String ip, int port) {
        try {
            UDPClient client = new UDPClient(ip, port) {
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
