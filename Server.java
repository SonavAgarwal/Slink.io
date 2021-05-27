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

    public Game getGame() {
        return game;
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

    private int ticksWithoutInput;
    private boolean connected = false;

    public ClientHandler(Socket cs, int cn, ServerHandler sh, Game game) {
        clientSocket = cs;
        clientNumber = cn;
        serverHandler = sh;

        snake = new Snake("Jimmy", game, clientNumber);
        game.addSnake(snake);

        latestInput = new ClientInput(0, false);
        ticksWithoutInput = 0;

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(new Notification(clientNumber + "", "clientID"));
        } catch (Exception e) {
            System.out.println(e);
            // connected = false;
        }
    }

    public void tick() {
        // if (!connected) return;
        ticksWithoutInput++;
        if (ticksWithoutInput > 100) {
            // serverHandler.removeClient(this);
            snake.die();
            // serverHandler.getGame().removeSnake(snake);
        } else {
            snake.handleInput(latestInput);
        }
        // System.out.println("snake tick");
    }

    public void updateClient(DLList<WorldObject> everything) {
        try {
            out.reset();
            ClientUpdateInfo cui = new ClientUpdateInfo();
            cui.setHeadPosition(snake.getHeadPosition());
            cui.setSize(snake.getSize());
            cui.setDead(snake.getDead());
            cui.set("snakeAngle", snake.getCurrentAngle());
            cui.set("mouseAngle", snake.getMouangsav());
            cui.setEverything(everything); // last
            out.writeObject(cui);
        } catch (Exception e) {
            System.out.println(e);
            // connected = false;
            System.out.println("adios");
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
                        Object incoming = in.readObject();
                        if (incoming.getClass() == ClientInput.class) {
                            ClientInput incomingInput = (ClientInput) incoming;
                            latestInput = incomingInput;
                            ticksWithoutInput = 0;
                            updateClient(serverHandler.getEverything());
                        } else if (incoming.getClass() == ServerUpdateInfo.class) {
                            ServerUpdateInfo sui = (ServerUpdateInfo) incoming;

                            if (sui.getAction() == 1) {
                                snake.die();
                            } else if (sui.getAction() == 2) {
                                snake.spawn();
                            } else if (sui.getAction() == 3) {
                                snake.grow(20);
                            }
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("bye");
                    serverHandler.removeClient(this);
                    break;
                }
            }
        } catch (Exception eeee) {
            System.out.println(eeee);
            System.out.println("zaijian");
            serverHandler.removeClient(this);
        }
    }
    // public void handleMessage() throws Exception {

    // }
}
