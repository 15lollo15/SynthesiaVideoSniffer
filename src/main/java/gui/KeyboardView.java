package gui;

import midi.Note;

import javax.swing.*;
import java.awt.*;

public class KeyboardView extends JPanel {
    public static final Color NOT_PRESSED_COLOR = Color.green;
    public static final Color PRESSED_COLOR = Color.red;

    private Note startNote;
    private int numKeys;
    private boolean[] status;

    public KeyboardView(Note startNote, int numKeys, int width, int height) {
        super(null);
        this.startNote = startNote;
        this.numKeys = numKeys;
        this.setPreferredSize(new Dimension(width, height));
        this.status = new boolean[numKeys];
    }

    public void setStatus(int index, boolean status){
        this.status[index] = status;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int keyW = this.getWidth() / numKeys;
        int keyH = this.getHeight() / 2;


        for(int i = startNote.ordinal(); i < (startNote.ordinal() + numKeys); i++) {
            String noteName = Note.values()[i].toString();
            g.setColor(NOT_PRESSED_COLOR);
            if(status[i - startNote.ordinal()])
                g.setColor(PRESSED_COLOR);
            int y = 0;
            if(noteName.charAt(noteName.length()-1) != 'b') {
                y = keyH;
            }
            g.fillRect(i*keyW, y, keyW, keyH);
            g.setColor(Color.BLACK);
            g.drawRect(i*keyW, y, keyW, keyH);
        }

    }
}
