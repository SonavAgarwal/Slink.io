import java.awt.*;

public class GridTile extends WorldObject {

    private Position position;
    private Color color = new Color(130, 130, 130);

    public GridTile(Position p) {
        position = p;
    }

    public void render(Graphics g, Position cameraPosition) {
        // if (cameraPosition.distanceTo(position) > 300) return;

        g.setColor(color);
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
        g.drawRect((transformedPosition.getX()), transformedPosition.getY(), Configuration.gridSquareWidth, Configuration.gridSquareWidth);
    }
}
