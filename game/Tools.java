public class Tools {

    public static Position worldToScreenPosition(Position p, Position cameraPosition) {
        return new Position(400 + p.getX() - cameraPosition.getX(), 250 + p.getY() - cameraPosition.getY());
    }

    public static Position randomWorldPosition() {
        Position randomPosition = new Position(
            (int) (
                Math.random() *
                Configuration.worldWidth *
                Configuration.gridSquareWidth *
                2 -
                Configuration.worldWidth *
                Configuration.gridSquareWidth
            ),
            (int) (
                Math.random() *
                Configuration.worldWidth *
                Configuration.gridSquareWidth *
                2 -
                Configuration.worldWidth *
                Configuration.gridSquareWidth
            )
        );
        return randomPosition;
    }

    public static boolean positionIsInWorld(Position p) {
        int maxW = Configuration.worldWidth * Configuration.gridSquareWidth;
        if (p.getX() < -1 * maxW || p.getX() > maxW || p.getY() < -1 * maxW || p.getY() > maxW) {
            return false;
        }
        return true;
    }
}
