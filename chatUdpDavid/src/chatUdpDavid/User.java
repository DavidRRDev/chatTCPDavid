package chatUdpDavid;

import java.io.*;
import java.net.Socket;

public class User extends Thread {
    private String username;
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private Room currentRoom;

    public User(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);
            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, null);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this.currentRoom);

            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has left.";
            server.broadcast(serverMessage, null);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a message to the client.
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * Sets the currentRoom of this user.
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * Gets the currentRoom of this user.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Prints the list of online users to the newly connected user.
     */
    private void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    /**
     * Stores username of the newly connected user.
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the user.
     */
    public String getUsername() {
        return this.username;
    }
}