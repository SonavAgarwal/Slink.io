import java.awt.*;
import java.io.Serializable;

public class GridSquare extends WorldObject implements Serializable {

    private Position position;
    private final int width = 100;

    private DLList<Food> foods;
    private DLList<SnakeRib> snakeRibs;

    private Game game;

    public GridSquare(Position p, Game g) {
        position = p;
        snakeRibs = new DLList<SnakeRib>();
        foods = new DLList<Food>();
        game = g;
    }

    public void addFood(Food f) {
        // if (foods.size() < 1)
        foods.add(f);
        game.increaseFoodCount();
    }

    public void removeFood(Food f) {
        if (foods.remove(f)) {
            game.decreaseFoodCount();
        }
    }

    public void addSnakeRib(SnakeRib sr) {
        snakeRibs.add(sr);
    }

    public void removeSnakeRib(SnakeRib sr) {
        snakeRibs.remove(sr);
    }

    public void checkHead(Snake snake, SnakeRib headRib) {
        for (Food f : foods) {
            if (headRib.getPosition().distanceTo(f.getPosition()) < 40) {
                foods.remove(f);
                snake.grow(f.getValue());
            }
        }
        for (SnakeRib sr : snakeRibs) {
            if (sr.getSnakeID() != headRib.getSnakeID()) {
                if (headRib.getPosition().distanceTo(sr.getPosition()) < 10) {
                    snake.die();
                }
            }
        }
    }

    // public void render(Graphics g, Position cameraPosition) {
    //     // if (position.distanceTo(cameraPosition) > 200) return;

    //     g.setColor(Color.green);
    //     Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
    //     g.drawRect((transformedPosition.getX()), transformedPosition.getY(), 100, 100);

    //     for (SnakeRib sr : snakeRibs) {
    //         sr.render(g, cameraPosition);
    //     }
    //     for (Food f : foods) {
    //         f.render(g, cameraPosition);
    //     }
    // }

    @Override
    public String toString() {
        return foods.size() + " = food size; " + foods.toString() + " ";
    }

    public DLList<WorldObject> getEverything() {
        DLList<WorldObject> everything = new DLList<WorldObject>();
        for (WorldObject wo : snakeRibs) {
            everything.add(wo);
        }
        for (WorldObject wo : foods) {
            everything.add(wo);
        }
        // everything.add(snakeRibs);
        // everything.add(foods);
        return everything;
    }
}
