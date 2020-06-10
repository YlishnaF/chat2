package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;


public class Server {
    private List<ClientHandler> clients;
    private AuthService authService;

    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();
        ServerSocket server = null;
        Socket socket;

        final int PORT = 8189;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился ");
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(String nick, String msg){
        for (ClientHandler c:clients) {
            c.sendMsg(nick + ": " + msg);
        }
    }

    public void privatMsg(ClientHandler sender, String reciver, String msg){
        String message = String.format("[s%] private [s%] : %s", sender, reciver,msg);
        for(ClientHandler c: clients){
            if(c.getNick().equals(reciver)){
                c.sendMsg(message);
                if(!c.getNick().equals(reciver)){
                    sender.sendMsg(message);
                }
                return;
            }
        }
    }




    public void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
