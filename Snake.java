import java.awt.Color;
import java.io.Serializable;

public class Snake implements Serializable {

    private String name;
    private int clientID;
    private int size;
    private DLList<SnakeRib> snakeRibs;
    private Game game;

    private Position headPosition;
    private Color color;

    private boolean dead;

    private double currentAngle;
    private double mouangsav;
    private int lastChangeDirection;

    private int maxSpeed;
    private double maxTurn;

    public Snake(String n, Game g, int cn) {
        name = n;
        snakeRibs = new DLList<SnakeRib>();
        game = g;
        clientID = cn;

        headPosition = new Position(0, 0);
        dead = true;
        // spawn();
    }

    public void tick() {
        if (dead) return;
        try {
            SnakeRib head = snakeRibs.getFirst();
            Position gridKey = head.getQuantizedPosition();
            GridSquare gs = game.getGrid().get(gridKey);
            if (gs != null) gs.checkHead(this, head);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addRib(SnakeRib sr) {
        boolean success = game.addSnakeRib(sr);
        if (success) {
            snakeRibs.addAtBeginning(sr);
        } else {
            die();
        }
    }

    public void removeLastRib() {
        // System.out.println("remove last rib");
        SnakeRib sr = snakeRibs.getLast();
        snakeRibs.removeLast();
        Position gridKey = sr.getQuantizedPosition();
        game.getGrid().get(gridKey).removeSnakeRib(sr);
    }

    public double cap(double number, double min, double max) {
        if (number > max) return max;
        if (number < min) return min;
        return number;
    }

    public double mod(double a, double b) {
        double i = a % b;
        if (i < 0) i += b;
        return i;
    }

    public int turnDirection(double current, double target) {
        double d = target - current;
        if (d < 0) d += 360;
        if (d > 180) return -1; // left turn
        else return 1; // right turn
    }

    public double getMouangsav() {
        return mouangsav;
    }

    private double turnable = 0.8;

    public void handleInput(ClientInput input) {
        if (dead) return;

        double pi2 = 2 * Math.PI;

        double transformedAngle = input.getMouseAngle() + (Math.PI / 2.0);
        transformedAngle = mod(transformedAngle, pi2);

        double dMa = transformedAngle - currentAngle;
        int direction = 1;

        dMa = mod(dMa, pi2);
        if (dMa > Math.PI) {
            direction = -1;
        }

        dMa = cap(dMa, 0, maxTurn);

        // ClientInput input = in;
        // transform input angle
        // input.setMouseAngle(input.getMouseAngle() + (Math.PI / 2.0));
        // input.setMouseAngle(mod(input.getMouseAngle(), pi2));

        // mouangsav = input.getMouseAngle();

        // // System.out.println(mod(-0.5, pi2));

        // // dMa *=

        // dMa = cap(dMa, 0, turnable);
        // // if (dMa > 0) lastChangeDirection = 1; else lastChangeDirection = -1;
        // // if (dMa < 0.1) dMa = 0;

        double ma = dMa * direction + currentAngle;
        // double ma = input.getMouseAngle() + (Math.PI / 2.0);
        int dist = maxSpeed + (input.getBoost() && size > 30 ? 2 : 0);
        headPosition.applyChange(ma, dist);
        Position newPosition = headPosition.copy();

        SnakeRib newRib = new SnakeRib(newPosition, color, clientID, getThickness());
        addRib(newRib);
        if (input.getBoost() && size > 30) {
            size--;
            game.addFood(new Food(snakeRibs.getLast().getPosition(), color.brighter(), 1));
        }

        while (snakeRibs.size() > (size / 5)) {
            removeLastRib();
        }
        // if (snakeRibs.size() > (size / 5)) {
        //     System.out.println("here");
        //     removeLastRib();
        // }

        mouangsav = ma;

        currentAngle = ma;
    }

    public void setMaxSpeed() {
        int speed = Configuration.snakeSpeed;
        speed -= size / 250;
        if (speed < 4) speed = 4;
        maxSpeed = speed;
    }

    public void setMaxTurn() {
        double turn = 0.3;
        turn -= size / 5000.0;
        if (turn < 0.03) turn = 0.03;
        maxTurn = turn;
    }

    public void die() {
        dead = true;
        while (snakeRibs.size() > 0) {
            // System.out.println("Snake is dying");
            SnakeRib sr = snakeRibs.getLast();
            snakeRibs.removeLast();
            Position gridKey = sr.getQuantizedPosition();
            // System.out.println(sr.getPosition());
            // System.out.println(sr.getQuantizedPosition());
            GridSquare gs = game.getGrid().get(gridKey);
            gs.removeSnakeRib(sr);
            gs.addFood(new Food(sr.getPosition().shiftRandom(20), color.brighter(), 4));
            size -= 4;
        }
    }

    public void spawn() {
        color = new Color((int) (Math.random() * 0x1000000));

        headPosition = new Position(0, 0);
        currentAngle = 0;
        lastChangeDirection = 1;

        addRib(new SnakeRib(new Position(0, 0), color, clientID, 20));
        addRib(new SnakeRib(new Position(0, -10), color, clientID, 20));
        dead = false;
        size = 200;

        setMaxSpeed();
        setMaxTurn();
    }

    public void grow(int s) {
        size += s;

        setMaxTurn();
        setMaxSpeed();
    }

    public SnakeName getNameObject() {
        if (dead) return null;
        return new SnakeName(name, getHeadPosition());
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClientID() {
        return clientID;
    }

    public boolean getDead() {
        return dead;
    }

    public Position getHeadPosition() {
        return headPosition;
        // if (dead) return null;
        // return snakeRibs.getFirst().getPosition();
    }

    public int getSize() {
        return size;
    }

    public int getThickness() {
        return (size / 20) + 5;
    }

    public double getCurrentAngle() {
        return currentAngle;
    }
}
