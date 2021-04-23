package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Client.Client;

public class Server {
    static Socket socket;

    public static void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Server.Server is running");
            Client.openConnection();
            socket = serverSocket.accept();
            System.out.println("Client.Client connected");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

