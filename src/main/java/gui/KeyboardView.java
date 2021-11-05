package gui;

import midi.Note;

import javax.swing.*;
import java.awt.*;

public class KeyboardView extends JPanel {

    private static final Color COLOR_WHITE_PRESSED = Color.GREEN;
    private static final Color COLOR_BLACK_PRESSED = Color.RED;

    private final Note startNote;
    private final Note endNote;
    private final boolean[] keysStatus;

    private final int panelWidth;
    private final int panelHeight;

    private final int BLACK_KEY_WIDTH;
    private final int BLACK_KEY_HEIGHT;
    private final int WHITE_KEY_WIDTH;
    private final int WHITE_KEY_HEIGHT;

    public KeyboardView(Note startNote, Note endNote, int frameWidth) {
        super(null);

        this.startNote = startNote;
        this.endNote = endNote;

        this.keysStatus = new boolean[endNote.ordinal() - startNote.ordinal() + 1];

        int scaleFactor = (int) (frameWidth / (Note.countWhiteKeys(startNote, endNote) * 3.0));
        BLACK_KEY_WIDTH = 2 * scaleFactor;
        BLACK_KEY_HEIGHT = BLACK_KEY_WIDTH * 6;
        WHITE_KEY_WIDTH = 3 * scaleFactor;
        WHITE_KEY_HEIGHT = WHITE_KEY_WIDTH * 6;

        this.panelWidth = frameWidth;
        this.panelHeight = WHITE_KEY_HEIGHT + 10;

        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    public void setKeyStatus(int index, boolean status) {
        this.keysStatus[index] = status;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // draw background
        g.setColor(new Color(150, 28, 20));
        g.fillRect(0, 0, panelWidth, panelHeight);

        // offset to center the piano
        int X_OFF = (panelWidth - Note.countWhiteKeys(startNote, endNote) * WHITE_KEY_WIDTH) / 2;

        // draw white keys
        int xOffset = 0;
        for (int i = startNote.ordinal(); i <= endNote.ordinal(); i++) {
            if (Note.isWhiteKey(i)) {
                // draw key
                g.setColor(Color.WHITE);
                g.fillRect(X_OFF + xOffset, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawRect(X_OFF + xOffset, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);

                // highlight key if pressed
                if (isKeyPressed(i - startNote.ordinal())) {
                    g.setColor(COLOR_WHITE_PRESSED);
                    g.fillRect(X_OFF + xOffset + 1, 1, WHITE_KEY_WIDTH - 2, WHITE_KEY_HEIGHT - 2);
                }

                xOffset += WHITE_KEY_WIDTH;
            }
        }

        // draw black keys
        xOffset = 0;
        for (int i = startNote.ordinal(); i <= endNote.ordinal(); i++) {
            if (Note.isBlackKey(i)) {
                // draw key
                g.setColor(Color.BLACK);
                g.fillRect(X_OFF + xOffset - (BLACK_KEY_WIDTH / 2), 0, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);

                // highlight key if pressed
                if (isKeyPressed(i - startNote.ordinal())) {
                    g.setColor(COLOR_BLACK_PRESSED);
                    g.fillRect(X_OFF + xOffset - (BLACK_KEY_WIDTH / 2) + 1, 1, BLACK_KEY_WIDTH - 2, BLACK_KEY_HEIGHT - 2);
                }
            } else {
                xOffset += WHITE_KEY_WIDTH;
            }
        }

    }

    private boolean isKeyPressed(int keyIndex) {
        return keysStatus[keyIndex];
    }

}
