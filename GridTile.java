import java.awt.*;

public class GridTile extends WorldObject {

    Position position;

    public GridTile(Position p) {
        position = p;
    }

    public void render(Graphics g, Position cameraPosition) {
        // if (cameraPosition.distanceTo(position) > 300) return;

        g.setColor(Color.green);
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
        g.drawRect((transformedPosition.getX()), transformedPosition.getY(), Configuration.gridSquareWidth, Configuration.gridSquareWidth);
    }
}
