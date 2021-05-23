import java.io.Serializable;

public class ClientUpdateInfo implements Serializable {

    private DLList<WorldObject> everything;
    private Position headPosition;
    private int size;

    public void setEverything(DLList<WorldObject> e) {
        this.everything = new DLList<WorldObject>();
        for (WorldObject wo : e) {
            if (wo.getPosition().distanceTo(headPosition) < 500) {
                everything.add(wo);
            }
        }
    }

    public void setHeadPosition(Position headPosition) {
        this.headPosition = headPosition;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DLList<WorldObject> getEverything() {
        return everything;
    }

    public Position getHeadPosition() {
        return headPosition;
    }

    public int getSize() {
        return size;
    }
}
