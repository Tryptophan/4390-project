package project;

import project.client.Client;
import project.server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.println("Is this host a server or client? (server/client):");

        if (input.next().toLowerCase().equals("server")) {
            Server.start();
        } else {
            Client.start();
        }
    }
}
