package project.server;

public class Server {
    public static void main(String args[]) {
        // TODO: Ask user if we want to run a TCP or UDP project.server

        // Start the project.server depending on choice
        try {
            TCPServer server = new TCPServer(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
