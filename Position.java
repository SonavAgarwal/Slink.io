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
        return 31 * x + y;
    }

    public double distanceTo(Position that) {
        return Math.sqrt(Math.pow(this.getX() - that.getX(), 2) + Math.pow(this.getY() - that.getY(), 2));
    }

    public Position quantized() {
        int nx = x / 100 * 100;
        int ny = x / 100 * 100;

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
}
