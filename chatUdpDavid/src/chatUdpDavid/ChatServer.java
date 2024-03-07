package chatUdpDavid;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<User> users;
    private List<Room> rooms;

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.startServer(8080);
    }

    public ChatServer() {
        users = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor de chat iniciado en el puerto " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress().getHostAddress());
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private User user;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println("Conexión establecida. Bienvenido al chat!");
                out.println("Ingrese su nombre de usuario:");

                String username = null;
                while (username == null || username.isEmpty()) {
                    username = in.readLine();  // Espera a que se ingrese el nombre de usuario
                    user = new User(username, clientSocket);
                    users.add(user);
                }

                out.println("Bienvenido, " + username + "! Para ver la lista de comandos, escriba /help");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("/")) {
                        handleCommand(inputLine);
                    } else {
                        broadcastMessage(user.getUsername() + " - " + inputLine, user.getCurrentRoom());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (user != null) {
                        users.remove(user);
                        user.leaveRoom();
                        if (user.getCurrentRoom() == null) {
                            out.println("Debe unirse a una sala antes de enviar mensajes.");
                        } 
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleCommand(String command) {
            String[] tokens = command.split("\\s+");
            
            switch (tokens[0]) {
                case "/help":
                    out.println("Lista de comandos:");
                    out.println("/list - Lista todas las salas disponibles");
                    out.println("/create [nombre_sala] - Crea una nueva sala");
                    out.println("/join [nombre_sala] - Únete a una sala existente");
                    out.println("/delroom [nombre_sala] - Elimina una sala existente");
                    out.println("/exit - Salir del chat");
                    break;
                case "/list":
                    if (rooms.isEmpty()) {
                        out.println("No hay salas creadas.");
                    } else {
                        out.println("Salas disponibles:");
                        for (Room room : rooms) {
                            out.println("- " + room.getName());
                        }
                        out.println("Use /join para unirse a una sala o /delroom para eliminar una sala.");
                    }
                    break;
                case "/create":
                    if (tokens.length < 2) {
                        out.println("Uso: /create [nombre_sala]");
                    } else {
                        String roomName = tokens[1];
                        Room room = createRoom(roomName);
                        if (room != null) {
                            user.joinRoom(room);
                            out.println("Sala '" + roomName + "' creada y unido exitosamente.");
                            out.println("Escriba el mensaje:");
                        } else {
                            out.println("La sala '" + roomName + "' ya existe.");
                        }
                    }
                    break;
                case "/join":
                    if (tokens.length < 2) {
                        out.println("Uso: /join [nombre_sala]");
                    } else {
                        String roomName = tokens[1];
                        Room room = findRoom(roomName);
                        if (room != null) {
                            user.joinRoom(room);
                            out.println("Unido exitosamente a la sala '" + roomName + "'.");
                        } else {
                            out.println("La sala '" + roomName + "' no existe.");
                        }
                    }
                    break;
                case "/delroom":
                    if (tokens.length < 2) {
                        out.println("Uso: /delroom [nombre_sala]");
                    } else {
                        String roomNameToRemove = tokens[1];
                        Room roomToRemove = null;
                        for (Room room : rooms) {
                            if (room.getName().equals(roomNameToRemove)) {
                                roomToRemove = room;
                                break;
                            }
                        }
                        if (roomToRemove != null) {
                            rooms.remove(roomToRemove);
                            out.println("Sala '" + roomNameToRemove + "' eliminada correctamente.");
                        } else {
                            out.println("La sala '" + roomNameToRemove + "' no existe.");
                        }
                    }
                    break;
                case "/exit":
                    if (user.getCurrentRoom() != null) {
                        user.leaveRoom();
                        out.println("Salió de la sala.");
                    } else {
                        out.println("Desconectado del chat.");
                    }
                    break;
                default:
                    out.println("Comando no reconocido. Escriba /help para ver la lista de comandos.");
            }
        }
    }

    public synchronized void broadcastMessage(String message, Room room) {
        if (room != null) {
            List<User> roomUsers = room.listUsers();
            for (User user : roomUsers) {
                user.sendMessage(message);
            }
        } else {
            System.out.println("Error: La sala especificada no existe. No se puede enviar el mensaje.");
        }
    }

    public Room findRoom(String roomName) {
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        return null; 
    }

    public synchronized Room createRoom(String roomName) {
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                return null; 
            }
        }
        
        Room newRoom = new Room(roomName);
        rooms.add(newRoom);
        return newRoom;
    }
}
