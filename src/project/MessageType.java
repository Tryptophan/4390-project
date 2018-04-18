package project;

public interface MessageType {
    public static final String CONN = "CONN";
    public static final String ACK = "ACK";
    public static final String NACK = "NACK";
    public static final String REQ_FILE = "REQ_FILE";
    public static final String EOF = "EOF";

    public static boolean isMessage(String str) {
        return str.equals(CONN) ||
                str.equals(ACK) ||
                str.equals(NACK) ||
                str.equals(REQ_FILE);
    }
}
