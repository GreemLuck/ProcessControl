import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        CarSimulator myCar = new CarSimulator();
        Thread myThread = new Thread(myCar);
        Display display = new Display(myCar);

        myThread.start();
        myCar.setAcceleration(10);

        while(myCar.getSpeed() < 100){
            display.update();
        }
        myCar.stop();
    }

    private static class Display {

        private int height = 400;
        private int width = 400;
        private CarSimulator car;

        JFrame frame;
        JLabel speedLabel;
        JTextField speedField;
        JButton goButton;

        Display (CarSimulator car) {
            this.car = car;

            this.frame = new JFrame("myCarPID");
            this.speedLabel = new JLabel("0", SwingConstants.CENTER);
            this.speedField = new JTextField("0", SwingConstants.TOP);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(speedField);
            frame.add(speedLabel, BorderLayout.CENTER);


            frame.setSize(width, height);
            frame.setVisible(true);
        }

        private void update() {
            double speed = car.getSpeed();
            speedLabel.setText(String.format("%.2f Km/h", speed));
        }
    }

    private static class Controller {
        private double P = 0;
        private double I = 0;
        private double D = 0;

        public void applyPID() {
            ;
        }
    }
}
