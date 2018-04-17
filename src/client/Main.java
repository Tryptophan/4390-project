package client;

public class Main {
    public static void main(String args[]) {
        try {
            TCPClient client = new TCPClient("127.0.0.1", 8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
