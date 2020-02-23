import javax.swing.*;
import java.awt.*;

public class Main {

    final static int FRAME_H = 500;
    final static int FRAME_W = 500;

    public static void main(String[] args) {

        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel speedLabel = new JLabel("0", SwingConstants.CENTER);
        frame.add(speedLabel, BorderLayout.CENTER);


        frame.setSize(FRAME_W, FRAME_H);
        frame.setVisible(true);
    }
}
