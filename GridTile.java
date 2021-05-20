import java.awt.*;

public class GridTile extends WorldObject  {

    Position position;

    public GridTile(Position p) {
        position = p;
    }

    public void render(Graphics g, Position cameraPosition) {
        g.setColor(Color.green);
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
        g.drawRect((transformedPosition.getX()), transformedPosition.getY(), 100, 100);
    }
}
