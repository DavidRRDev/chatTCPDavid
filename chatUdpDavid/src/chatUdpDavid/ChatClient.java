package chatUdpDavid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private String username;
    private Socket socket;
    private BufferedReader consoleInput;
    private PrintWriter outputStream;
    private BufferedReader inputStream;

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }

    public void start() {
        try {
            consoleInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese su nombre de usuario: ");
            username = consoleInput.readLine();

            connectToServer("127.0.0.1", 8080);

            Thread messageReceiverThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = receiveMessage();
                        if (message != null) {
                            System.out.println(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            messageReceiverThread.start();

            while (true) {
                System.out.print("");
                String message = consoleInput.readLine();
                sendCommand(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                consoleInput.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

    public void connectToServer(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Conectado al servidor.");
            outputStream = new PrintWriter(socket.getOutputStream(), true);
            outputStream.println(username);
            System.out.println("Nombre de usuario enviado al servidor: " + username);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Flujo de entrada inicializado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al conectar al servidor o inicializar el flujo de entrada:");
            e.printStackTrace();
        }
    }

    public void sendCommand(String command) {
        outputStream.println(command);
    }

    public String receiveMessage() throws IOException {
        return inputStream.readLine();
    }
}
