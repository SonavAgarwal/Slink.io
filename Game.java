import java.awt.*;
import java.io.Serializable;

public class Game implements Serializable {

    private DLList<Snake> snakes;
    private HashMap<Position, GridSquare> grid;

    public Game() {
        snakes = new DLList<Snake>();
        grid = new HashMap<Position, GridSquare>();

        for (int i = -Configuration.worldWidth; i <= Configuration.worldWidth; i++) { //TODO think
            for (int j = -Configuration.worldWidth; j <= Configuration.worldWidth; j++) { //TODO think more
                grid.put(
                    new Position(i * 100, j * 100),
                    new GridSquare(new Position(i * Configuration.gridSquareWidth, j * Configuration.gridSquareWidth))
                );
            }
        }
    }

    public void addSnake(Snake s) {
        snakes.add(s);
    }

    public void removeSnake(Snake s) {
        snakes.remove(s);
    }

    public HashMap<Position, GridSquare> getGrid() {
        return grid;
    }

    public void tick() {
        if (Math.random() < 0.1) {
            try {
                Position randomPosition = new Position(
                    (int) (
                        Math.random() *
                        Configuration.worldWidth *
                        Configuration.gridSquareWidth *
                        2 -
                        Configuration.worldWidth *
                        Configuration.gridSquareWidth
                    ),
                    (int) (
                        Math.random() *
                        Configuration.worldWidth *
                        Configuration.gridSquareWidth *
                        2 -
                        Configuration.worldWidth *
                        Configuration.gridSquareWidth
                    )
                );
                if (randomPosition.distanceTo(new Position(0, 0)) + 100 < Configuration.worldWidth * Configuration.gridSquareWidth) {
                    grid.get(randomPosition.quantized()).addFood(new Food(randomPosition));
                }
            } catch (Exception ee) {}
        }

        // grid.get(new Position(0, 0)).addFood(new Food(new Position(10, 10)));

        for (Snake s : snakes) {
            s.tick();
        }
        // food spawn algorithm
    }

    public void render(Graphics g, int clientID) {
        Position cameraPosition = new Position(0, 0);
        for (Snake s : snakes) {
            if (s.getClientID() == clientID) cameraPosition = s.getHeadPosition();
        }

        DLList<Position> gridSquarePositions = grid.keySet();
        // System.out.println(gridSquarePositions);
        for (Position gsp : gridSquarePositions) {
            // System.out.println(gsp);
            // System.out.println(cameraPosition);
            // System.out.println(gsp.distanceTo(cameraPosition));
            if (gsp.distanceTo(cameraPosition) < 500) {
                grid.get(gsp).render(g, cameraPosition);
            }
        }
    }

    public String toString() {
        String hold = "";

        hold += "grid squares length: " + grid.keySet().size() + "\n";
        for (Position p : grid.keySet()) {
            hold += "" + grid.get(p) + "\n";
        }

        return hold;
    }

    public DLList<WorldObject> getEverything() {
        DLList<WorldObject> everything = new DLList<WorldObject>();
        DLList<Position> gridSquarePositions = grid.keySet();
        for (Position gsp : gridSquarePositions) {
            // if (gsp.distanceTo(cameraPosition) < 500) {
            // everything.add(grid.get(gsp));
            everything.add(grid.get(gsp).getEverything());
            // }
        }
        return everything;
    }
}
