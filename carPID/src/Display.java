import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Display{
    private String name;
    private int height;
    private int width;
    private Controller controller;

    private JFrame frame;
    private JLabel speedLabel, speedFieldLabel;
    private JTextField speedField;
    private JButton goButton;
    private JPanel controlPanel;

    Display(String name, int height, int width, Controller controller){
        this.controller = controller;
        this.name = name;
        this.height = height;
        this.width = width;

        initFrame();
        initComponents();
        frame.add(controlPanel);
    }

    private void initFrame(){
        frame = new JFrame();
        frame.setName(name);
        frame.setSize(width,height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents(){
        speedLabel = new JLabel("0 m/s");
        speedFieldLabel = new JLabel("Desired Speed");
        speedField = new JTextField(4);
        goButton = new JButton("Go");
        setGoButton();

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        buildControlPanel();
    }

    private void buildControlPanel(){
        controlPanel.add(speedLabel);
        controlPanel.add(speedFieldLabel);
        controlPanel.add(speedField);
        controlPanel.add(goButton);
    }

    public void setSpeedLabel(double speed){
        speedLabel.setText(String.format("%.2f m/s", speed));
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
