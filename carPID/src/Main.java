import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    static final double MAX_OUTPUT = 4.5;
    static final double MIN_OUTPUT = -3.5;

    public static void main(String[] args) throws InterruptedException {

        CarSimulator myCar = new CarSimulator();
        Thread myThread = new Thread(myCar);
        Controller controller;
        Display display;

        myThread.start();

        //"a" is the amplitude of the output cycle given while using the bang bang controller.
        //"h" is simply the difference between both the min and max bang bang input.
        double a = 0.28890;
        double h = (MAX_OUTPUT-MIN_OUTPUT)*0.6;
        double kU = (4/Math.PI)*(h/a);
        double pU = 100; // Cycle of the thread.

        double kP = kU / 5;
        double kI = (2/5) * (kU / pU);
        double kD = kU * pU / 15;

        controller = new Controller(kP, kI, kD, -3.5,4.5);
        display = new Display("Car Bang Bang", 400, 400, controller);
        controller.setSetpoint(0);

        double speed;
        while (true) {
            speed = myCar.getSpeed();
            myCar.setAcceleration(controller.getOutput(speed));
            display.setSpeedLabel(speed);
            Thread.sleep(100);
        }
    }
}
