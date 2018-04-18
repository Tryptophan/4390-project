package project.server;

import java.util.Scanner;

public class Server {
    public static void main(String args[]) {

        // Ask user if we want to run a TCP or UDP server
        Scanner input = new Scanner(System.in);
        System.out.println("Type in the protocol for the server (TCP/UDP):");

        String protocol = input.next();

        // TODO: Get the server port from the user

        if (protocol.equals("TCP")) {
            // Start TCP server
            try {
                TCPServer server = new TCPServer(8080);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Start UDP server
            try {
                UDPServer server = new UDPServer(8080);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
