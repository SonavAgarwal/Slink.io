import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
// import java.util.*;
import javax.swing.*;

public class Screen extends JPanel implements ActionListener, KeyListener {

    private Color backgroundColor = new Color(20, 20, 20);
    private Color overlayColor = new Color(255, 255, 255, 30);
    private Color fontColor = new Color(77, 77, 77);
    private Font font = new Font("Arial", Font.BOLD, 15);

    private JButton playButton;
    private JTextField nameInput;

    private JLabel instructionsLabel;
    private JLabel instructionsLabel1;
    private JLabel instructionsLabel2;
    private JLabel instructionsLabel3;
    private JLabel gameNameLabel;

    private DLList<JComponent> jComponents;

    ObjectOutputStream out = null;
    // private Game game;
    // private DLList<WorldObject> everything;
    ClientUpdateInfo clientUpdateInfo;
    private String name;
    private int clientID;

    private DLList<GridTile> gridTiles;
    private MiniMap map;

    private boolean instructed = false;

    public Screen() {
        setLayout(null);

        gridTiles = new DLList<GridTile>();
        for (int i = -Configuration.worldWidth; i <= Configuration.worldWidth; i++) { //TODO think
            for (int j = -Configuration.worldWidth; j <= Configuration.worldWidth; j++) { //TODO think more
                Position newPos = new Position(i * Configuration.gridSquareWidth, j * Configuration.gridSquareWidth);

                // if (newPos.distanceTo(new Position(0, 0)) < 1000) {
                gridTiles.add(new GridTile(new Position(i * Configuration.gridSquareWidth, j * Configuration.gridSquareWidth)));
                // }
            }
        }

        map = new MiniMap();

        jComponents = new DLList<JComponent>();

        playButton = new JButton();
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setHorizontalAlignment(SwingConstants.CENTER);
        playButton.setBounds(310, 228, 200, 30);
        playButton.setText("Play!");
        this.add(playButton);
        playButton.addActionListener(this);
        playButton.setVisible(true);

        nameInput = new JTextField();
        nameInput.setFont(new Font("Arial", Font.PLAIN, 20));
        nameInput.setHorizontalAlignment(SwingConstants.CENTER);
        nameInput.setBounds(310, 186, 200, 30);
        nameInput.setText("Name...");
        this.add(nameInput);

        instructionsLabel = new JLabel();
        instructionsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionsLabel.setBounds(255, 313, 300, 30);
        instructionsLabel.setText("Instructions:");
        this.add(instructionsLabel);

        instructionsLabel1 = new JLabel();
        instructionsLabel1.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionsLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        instructionsLabel1.setBounds(255, 356, 300, 30);
        instructionsLabel1.setText("- click / space to sprint");
        this.add(instructionsLabel1);

        instructionsLabel2 = new JLabel();
        instructionsLabel2.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionsLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        instructionsLabel2.setBounds(255, 396, 300, 30);
        instructionsLabel2.setText("- move mouse and eat food");
        this.add(instructionsLabel2);

        instructionsLabel3 = new JLabel();
        instructionsLabel3.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionsLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        instructionsLabel3.setBounds(255, 436, 300, 30);
        instructionsLabel3.setText("- avoid other snakes and walls");
        this.add(instructionsLabel3);

        gameNameLabel = new JLabel();
        gameNameLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gameNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameNameLabel.setBounds(255, 80, 300, 50);
        gameNameLabel.setText("Slink");
        this.add(gameNameLabel);

        instructionsLabel.setForeground(Color.white);
        instructionsLabel1.setForeground(Color.white);
        instructionsLabel2.setForeground(Color.white);
        instructionsLabel3.setForeground(Color.white);
        gameNameLabel.setForeground(Color.white);

        jComponents.add(playButton);
        jComponents.add(nameInput);
        jComponents.add(instructionsLabel);
        jComponents.add(instructionsLabel1);
        jComponents.add(instructionsLabel2);
        jComponents.add(instructionsLabel3);
        jComponents.add(gameNameLabel);

        // frame.addComponentListener(new ComponentAdapter() {
        //     public void componentResized(ComponentEvent componentEvent) {
        //         // do stuff
        //         componentEvent.
        //     }
        // });

        this.addKeyListener(this);

        this.setFocusable(true);
    }

    public Dimension getPreferredSize() {
        // Sets the size of the panel
        return new Dimension(800, 500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(backgroundColor);
        g.fillRect(0, 0, 2000, 2000);

        if (clientUpdateInfo != null) {
            if (clientUpdateInfo.getDead()) {
                for (JComponent jc : jComponents) {
                    jc.setVisible(true);
                }
            } else {
                for (JComponent jc : jComponents) {
                    jc.setVisible(false);
                }
                instructed = true;
            }

            for (GridTile gt : gridTiles) {
                gt.render(g, clientUpdateInfo.getHeadPosition());
            }
            for (WorldObject wo : clientUpdateInfo.getEverything()) {
                wo.render(g, clientUpdateInfo.getHeadPosition());
            }

            g.setColor(fontColor);
            g.setFont(font);
            g.drawString("Size: " + clientUpdateInfo.getSize() + ", Position: " + clientUpdateInfo.getHeadPosition().toString(), 10, 20);

            map.render(g, clientUpdateInfo.getHeadPosition(), clientUpdateInfo.getMapObjects());

            if (clientUpdateInfo.getDead()) {
                g.setColor(overlayColor);
                g.fillRect(0, 0, 2000, 2000);
            }
        }
        // if (everything.size() > 0) {
        //     for ()
        // }
        // if (game != null) game.render(g, clientID);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            name = nameInput.getText();
            sendServerUpdate(new ServerUpdateInfo(2, name));
        }
    }

    public void recordInput(ObjectOutputStream out) {
        Runnable inputRecorder = new InputRecorder(this, out);

        this.addKeyListener((KeyListener) inputRecorder);

        Thread inputRecorderThread = new Thread(inputRecorder);
        inputRecorderThread.start();
    }

    public void poll() throws IOException {
        String hostName = "192.168.50.106";
        int portNumber = 1024;
        System.out.println("x");
        Socket serverSocket = new Socket(hostName, portNumber);
        System.out.println("y");

        out = new ObjectOutputStream(serverSocket.getOutputStream());
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
                    if (cui.getHeadPosition() == null) cui.setHeadPosition(clientUpdateInfo.getHeadPosition());
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

    public void sendServerUpdate(ServerUpdateInfo sui) {
        try {
            out.writeObject(sui);
        } catch (Exception ee) {
            System.out.println(ee);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'd') {
            sendServerUpdate(new ServerUpdateInfo(1));
        } else if (e.getKeyChar() == 'g') {
            sendServerUpdate(new ServerUpdateInfo(3));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}

class InputRecorder implements Runnable, MouseListener, KeyListener {

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
                Thread.sleep(Configuration.inputTime);
            } catch (Exception eee) {
                System.out.println(eee);
            }

            // System.out.println("tick");

            ClientInput cin = new ClientInput(0, boosting);

            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, jPanel);

            double angle = Math.atan2(p.getX() - 400, p.getY() - 250);
            // System.out.println(angle);
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

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            boosting = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            boosting = false;
        }
    }
}
