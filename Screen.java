import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Screen extends JPanel implements ActionListener {

    // private Game game;
    // private DLList<WorldObject> everything;
    ClientUpdateInfo clientUpdateInfo;
    private String name;
    private int clientID;

    private DLList<GridTile> gridTiles;

    public Screen() {
        setLayout(null);

        gridTiles = new DLList<GridTile>();
        for (int i = -Configuration.worldWidth; i <= Configuration.worldWidth; i++) { //TODO think
            for (int j = -Configuration.worldWidth; j <= Configuration.worldWidth; j++) { //TODO think more
                gridTiles.add(new GridTile(new Position(i * 100, j * 100)));
            }
        }

        this.setFocusable(true);
    }

    public Dimension getPreferredSize() {
        // Sets the size of the panel
        return new Dimension(800, 500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        if (clientUpdateInfo != null) {
            for (GridTile gt : gridTiles) {
                gt.render(g, clientUpdateInfo.getHeadPosition());
            }
            for (WorldObject wo : clientUpdateInfo.getEverything()) {
            wo.render(g, clientUpdateInfo.getHeadPosition());
            }
        }

        // if (everything.size() > 0) {
        //     for ()
        // }
        // if (game != null) game.render(g, clientID);
    }

    public void actionPerformed(ActionEvent e) {}

    public void recordInput(ObjectOutputStream out) {
        Runnable inputRecorder = new InputRecorder(this, out);

        Thread inputRecorderThread = new Thread(inputRecorder);
        inputRecorderThread.start();
    }

    public void poll() throws IOException {
        String hostName = "localhost";
        int portNumber = 1024;
        Socket serverSocket = new Socket(hostName, portNumber);

        ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

        System.out.println("Success");
        recordInput(out);

        repaint();

        while (true) {
            try {
                Object incoming = in.readObject();
                if (incoming.getClass() == Game.class) {
                    Game ga = (Game) incoming;
                    // game = ga;
                    // System.out.println("game gotten");
                    // System.out.println(game);

                } else if (incoming.getClass() == Notification.class) {
                    Notification nf = (Notification) incoming;
                    if (nf.getPurpose().equals("clientID")) {
                        clientID = Integer.parseInt(nf.getText());
                    }
                } else if (incoming.getClass() == ClientUpdateInfo.class) {
                    // System.out.println("GOT SOMETHING\n\n");
                    ClientUpdateInfo cui = (ClientUpdateInfo) incoming;
                    clientUpdateInfo = cui;
                }
                repaint();
            } catch (Exception e) {
                System.out.println(e);
                break;
            }
        }
        //listens for inputs
        // try {
        //     while (true) {
        //         // Message chatMessage = (Message) in.readObject();
        //         // if (chatMessage.getTextLength() > 0) {
        //         //     list.add(chatMessage);
        //         // }

        //         // Collections.sort(list);
        //         // lineCounter = lineCounter + 2 + (chatMessage.getTextLength() / 100);
        //         // while (lineCounter >= 20) {
        //         // 	lineCounter = lineCounter - 2 - (list.get(0).getTextLength() / 100);
        //         // 	list.remove(0);
        //         // }
        //         // System.out.println(chatMessage);
        //         repaint();
        //     }
        // } catch (UnknownHostException e) {
        //     System.err.println("Host unkown: " + hostName);
        //     System.exit(1);
        // } catch (IOException e) {
        //     System.err.println("Couldn't get I/O for the connection to " + hostName);
        //     System.exit(1);
        // } catch (Exception e) {
        //     System.out.println(e);
        // }
    }
}

class InputRecorder implements Runnable, MouseListener {

    JPanel jPanel;
    ObjectOutputStream out;
    boolean boosting = false;

    public InputRecorder(JPanel jp, ObjectOutputStream oos) {
        jPanel = jp;
        out = oos;
        jPanel.addMouseListener(this);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(Configuration.communicationTime);
            } catch (Exception eee) {
                System.out.println(eee);
            }

            // System.out.println("tick");

            ClientInput cin = new ClientInput(0, boosting);

            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, jPanel);

            double angle = Math.atan2(p.getX() - 400, p.getY() - 250);
            cin.setMouseAngle(angle);

            try {
                out.writeObject(cin);
            } catch (Exception ee) {
                System.out.println(ee);
                break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        boosting = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        boosting = false;
    }
}
