import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public static Socket socket;
    public static DataInputStream in;
    public static DataOutputStream out;


    public static void openConnection() {

        try {
          socket = new Socket(HOST, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            socket.close();
            System.out.println("Client left");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
