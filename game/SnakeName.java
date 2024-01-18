import java.awt.*;
import java.io.Serializable;

public class SnakeName extends WorldObject implements Serializable {

    private String name;
    private Position position;

    public SnakeName(String n, Position p) {
        setName(n);
        setPosition(p);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void render(Graphics g, Position cameraPosition) {
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);

        g.setFont(Configuration.nameFont);
        int width = g.getFontMetrics().stringWidth(name);

        g.setColor(Color.darkGray);
        g.fillRect(transformedPosition.getX() - (width / 2) - 10, transformedPosition.getY() - 50, width + 20, 30);
        g.setColor(Color.white);
        g.drawString(name, transformedPosition.getX() - (width / 2), transformedPosition.getY() - 27);
    }
}
