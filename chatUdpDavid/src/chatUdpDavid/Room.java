package chatUdpDavid;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private List<User> users; // Esta lista contendrá los hilos de los usuarios

    public Room(String name) {
        this.name = name;
        this.users = new ArrayList<>();
    }

    public synchronized void addUser(User user) {
        // Sincronizado para manejar el acceso concurrente a la lista de usuarios
        this.users.add(user);
    }

    public synchronized void removeUser(User user) {
        // Sincronizado por la misma razón mencionada anteriormente
        this.users.remove(user);
    }

    public void sendMessage(String message, User sender) {
        // Enviar un mensaje a todos los usuarios en la sala excepto al remitente
        for (User aUser : users) {
            if (aUser != sender) {
                aUser.sendMessage(message);
            }
        }
    }

    public List<String> listUsers() {
        // Devuelve una lista de nombres de usuario para mostrar quién está en la sala
        List<String> userList = new ArrayList<>();
        for (User aUser : users) {
            userList.add(aUser.getUsername());
        }
        return userList;
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }
}