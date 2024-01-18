import java.io.Serializable;

public class ClientInput implements Serializable {

    private double mouseAngle;
    private boolean boost;

    public ClientInput(double mA, boolean b) {
        mouseAngle = mA;
        boost = b;
    }

    public boolean getBoost() {
        return boost;
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
    }

    public double getMouseAngle() {
        return mouseAngle;
    }

    public void setMouseAngle(double mouseAngle) {
        this.mouseAngle = mouseAngle;
    }

    @Override
    public String toString() {
        return "Client input: " + mouseAngle + " " + boost;
    }
}
