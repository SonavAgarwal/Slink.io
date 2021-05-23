import java.io.Serializable;

public class ClientUpdateInfo implements Serializable {

    private DLList<WorldObject> everything;
    private Position headPosition;
    private int size;

    public void setEverything(DLList<WorldObject> everything) {
        this.everything = everything;
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
