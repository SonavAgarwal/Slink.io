import java.awt.*;
import java.io.Serializable;

public class Game implements Serializable {

    private DLList<Snake> snakes;
    private HashMap<Position, GridSquare> grid;

    private int foodCount;

    public Game() {
        snakes = new DLList<Snake>();
        grid = new HashMap<Position, GridSquare>();

        for (int i = -Configuration.worldWidth; i <= Configuration.worldWidth; i++) { //TODO think
            for (int j = -Configuration.worldWidth; j <= Configuration.worldWidth; j++) { //TODO think more
                Position newPos = new Position(i * Configuration.gridSquareWidth, j * Configuration.gridSquareWidth);
                grid.put(newPos, new GridSquare(newPos, this));
            }
        }
    }

    public void addSnake(Snake s) {
        snakes.add(s);
    }

    public void removeSnake(Snake s) {
        snakes.remove(s);
    }

    public boolean addSnakeRib(SnakeRib sr) {
        Position gridKey = sr.getQuantizedPosition();
        GridSquare gs = grid.get(gridKey);
        if (gs != null) {
            gs.addSnakeRib(sr);
            return true;
        } else {
            return false;
        }
    }

    public void addFood(Food f) {
        Position gridKey = f.getPosition().quantized();
        GridSquare gs = grid.get(gridKey);
        if (gs != null) {
            gs.addFood(f);
        }
    }

    public HashMap<Position, GridSquare> getGrid() {
        return grid;
    }

    public void tick() {
        if (Math.random() < 0.3) {
            spawnFood();
        }

        // grid.get(new Position(0, 0)).addFood(new Food(new Position(10, 10)));

        for (Snake s : snakes) {
            s.tick();
        }
        // food spawn algorithm
    }

    public void spawnFood() {
        if (foodCount > 500) return;
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
                int randomSize = (int) (Math.random() * 3 + 2);
                grid.get(randomPosition.quantized()).addFood(new Food(randomPosition, Color.white, randomSize));
            }
        } catch (Exception ee) {}
    }

    // public void render(Graphics g, int clientID) {
    //     Position cameraPosition = new Position(0, 0);
    //     for (Snake s : snakes) {
    //         if (s.getClientID() == clientID) cameraPosition = s.getHeadPosition();
    //     }

    //     DLList<Position> gridSquarePositions = grid.keySet();
    //     // System.out.println(gridSquarePositions);
    //     for (Position gsp : gridSquarePositions) {
    //         // System.out.println(gsp);
    //         // System.out.println(cameraPosition);
    //         // System.out.println(gsp.distanceTo(cameraPosition));
    //         if (gsp.distanceTo(cameraPosition) < 500) {
    //             grid.get(gsp).render(g, cameraPosition);
    //         }
    //     }
    // }

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

    public void increaseFoodCount() {
        foodCount++;
    }

    public void decreaseFoodCount() {
        foodCount--;
    }
}
