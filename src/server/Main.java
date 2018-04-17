package server;

public class Main {
    public static void main(String args[]) {
        // Ask user if we want to run a TCP or UDP server

        // Start the server depending on choice
        try {
            TCPServer tcpServer = new TCPServer(8080);
            tcpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
