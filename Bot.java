import java.awt.*;
import java.io.Serializable;

public class Bot extends WorldObject implements Serializable {

    private Color color;
    private Position position;
    private Position targetPosition;

    private boolean dead = true;

    public Bot() {
        dead = true;
        position = new Position(0, 0);
        targetPosition = new Position(0, 0);
    }

    public void tick(Game game) {
        if (dead) {
            spawn(game);
            return;
        }
        if (position.distanceTo(targetPosition) < 20) {
            setTarget();
        }
        moveTowardsTarget(game);
    }

    public void moveTowardsTarget(Game game) {
        removeFromGrid(game);

        int xDist = targetPosition.getX() - position.getX();
        int xChange = Math.abs(xDist) > 8 ? 8 : 0;
        if (targetPosition.getX() > position.getX()) {
            position.setX(position.getX() + xChange);
        } else {
            position.setX(position.getX() - xChange);
        }

        int yDist = targetPosition.getY() - position.getY();
        int yChange = Math.abs(yDist) > 8 ? 8 : 0;
        if (targetPosition.getY() > position.getY()) {
            position.setY(position.getY() + yChange);
        } else {
            position.setY(position.getY() - yChange);
        }
        addToGrid(game);
    }

    public void setTarget() {
        do {
            targetPosition.setX(position.getX() + ((int) (Math.random() * 1000)) - 500);
            targetPosition.setY(position.getY() + ((int) (Math.random() * 1000)) - 500);
        } while (!Tools.positionIsInWorld(targetPosition));
    }

    public void die() {
        dead = true;
    }

    public void spawn(Game game) {
        dead = false;

        color = new Color((int) (Math.random() * 0x1000000));

        setTarget();

        // removeFromGrid(game);
        position = Tools.randomWorldPosition();
        addToGrid(game);
    }

    public void addToGrid(Game game) {
        Position gridKey = position.quantized();
        GridSquare gs = game.getGrid().get(gridKey);
        if (gs == null) return;

        gs.addBot(this);
    }

    public void removeFromGrid(Game game) {
        Position gridKey = position.quantized();
        GridSquare gs = game.getGrid().get(gridKey);
        if (gs == null) return;
        gs.removeBot(this);
    }

    public void render(Graphics g, Position cameraPosition) {
        Position transformedPosition = Tools.worldToScreenPosition(position, cameraPosition);
        Color tempColor = color.darker().darker();
        // for (int i = 0; i < 5; i++) {
        //     int radius = 20 - i * 2;
        //     tempColor = tempColor.brighter();
        //     g.setColor(tempColor);
        //     g.fillOval(transformedPosition.getX() - radius, transformedPosition.getY() - radius, radius * 2, radius * 2);
        // }
        g.setColor(color);
        g.fillOval(transformedPosition.getX() - 20, transformedPosition.getY() - 20, 20 * 2, 20 * 2);
        g.setColor(Color.white);
        g.fillOval(transformedPosition.getX() - 10, transformedPosition.getY() - 10, 10 * 2, 10 * 2);
    }

    public boolean getDead() {
        return dead;
    }

    public Position getPosition() {
        return position;
    }
}
