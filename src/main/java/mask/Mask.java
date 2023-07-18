package mask;

import midi.Note;

import java.awt.*;
import java.util.ArrayList;

public class Mask {

    private final int frameWidth;
    private final int frameHeight;

    private int whiteYOffset;
    private int blackYOffset;

    private static final int DEFAULT_SENSOR_DIM = 3;
    private int sensorWidth;
    private int sensorHeight;

    private Note startNote;
    private Note endNote;

    public Mask(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        whiteYOffset = (int) (frameHeight - (frameHeight / 36.0));
        blackYOffset = (int) (whiteYOffset - (frameHeight / 8.0));

        sensorWidth = DEFAULT_SENSOR_DIM;
        sensorHeight = DEFAULT_SENSOR_DIM;

        // piano with 88 keys
        startNote = Note.A0;
        endNote = Note.C8;
    }

    public int getWhiteSensorYOffset() {
        return whiteYOffset;
    }

    /**
     * Set Y offset for white keys sensors
     * @param newYOffset Y offset for white keys
     * @return true is the new offset is valid, false otherwise
     */
    public boolean setWhiteSensorYOffset(int newYOffset) {
        if (0 <= newYOffset && newYOffset <= frameHeight) {
            this.whiteYOffset = newYOffset;
            return true;
        } else {
            return false;
        }
    }

    public int getBlackSensorYOffset() {
        return blackYOffset;
    }

    /**
     * Set Y offset for black keys sensors
     * @param newYOffset Y offset for black keys
     * @return true is the new offset is valid, false otherwise
     */
    public boolean setBlackSensorYOffset(int newYOffset) {
        if (0 <= newYOffset && newYOffset <= frameHeight) {
            this.blackYOffset = newYOffset;
            return true;
        } else {
            return false;
        }
    }

    public int getSensorWidth() {
        return sensorWidth;
    }

    public void setSensorWidth(int sensorWidth) {
        this.sensorWidth = sensorWidth;
    }

    public int getSensorHeight() {
        return sensorHeight;
    }

    public void setSensorHeight(int sensorHeight) {
        this.sensorHeight = sensorHeight;
    }

    public Note getStartNote() {
        return startNote;
    }

    public void setStartNote(Note startNote) {
        this.startNote = startNote;
    }

    public Note getEndNote() {
        return endNote;
    }

    public void setEndNote(Note endNote) {
        this.endNote = endNote;
    }

    public Rectangle[] getRectangles() {
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        double whiteKeyOffset = ((double) frameWidth) / Note.countWhiteKeys(startNote, endNote);

        double whiteKeys = 0;

        for (int i = startNote.ordinal(); i <= endNote.ordinal(); ++i) {
            if (Note.isWhiteKey(i)) {
                double xOffset = whiteKeyOffset * whiteKeys + ((whiteKeyOffset - DEFAULT_SENSOR_DIM) / 2.0);

                Rectangle white = new Rectangle((int) xOffset, whiteYOffset, sensorWidth, sensorHeight);
                rectangles.add(white);

                whiteKeys++;
            } else {
                double xOffset = whiteKeyOffset * whiteKeys;

                Rectangle black = new Rectangle((int) xOffset, blackYOffset, sensorWidth, sensorHeight);
                rectangles.add(black);
            }
        }

        rectangles.sort((o1, o2) -> (int) (o1.getX() - o2.getX()));

        return rectangles.toArray(new Rectangle[0]);
    }

}