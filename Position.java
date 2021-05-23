import java.io.Serializable;

public class Position implements Serializable {

    private int x, y;

    public Position() {
        x = 0;
        y = 0;
    }

    public Position(int nx, int ny) {
        x = nx;
        y = ny;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        return 10000 * x + y;
    }

    public double distanceTo(Position that) {
        return Math.sqrt(Math.pow(this.getX() - that.getX(), 2) + Math.pow(this.getY() - that.getY(), 2));
    }

    public Position quantized() {
        int nx = (int) Math.floor(x / (double) Configuration.gridSquareWidth) * Configuration.gridSquareWidth;
        int ny = (int) Math.floor(y / (double) Configuration.gridSquareWidth) * Configuration.gridSquareWidth;
        // int nx = x / Configuration.gridSquareWidth * Configuration.gridSquareWidth;
        // int ny = y / Configuration.gridSquareWidth * Configuration.gridSquareWidth;

        return new Position(nx, ny);
    }

    public Position copy() {
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    public Position applyChange(double angle, int distance) {
        x -= (int) (Math.cos(angle) * distance);
        y += (int) (Math.sin(angle) * distance);

        return this;
    }

    public Position shiftRandom(int maxDistance) {
        x += (int) (Math.random() * 2 * maxDistance - maxDistance);
        y += (int) (Math.random() * 2 * maxDistance - maxDistance);
        return this;
    }
}
