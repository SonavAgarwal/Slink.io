import java.io.Serializable;

public class Notification implements Serializable {
    private String text;
    private String purpose;

    public Notification(String t, String p) {
        text = t;
        purpose = p;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getText() {
        return text;
    }
}
