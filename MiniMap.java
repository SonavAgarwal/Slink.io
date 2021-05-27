import java.awt.*;

public class MiniMap {

    Color color = new Color(30, 43, 32);

    public void render(Graphics g, Position pos) {
        g.setColor(color);
        g.fillRect(710, 410, 80, 80);

        int x = 40 * pos.getX() / (Configuration.worldWidth * Configuration.gridSquareWidth);
        int y = 40 * pos.getY() / (Configuration.worldWidth * Configuration.gridSquareWidth);

        g.setColor(Color.white);
        g.drawOval(750 + x, 450 + y, 2, 2);
    }
}
