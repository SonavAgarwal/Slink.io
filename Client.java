import java.io.*;
import javax.swing.JFrame;

public class Client {

    public static void main(String args[]) throws IOException {
        JFrame frame = new JFrame("Slink.io");

        Screen sc = new Screen();
        frame.add(sc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        sc.poll();
    }
}
