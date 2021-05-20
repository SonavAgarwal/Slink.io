import java.awt.*;
import java.io.Serializable;

public class SnakeRib extends WorldObject implements Serializable {

    private Position position;
    private Snake snake;

    public SnakeRib(Position p) {
        position = p;
    }

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
        g.setColor(Color.BLACK);
        g.fillOval(transformedPosition.getX() - 20, transformedPosition.getY() - 20, 40, 40);
    }
}
