import java.io.Serializable;

public class ClientUpdateInfo implements Serializable {

    private DLList<WorldObject> everything;
    private Position headPosition;
    private int size;
    private HashMap<String, Double> additionalInfo = new HashMap<String, Double>(100);

    public void setEverything(DLList<WorldObject> e) {
        this.everything = new DLList<WorldObject>();
        for (WorldObject wo : e) {
            if (wo.getPosition().distanceTo(headPosition) < 500) {
                everything.add(wo);
            }
        }
    }

    public DLList<WorldObject> getEverything() {
        return everything;
    }

    public void setHeadPosition(Position headPosition) {
        this.headPosition = headPosition;
    }

    public Position getHeadPosition() {
        return headPosition;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void set(String key, double value) {
        additionalInfo.put(key, value);
    }

    public double get(String key) {
        return additionalInfo.get(key);
    }
}
