package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class MaskInputController {
    private static final String TEST_IMG_URI = "img.png";

    private MaskInputFrame maskInputFrame;
    private JButton selectedButton;

    public MaskInputController() {
        //TODO Read image
        try {
            maskInputFrame = new MaskInputFrame(ImageIO.read(new File(TEST_IMG_URI)));
            addKeyListener();
            maskInputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            maskInputFrame.pack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addKeyListener() {
        maskInputFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("ciao typed");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("ciao");
                if (selectedButton == null) return;
                Point selectedLocation = selectedButton.getLocation();
                System.out.println(e.getID() + " " + KeyEvent.VK_LEFT);
                switch (e.getID()) {
                    case KeyEvent.VK_LEFT:
                        selectedButton.setLocation(selectedLocation.x - 1, selectedLocation.y);
                        break;
                    case KeyEvent.VK_UP:
                        selectedButton.setLocation(selectedLocation.x - 1, selectedLocation.y - 1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        selectedButton.setLocation(selectedLocation.x + 1, selectedLocation.y);
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedButton.setLocation(selectedLocation.x, selectedLocation.y + 1);
                        break;
                }
                maskInputFrame.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void addListenerToButton(JButton b) {
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedButton != null)
                    selectedButton.setBackground(MaskInputFrame.DEFAULT_UNSELECTED_BUTTON);
                selectedButton = (JButton) (e.getSource());
                selectedButton.setBackground(MaskInputFrame.DEFAULT_SELECTED_BUTTON);
            }
        });
    }

    public void start() {
        maskInputFrame.setVisible(true);
    }

    public void addSensor() {
        JButton sensorButton = maskInputFrame.addSensor();
        addListenerToButton(sensorButton);
        maskInputFrame.repaint();
    }

}
