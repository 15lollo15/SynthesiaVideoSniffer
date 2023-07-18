package gui;

import midi.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DebugFrame extends JFrame {
    public static final String FRAME_TITLE = "DebugFrame";

    private JPanel mainPanel;
    private JLabel frameView;
    private KeyboardView keyboardView;

    public DebugFrame(BufferedImage frame) {
        this(frame, Note.A0, Note.C8);
    }

    public DebugFrame(BufferedImage frame, Note startNote, Note endNote) {
        super(FRAME_TITLE);
        frameView = new JLabel(new ImageIcon(frame));
        keyboardView = new KeyboardView(startNote, endNote, frame.getWidth());

        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        mainPanel.add(frameView, BorderLayout.CENTER);
        mainPanel.add(keyboardView, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setFrame(BufferedImage frame) {
        frameView.setIcon(new ImageIcon(frame));
        this.repaint();
    }

    public void setKeyboardStatus(int index, boolean status) {
        keyboardView.setKeyStatus(index, status);
    }

}