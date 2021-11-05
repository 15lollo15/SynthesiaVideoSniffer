package gui;

import midi.Note;
import sniffer.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DebugFrame extends JFrame {
    public static final String FRAME_TITLE = "DebugFrame";

    private JPanel mainPanel;
    private JLabel frameView;
    private KeyboardView keyboardView;
    private JButton nextFrameButton;
    private JButton prevFrameButton;

    public DebugFrame(BufferedImage frame) {
        this(frame, Note.A0, Keyboard.DEFAULT_KEYBOARD_SIZE);
    }

    public DebugFrame(BufferedImage frame, Note startNote, int numKeys) {
        super(FRAME_TITLE);
        frameView = new JLabel(new ImageIcon(frame));
        keyboardView = new KeyboardView(startNote, numKeys, frame.getWidth(), 70);

        mainPanel = new JPanel(new BorderLayout());
        this.setContentPane(mainPanel);

        mainPanel.add(frameView, BorderLayout.CENTER);
        mainPanel.add(keyboardView, BorderLayout.SOUTH);

        nextFrameButton = new JButton("Next frame");
        prevFrameButton = new JButton("Prev frame");
        mainPanel.add(prevFrameButton, BorderLayout.WEST);
        mainPanel.add(nextFrameButton, BorderLayout.EAST);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }


    public void setFrame(BufferedImage frame) {
        frameView.setIcon(new ImageIcon(frame));
        frameView.repaint();
        this.repaint();
    }

    public void setKeyboardStatus(int index, boolean status) {
        keyboardView.setStatus(index, status);
    }

    public JButton getNextFrameButton() {
        return nextFrameButton;
    }

    public JButton getPrevFrameButton() {
        return prevFrameButton;
    }
}
