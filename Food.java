import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Food extends WorldObject implements Serializable {

    private Position position;
    private Color color;

    public Position getPosition() {
        return position;
    }

    public Food(Position p, Color c) {
        color = c;
        position = p;
    }

    public Food(Position p) {
        color = Color.blue;
        position = p;
    }

    public void remove() {}

    @Override
    public String toString() {
        return " (" + position.getX() + ", " + position.getY() + ") ";
    }

    public void render(Graphics g, Position cameraPosition) {
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
        g.setColor(color);
        g.fillOval(transformedPosition.getX(), transformedPosition.getY(), 20, 20);
    }
}
