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

    public Snake(String n, Game g, int cn) {
        name = n;
        snakeRibs = new DLList<SnakeRib>();
        game = g;
        clientID = cn;

        color = new Color((int) (Math.random() * 0x1000000));

        headPosition = new Position(0, 0);
        addRib(new SnakeRib(new Position(0, 0), color));
        addRib(new SnakeRib(new Position(0, -10), color));
        dead = false;
        size = 15;
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

    public void handleInput(ClientInput input) {
        if (dead) return;
        double ma = input.getMouseAngle() + (Math.PI / 2.0);
        int dist = input.getBoost() && size > 10 ? Configuration.snakeBoostSpeed : Configuration.snakeSpeed;
        headPosition.applyChange(ma, dist);
        Position newPosition = headPosition.copy();

        SnakeRib newRib = new SnakeRib(newPosition, color);
        addRib(newRib);
        if (input.getBoost() && size > 10) {
            size--;
            game.addFood(new Food(snakeRibs.getLast().getPosition(), color.brighter(), 1));
        }

        System.out.println(snakeRibs.size());
        while (snakeRibs.size() > (size / 5)) {
            removeLastRib();
        }
        // if (snakeRibs.size() > (size / 5)) {
        //     System.out.println("here");
        //     removeLastRib();
        // }
    }

    public void die() {
        dead = true;
        while (snakeRibs.size() > 0) {
            SnakeRib sr = snakeRibs.getLast();
            snakeRibs.removeLast();
            Position gridKey = sr.getQuantizedPosition();
            GridSquare gs = game.getGrid().get(gridKey);
            gs.removeSnakeRib(sr);
            gs.addFood(new Food(sr.getPosition().shiftRandom(20), color.brighter(), 4));
            size -= 4;
        }
    }

    public void grow(int s) {
        size += s;
    }

    public int getClientID() {
        return clientID;
    }

    public Position getHeadPosition() {
        return headPosition;
        // if (dead) return null;
        // return snakeRibs.getFirst().getPosition();
    }

    public int getSize() {
        return size;
    }
}
