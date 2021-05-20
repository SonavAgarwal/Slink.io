import java.io.Serializable;

public class Snake implements Serializable {

    private String name;
    private int clientID;
    private int size;
    private DLList<SnakeRib> snakeRibs;
    private Game game;

    public Snake(String n, Game g, int cn) {
        name = n;
        snakeRibs = new DLList<SnakeRib>();
        game = g;
        clientID = cn;

        addRib(new SnakeRib(new Position(0, 0)));
        addRib(new SnakeRib(new Position(0, -10)));

        size = 2;
    }

    public void tick() {
        SnakeRib head = snakeRibs.getFirst();
        
        Position gridKey = head.getQuantizedPosition();
        game.getGrid().get(gridKey);

        // grid.get(head.getQuantizedPosition());
    }

    public void addRib(SnakeRib sr) {
        snakeRibs.addAtBeginning(sr);
        Position gridKey = sr.getQuantizedPosition();
        game.getGrid().get(gridKey).addSnakeRib(sr);
    }

    public void removeLastRib() {
        SnakeRib sr = snakeRibs.getLast();
        snakeRibs.removeLast();
        Position gridKey = sr.getQuantizedPosition();
        game.getGrid().get(gridKey).removeSnakeRib(sr);
    }

    public void handleInput(ClientInput input) {
        Position headPosition = snakeRibs.getFirst().getPosition();
        Position newPosition = headPosition.copy().applyChange(input.getMouseAngle() + (Math.PI / 2.0), input.getBoost() ? Configuration.snakeBoostSpeed : Configuration.snakeSpeed);
        SnakeRib newRib = new SnakeRib(newPosition);
        addRib(newRib);
        if (snakeRibs.size() > 5) removeLastRib();
    }

    public void die() {}

    public void grow(int s) {
        size += s;
    }

    public int getClientID() {
        return clientID;
    }

    public Position getHeadPosition() {
        return snakeRibs.getFirst().getPosition();
    }
}
