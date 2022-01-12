import gui.DebugFrame;
import keyboard.sensors.Mask;
import midi.MidiMaker;
import keyboard.Note;
import keyboard.sensors.KeySensor;
import keyboard.Keyboard;
import video.VideoFrameGrabber;

import javax.sound.midi.InvalidMidiDataException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainClass {

    private static final File SYNTHESIA_VIDEO = new File(
            "C:\\Users\\Utente\\Desktop\\test\\knf.mp4"
    );

    private static final File MIDI_OUTPUT = new File(
            "C:\\Users\\Utente\\Desktop\\test\\midi_f.midi"
    );

    private static final long MILLIS_TO_SKIP = 0;

    // DEBUG CONSTANTS
    private static final boolean UNLIMITED_SPEED = true;
    private static final double SPEED = 1.0;
    private static final boolean SHOW_KEY_SENSORS = true;

    public static void main(String[] args) throws IOException, InvalidMidiDataException {

        // Grabber start
        VideoFrameGrabber videoFrameGrabber = new VideoFrameGrabber(SYNTHESIA_VIDEO);
        videoFrameGrabber.start();
        videoFrameGrabber.skipMillis(MILLIS_TO_SKIP);

        // Read base frame
        BufferedImage baseFrame = videoFrameGrabber.nextFrame();

        // Mask upload
        List<KeySensor> keySensors = loadSensors(baseFrame);

        // Setup Keyboard
        Keyboard keyboard = new Keyboard();

        MidiMaker midiMaker = new MidiMaker(keyboard);

        DebugFrame debugFrame = new DebugFrame(baseFrame);

        int startingFrame = -1;
        int numFrame = 1;
        BufferedImage frame;
        while (null != (frame = videoFrameGrabber.nextFrame())) {
            for (KeySensor keySensor : keySensors) {
                Note note = keySensor.getNote();
                boolean isPressed = keySensor.isPressed(frame);

                if (isPressed) {
                    if (startingFrame == -1)
                        startingFrame = numFrame;
                    keyboard.pressKey(note, numFrame - startingFrame);
                } else {
                    keyboard.releaseKey(note, numFrame - startingFrame);
                }

                debugFrame.setKeyboardStatus(note.ordinal(), isPressed);
            }

            if (SHOW_KEY_SENSORS) {
                drawSensors(frame, keySensors);
            }

            debugFrame.setFrame(frame);
            numFrame++;

            if (!UNLIMITED_SPEED) {
                sleep(videoFrameGrabber.getFrameRate(), SPEED);
            }
        }
        videoFrameGrabber.close();

        midiMaker.saveToMidi(MIDI_OUTPUT);

        System.exit(0);
    }

    public static void sleep(double frameRate, double speed) {
        long ms = (long) (1000.0 / (frameRate * speed));
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<KeySensor> loadSensors(BufferedImage baseFrame) {
        Mask mask = new Mask.Builder(baseFrame.getWidth(), baseFrame.getHeight())
                .build();

        return mask.createKeySensors(baseFrame);
    }

    public static void drawSensors(BufferedImage img, List<KeySensor> keySensors) {
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.RED);
        for (KeySensor keySensor : keySensors) {
            Rectangle sensorArea = keySensor.getSensorArea();
            g2d.fillRect(sensorArea.x, sensorArea.y, sensorArea.width, sensorArea.height);
        }
        g2d.dispose();
    }

}
