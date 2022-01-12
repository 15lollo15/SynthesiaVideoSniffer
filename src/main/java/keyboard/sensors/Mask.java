package keyboard.sensors;

import image.ImageUtils;
import keyboard.Note;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Mask {

    private final int frameWidth;
    private final int frameHeight;

    private final int sensorWidth;
    private final int sensorHeight;

    private final int whiteYOffset;
    private final int blackYOffset;

    private final Note startNote;
    private final Note endNote;

    public static class Builder {

        private static final int DEFAULT_SENSOR_DIM = 3;

        private final int frameWidth;
        private final int frameHeight;

        private int sensorWidth = DEFAULT_SENSOR_DIM;
        private int sensorHeight = DEFAULT_SENSOR_DIM;

        private int whiteYOffset;
        private int blackYOffset;

        private Note startNote = Note.A0;
        private Note endNote = Note.C8;

        public Builder(int frameWidth, int frameHeight) {
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;

            whiteYOffset = (int) (frameHeight - (frameHeight / 36.0));
            blackYOffset = (int) (whiteYOffset - (frameHeight / 13.0));
        }

        public Builder setSensorWidth(int sensorWidth) {
            this.sensorWidth = sensorWidth;
            return this;
        }

        public Builder setSensorHeight(int sensorHeight) {
            this.sensorHeight = sensorHeight;
            return this;
        }

        public Builder setWhiteYOffset(int whiteYOffset) {
            this.whiteYOffset = whiteYOffset;
            return this;
        }

        public Builder setBlackYOffset(int blackYOffset) {
            this.blackYOffset = blackYOffset;
            return this;
        }

        public Builder setStartNote(Note startNote) {
            this.startNote = startNote;
            return this;
        }

        public Builder setEndNote(Note endNote) {
            this.endNote = endNote;
            return this;
        }

        public Mask build() {
            return new Mask(this);
        }

    }

    private Mask(Builder builder) {
        frameWidth = builder.frameWidth;
        frameHeight = builder.frameHeight;
        sensorWidth = builder.sensorWidth;
        sensorHeight = builder.sensorHeight;
        whiteYOffset = builder.whiteYOffset;
        blackYOffset = builder.blackYOffset;
        startNote = builder.startNote;
        endNote = builder.endNote;
    }

    public List<KeySensor> createKeySensors(BufferedImage baseFrame) {
        Rectangle[] rects = getRectangles();
        int numSensors = endNote.ordinal() - startNote.ordinal() + 1;
        List<KeySensor> keySensors = new ArrayList<>(numSensors);

        for (int i = 0; i < numSensors; ++i) {
            Note note = Note.values()[startNote.ordinal() + i];
            Rectangle rect = rects[i];
            Color color = ImageUtils.average(baseFrame, rect);

            keySensors.add(new KeySensor(note, rect, color));
        }
        return keySensors;
    }

    private Rectangle[] getRectangles() {
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        double whiteKeyOffset = ((double) frameWidth) / Note.countWhiteKeys(startNote, endNote);

        double whiteKeys = 0;

        for (int i = startNote.ordinal(); i <= endNote.ordinal(); ++i) {
            if (Note.isWhiteKey(i)) {
                double xOffset = whiteKeyOffset * whiteKeys + (whiteKeyOffset / 2.0) - (sensorWidth / 2.0);

                Rectangle white = new Rectangle((int) xOffset, whiteYOffset, sensorWidth, sensorHeight);
                rectangles.add(white);

                whiteKeys++;
            } else {
                double xOffset = whiteKeyOffset * whiteKeys - (sensorWidth / 2.0);

                // if D#b or G#b
                if (i % 12 == 4 || i % 12 == 9) {
                    xOffset -= whiteKeyOffset / 10.0;
                }

                // if B#b or E#b
                if (i % 12 == 1 || i % 12 == 6) {
                    xOffset += whiteKeyOffset / 10.0;
                }

                Rectangle black = new Rectangle((int) xOffset, blackYOffset, sensorWidth, sensorHeight);
                rectangles.add(black);
            }
        }

        rectangles.sort((o1, o2) -> (int) (o1.getX() - o2.getX()));

        return rectangles.toArray(new Rectangle[0]);
    }

}
