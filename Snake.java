import java.awt.Color;
import java.io.Serializable;

public class Snake implements Serializable {

    private String name;
    private int clientID;
    private int size;
    private DLList<SnakeRib> snakeRibs;
    private Game game;

    private boolean dead;

    public Snake(String n, Game g, int cn) {
        name = n;
        snakeRibs = new DLList<SnakeRib>();
        game = g;
        clientID = cn;

        addRib(new SnakeRib(new Position(0, 0)));
        addRib(new SnakeRib(new Position(0, -10)));
        dead = false;
        size = 5;
    }

    public void tick() {
        if (dead) return;
        SnakeRib head = snakeRibs.getFirst();
        Position gridKey = head.getQuantizedPosition();
        GridSquare gs = game.getGrid().get(gridKey);
        if (gs != null) gs.checkHead(this, head);
        // grid.get(head.getQuantizedPosition());
    }

    public void addRib(SnakeRib sr) {
        Position gridKey = sr.getQuantizedPosition();
        GridSquare gs = game.getGrid().get(gridKey);
        if (gs != null) gs.addSnakeRib(sr);
        // game.getGrid().get(gridKey).addSnakeRib(sr);
        snakeRibs.addAtBeginning(sr);
    }

    public void removeLastRib() {
        SnakeRib sr = snakeRibs.getLast();
        snakeRibs.removeLast();
        Position gridKey = sr.getQuantizedPosition();
        game.getGrid().get(gridKey).removeSnakeRib(sr);
    }

    public void handleInput(ClientInput input) {
        Position headPosition = snakeRibs.getFirst().getPosition();
        Position newPosition = headPosition
            .copy()
            .applyChange(
                input.getMouseAngle() + (Math.PI / 2.0),
                input.getBoost() ? Configuration.snakeBoostSpeed : Configuration.snakeSpeed
            );
        SnakeRib newRib = new SnakeRib(newPosition);
        addRib(newRib);
        if (snakeRibs.size() > size) removeLastRib();
    }

    public void die() {
        while (snakeRibs.size() > 0) {
            SnakeRib sr = snakeRibs.getLast();
            snakeRibs.removeLast();
            Position gridKey = sr.getQuantizedPosition();
            GridSquare gs = game.getGrid().get(gridKey);
            gs.removeSnakeRib(sr);
            gs.addFood(new Food(sr.getPosition().shiftRandom(20), Color.red));
        }
    }

    public void grow(int s) {
        size += s;
    }

    public int getClientID() {
        return clientID;
    }

    public Position getHeadPosition() {
        return snakeRibs.getFirst().getPosition();
    }

    public int getSize() {
        return size;
    }
}
