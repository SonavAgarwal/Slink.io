import java.awt.*;

public class MiniMap {
    Color color = new Color(30, 43, 32);

    public void render(Graphics g, Position pos) {
        g.setColor(color);
        g.fillRect(10, 10, 80, 80);

        int x = pos.getX() / (Configuration.worldWidth * Configuration.gridSquareWidth);
        int y = pos.getY() / (Configuration.worldWidth * Configuration.gridSquareWidth);

        g.setColor(Color.white);
        g.drawOval(50 + x, 50 + y, 2, 2);
    }
}
