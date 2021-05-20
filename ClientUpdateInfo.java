import java.io.Serializable;

public class ClientUpdateInfo implements Serializable {
    private DLList<WorldObject> everything;
    private Position headPosition;

    public ClientUpdateInfo(DLList<WorldObject> e, Position p) {
        everything = e;
        headPosition = p;
    }

    public DLList<WorldObject> getEverything() {
        return everything;
    }

    public Position getHeadPosition() {
        return headPosition;
    }
}
