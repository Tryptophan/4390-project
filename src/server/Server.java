package server;

public class Server {
    public static void main(String args[]) {
        // TODO: Ask user if we want to run a TCP or UDP server

        // Start the server depending on choice
        try {
            TCPServer server = new TCPServer(8080);
            server.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
