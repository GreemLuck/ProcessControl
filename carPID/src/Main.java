import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CarSimulator myCar = new CarSimulator();
        Thread myThread = new Thread(myCar);

        double kU = 34;
        long pU = 320;

        double kP = kU/5;
        double kI = (2/5)*kU/pU;
        double kD = kU*pU/15;

        //Controller controller = new Controller(kU,0,0);
        Controller controller = new Controller(kP,kI,kD);
        Display display = new Display(myCar, controller);
        double acceleration = 0;

        myThread.start();

        //Determining ultimate gain and period
        long timer = System.currentTimeMillis();
        double maxSpeed = myCar.getSpeed();
        double minSpeed = myCar.getSpeed();
        boolean above = false;

        while(true) {
            //Period sampling
            double carSpeed = myCar.getSpeed();
            if((above && carSpeed >= controller.getSetpoint()) ||
                    (!above && carSpeed <= controller.getSetpoint())){
                System.out.printf("%d %.5f %.5f\n", System.currentTimeMillis() - timer, maxSpeed, minSpeed);
                timer = System.currentTimeMillis();
                above = !above;
            }
            if(carSpeed > maxSpeed){
                maxSpeed = carSpeed;
            }
            if(carSpeed < minSpeed) minSpeed = carSpeed;

            acceleration += controller.getOutput(myCar.getSpeed());
            myCar.setAcceleration(acceleration);
            display.update();
            myThread.sleep(50);
        }


    }

    private static class Display extends JFrame {

        private int height = 400;
        private int width = 400;
        private CarSimulator car;
        private Controller controller;

        JLabel speedLabel = new JLabel("0 Km/h");
        JTextField speedField = new JTextField("0", 3);
        JButton goButton = new JButton("Go");

        Display (CarSimulator car, Controller controller) {
            super("myCarPID");
            this.car = car;
            this.controller = controller;

            setGoButton();

            //Display setup
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(400, 400);
            setResizable(true);

            //Control panel setup
            JPanel controlPanel = new JPanel();
            controlPanel.setBounds(0,0, this.width, this.height/4);
            controlPanel.add(speedField);
            controlPanel.add(goButton);

            //Graph setup
            JPanel graphPanel = new JPanel();
            graphPanel.setBounds(0, controlPanel.getHeight(),
                    this.width, this.height-controlPanel.getHeight());
            graphPanel.setBackground(Color.white);
            graphPanel.setLayout(new GridBagLayout());
            graphPanel.add(speedLabel);


            add(BorderLayout.SOUTH,controlPanel);
            add(BorderLayout.CENTER,graphPanel);

            setVisible(true);

        }

        private void update() {
            double speed = car.getSpeed();
            speedLabel.setText(String.format("%.2f Km/h", speed));
        }

        private void setGoButton(){
            goButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.reset();
                    double sp = Double.valueOf(speedField.getText());
                    controller.setSetpoint(sp);
                }
            });

        }
    }

    private static class Controller {

        private double P;
        private double I;
        private double D;

        private double errorMax = 100;
        private double errorMin = -100;

        long priorTime;
        double accumulatedError;
        double priorError;

        private double setpoint = 0;

        public Controller(double p, double i, double d){
            P=p; I=i; D=d;

            priorTime = System.currentTimeMillis();
            accumulatedError = 0;
            priorError = 0;
        }

        public double getOutput(double actual){
            return getOutput(actual, setpoint);
        }

        public double getOutput(double actual, double setpoint) {
            double output;
            this.setpoint = setpoint;

            double error = setpoint - actual;

            long dt = System.currentTimeMillis() - priorTime;
            priorTime = System.currentTimeMillis();

            accumulatedError += error*dt;
            if(accumulatedError > errorMax) accumulatedError = errorMax;
            if(accumulatedError < errorMin) accumulatedError = errorMin;

            double derror = (error - priorError)/dt;
            priorError = error;

            output = P*error + I* accumulatedError + D*derror;

            return output;
        }

        public void reset() {
            setpoint = 0;
            priorError = 0;
            priorTime = System.currentTimeMillis();
            accumulatedError = 0;
        }

        public double getP() {
            return P;
        }

        public void setP(double p) {
            P = p;
        }

        public double getI() {
            return I;
        }

        public void setI(double i) {
            I = i;
        }

        public double getD() {
            return D;
        }

        public void setD(double d) {
            D = d;
        }

        public double getSetpoint() {
            return setpoint;
        }

        public void setSetpoint(double setpoint) {
            this.setpoint = setpoint;
        }
    }
}
