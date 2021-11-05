package gui;

import midi.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DebugFrame extends JFrame {

    public static final String FRAME_TITLE = "DebugFrame";

    private final JLabel frameView;
    private final KeyboardView keyboardView;
    private final JButton nextFrameButton;
    private final JButton prevFrameButton;

    public DebugFrame(BufferedImage frame) {
        this(frame, Note.A0, Note.C8);
    }

    public DebugFrame(BufferedImage frame, Note startNote, Note endNote) {
        super(FRAME_TITLE);
        frameView = new JLabel(new ImageIcon(frame));
        keyboardView = new KeyboardView(startNote, endNote, frame.getWidth());

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        mainPanel.add(frameView, BorderLayout.CENTER);
        mainPanel.add(keyboardView, BorderLayout.SOUTH);

        nextFrameButton = new JButton("Next frame");
        prevFrameButton = new JButton("Prev frame");
        mainPanel.add(prevFrameButton, BorderLayout.WEST);
        mainPanel.add(nextFrameButton, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setFrame(BufferedImage frame) {
        frameView.setIcon(new ImageIcon(frame));
        frameView.repaint();
        this.repaint();
    }

    public void setKeyboardStatus(int index, boolean status) {
        keyboardView.setKeyStatus(index, status);
    }

    public JButton getNextFrameButton() {
        return nextFrameButton;
    }

    public JButton getPrevFrameButton() {
        return prevFrameButton;
    }

}
