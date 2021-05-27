import java.io.Serializable;

public class ServerUpdateInfo implements Serializable {
    private String message;
    private int action;
    
    public ServerUpdateInfo(int a) {
        action = a;
        message = "";
    }

    public ServerUpdateInfo(int a, String m) {
        action = a;
        message = m;
    }

    public int getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
