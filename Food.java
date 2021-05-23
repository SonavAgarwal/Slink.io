import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Food extends WorldObject implements Serializable {

    private Position position;
    private Color color;
    private int value;

    @Override
    public Position getPosition() {
        return position;
    }

    public Food(Position p, Color c, int v) {
        color = c;
        position = p;
        value = v;
    }

    public Food(Position p, Color c) {
        color = c;
        position = p;
        value = 2;
    }

    public Food(Position p) {
        color = Color.blue;
        position = p;
        value = 2;
    }

    public int getValue() {
        return value;
    }

    public void remove() {}

    @Override
    public String toString() {
        return " (" + position.getX() + ", " + position.getY() + ") ";
    }

    public void render(Graphics g, Position cameraPosition) {
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
        g.setColor(color);
        g.fillOval(transformedPosition.getX(), transformedPosition.getY(), value * 5, value * 5);
    }
}
