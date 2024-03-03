package chatUdpDavid;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<User> users; // Aquí usaríamos UserThread en lugar de User
    private List<Room> rooms;

    public ChatServer(int port) {
        users = new ArrayList<>();
        rooms = new ArrayList<>();
        // Inicializar serverSocket aquí con el puerto especificado
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error al crear el server socket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            System.out.println("Servidor iniciado en el puerto: " + serverSocket.getLocalPort());
            
            // Bucle infinito para esperar conexiones
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                
                User user = new User(clientSocket, this);
                users.add(user);
                user.start();
            }
        } catch (IOException e) {
            System.out.println("Error al aceptar conexión del cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, Room room) {
        for (User user : users) {
            if (user.getCurrentRoom().equals(room)) {
                user.sendMessage(message);
            }
        }
    }

    public Room createRoom(String roomName) {
        Room room = new Room(roomName);
        rooms.add(room);
        return room;
    }

    public List<Room> listRooms() {
        return rooms;
    }

    // Métodos adicionales para manejar usuarios y salas podrían ser añadidos aquí
}