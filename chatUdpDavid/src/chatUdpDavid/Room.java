package chatUdpDavid;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private List<User> users;

    public Room(String name) {
        this.name = name;
        this.users = new ArrayList<>();
    }
    public void addUser(User user) {
        synchronized (users) {
            users.add(user);
        }
    }
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public void removeUser(User user) {
        synchronized (users) {
            users.remove(user);
        }
    }
    public synchronized void sendMessage(String message, User sender) {
    	
        for (User user : users) {
        	if (!user.equals(sender)) {
                user.sendMessage(message);
            }
        }
    }

    public  synchronized List<User> listUsers() {
    	   return new ArrayList<>(users);
    }
}