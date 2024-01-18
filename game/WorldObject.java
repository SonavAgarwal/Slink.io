import java.awt.*;

public class WorldObject {

    @Override
    public String toString() {
        return "worldobject";
    }

    public Position getPosition() {
        return new Position(0, 0);
    }

    public void render(Graphics g, Position cameraPosition) {
        return;
    }
}
