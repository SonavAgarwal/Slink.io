import java.io.*;
import java.net.*;

public class Server {

    public static void main(String args[]) throws IOException {
        ServerHandler sh = new ServerHandler();
        sh.startGame();
        sh.start();
    }
}

class ServerHandler {

    private int portNumber = 1024;
    private int clientCount = 1;

    private DLList<ClientHandler> clients;

    private Game game;

    private DLList<WorldObject> everything;

    public ServerHandler() {
        clientCount = 0;
        game = new Game();
        clients = new DLList<ClientHandler>();
    }

    public void removeClient(ClientHandler ch) {
        System.out.println(clients.size());
        clients.remove(ch);
        System.out.println(clients.size());
    }

    public void startGame() {
        Runnable gameUpdater = new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(Configuration.communicationTime);
                    } catch (Exception eee) {
                        System.out.println(eee);
                    }

                    // System.out.println("tick");

                    game.tick();

                    for (ClientHandler ch : clients) {
                        ch.tick();
                    }

                    everything = game.getEverything();
                    // for (ClientHandler ch : clients) {

                    //     ch.updateClient(everything);
                    // }
                }
            }
        };

        Thread gameUpdateThread = new Thread(gameUpdater);
        gameUpdateThread.start();
    }

    public DLList<WorldObject> getEverything() {
        return everything;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        while (true) {
            clientCount++;
            Socket clientSocket = serverSocket.accept();
            ClientHandler ch = new ClientHandler(clientSocket, clientCount, this, game);
            Runnable r = (Runnable) ch;
            clients.add(ch);
            Thread thread = new Thread(r);
            thread.start();

            System.out.println("New client!");
        }
    }
}

class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String playerName;
    private int clientNumber;
    private ServerHandler serverHandler;
    private ObjectOutputStream out;

    private Snake snake;

    private ClientInput latestInput;

    public ClientHandler(Socket cs, int cn, ServerHandler sh, Game game) {
        clientSocket = cs;
        clientNumber = cn;
        serverHandler = sh;

        snake = new Snake("Jimmy", game, clientNumber);
        game.addSnake(snake);

        latestInput = new ClientInput(0, false);

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(new Notification(clientNumber + "", "clientID"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void tick() {
        snake.handleInput(latestInput);
    }

    public void updateClient(DLList<WorldObject> everything) {
        try {
            out.reset();
            ClientUpdateInfo cui = new ClientUpdateInfo(everything, snake.getHeadPosition());
            out.writeObject(cui);
        } catch (Exception e) {
            System.out.println(e);
            serverHandler.removeClient(this);
        }
    }

    public void run() {
        ObjectInputStream in = null;
        PushbackInputStream pin = null;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            pin = new PushbackInputStream(clientSocket.getInputStream());
            while (true) {
                try {
                    if (pin.available() != 0) {
                        ClientInput incomingInput;
                        incomingInput = (ClientInput) in.readObject();
                        latestInput = incomingInput;
                        updateClient(serverHandler.getEverything());
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    serverHandler.removeClient(this);
                    break;
                }
            }
        } catch (Exception eeee) {
            System.out.println(eeee);
            serverHandler.removeClient(this);
        }
    }
    // public void handleMessage() throws Exception {

    // }
}
