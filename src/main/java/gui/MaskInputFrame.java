package gui;

import sniffer.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MaskInputFrame extends JFrame {
    public static final Color DEFAULT_UNSELECTED_BUTTON = Color.RED;
    public static final Color DEFAULT_SELECTED_BUTTON = Color.GREEN;
    public static final int DEFAULT_BUTTON_SIZE = 7;

    private JPanel mainPanel;
    private PanelWithBackground sensorsPanel;
    private Dimension buttonDimension;

    public MaskInputFrame(BufferedImage img) {
        this(new Rectangle[0], DEFAULT_BUTTON_SIZE, img);
    }

    public MaskInputFrame(Rectangle[] defaultRects, int buttonDimension, BufferedImage img) {
        super();
        this.setResizable(false);
        this.buttonDimension = new Dimension(buttonDimension, buttonDimension);

        mainPanel = new JPanel(new BorderLayout());
        this.setContentPane(mainPanel);

        sensorsPanel = new PanelWithBackground(img);
        mainPanel.add(sensorsPanel, BorderLayout.CENTER);


        for (int i = 0; i < defaultRects.length; i++) {
            JButton tmpButton = newSensor(defaultRects[i].x, defaultRects[i].y);
            sensorsPanel.add(tmpButton);
        }
    }


    private JButton newSensor(int x, int y) {
        JButton tmpButton = new JButton("ciao");
        tmpButton.setBackground(Color.red);
        tmpButton.setSize(buttonDimension);
        tmpButton.setLocation(x, y);
        return tmpButton;
    }

    public JButton addSensor() {
        if (sensorsPanel.getComponentCount() < Keyboard.DEFAULT_KEYBOARD_SIZE) {
            JButton sensor = newSensor((int) (Math.random() * sensorsPanel.getWidth()), (int) (Math.random() * sensorsPanel.getHeight()));
            sensor.setSelected(true);
            sensorsPanel.add(sensor);
            return sensor;
        }
        return null;
    }

    public PanelWithBackground getSensorsPanel() {
        return sensorsPanel;
    }
}
