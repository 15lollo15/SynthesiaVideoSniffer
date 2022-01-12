package keyboard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Keyboard {

    private static final boolean PRESSED = true;
    private static final boolean RELEASED = false;

    public static final int KEYBOARD_SIZE = 88;
    private final boolean[] isPressed = new boolean[KEYBOARD_SIZE];

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void pressKey(Note note, int tick) {
        if (!isPressed[note.ordinal()]) {
            isPressed[note.ordinal()] = true;

            pcs.firePropertyChange(
                    "keystroke",
                    null,
                    new Keystroke(note, PRESSED, tick)
            );
        }
    }

    public void releaseKey(Note note, int tick) {
        if (isPressed[note.ordinal()]) {
            isPressed[note.ordinal()] = false;

            pcs.firePropertyChange(
                    "keystroke",
                    null,
                    new Keystroke(note, RELEASED, tick)
            );
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

}
