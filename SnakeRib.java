import java.awt.*;
import java.io.Serializable;

public class SnakeRib extends WorldObject implements Serializable {

    private Position position;
    private Snake snake;
    private Color color;
    private int snakeNumber;

    public SnakeRib(Position p, Color c, int sn) {
        position = p;
        color = c;
        snakeNumber = sn;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public Position getQuantizedPosition() {
        return position.quantized();
    }

    public void checkFood(Food f) {
        if (position.distanceTo(f.getPosition()) < 30) {
            f.remove();
            snake.grow(1);
        }
    }

    public void checkRib(SnakeRib that) {
        if (position.distanceTo(that.getPosition()) < 50) {
            die();
        }
    }

    public void die() {
        snake.die();
    }

    @Override
    public int hashCode() {
        return position.quantized().hashCode();
    }

    public void render(Graphics g, Position cameraPosition) {
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
        g.setColor(color);
        g.fillOval(transformedPosition.getX() - 20, transformedPosition.getY() - 20, 40, 40);
    }
}
