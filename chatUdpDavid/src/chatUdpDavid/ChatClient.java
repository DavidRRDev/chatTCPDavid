package chatUdpDavid;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }
    /*
    
    This class reads messages from the server and prints them to the console*/
      static class ReadThread extends Thread {
          private BufferedReader reader;
          private Socket socket;
          private ChatClient client;

            public ReadThread(Socket socket, ChatClient client) {
                this.socket = socket;
                this.client = client;

                try {
                    InputStream input = socket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(input));
                } catch (IOException ex) {
                    System.out.println("Error getting input stream: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            public void run() {
                while (true) {
    try {  String response = reader.readLine();
                        System.out.println("\n" + response);

                        // Prints the prompt after displaying the server's message
                        if (client.getUserName() != null) {
                            System.out.print("[" + client.getUserName() + "]: ");
                        }
                    } catch (IOException ex) {
                        System.out.println("Error reading from server: " + ex.getMessage());
                        ex.printStackTrace();
                        break;
      }
    }
            }
        }
    }