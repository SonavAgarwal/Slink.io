public class Tools {

    public static Position worldToScreenPosition(Position p, Position cameraPosition) {
        return new Position(400 + p.getX() - cameraPosition.getX(), 250 + p.getY() - cameraPosition.getY());
    }
}
