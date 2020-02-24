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

        Display (CarSimulator car) {
            this.car = car;

            this.frame = new JFrame("myCarPID");
            this.speedLabel = new JLabel("0", SwingConstants.CENTER);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(speedLabel, BorderLayout.CENTER);

            frame.setSize(width, height);
            frame.setVisible(true);
        }

        private void update() {
            double speed = car.getSpeed();
            speedLabel.setText(String.format("%.2f Km/h", speed));
        }
    }
}
