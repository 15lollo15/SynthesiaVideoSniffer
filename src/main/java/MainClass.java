import gui.DebugFrame;
import image.ImageUtils;
import mask.Mask;
import midi.Note;
import sniffer.KeySensor;
import sniffer.Keyboard;
import video.VideoFrameGrabber;

import javax.sound.midi.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    private static final File SYNTHESIA_VIDEO = new File(
            "C:\\Users\\Utente\\Desktop\\test\\gny.mp4"
    );

    private static final File MIDI_OUTPUT = new File(
            "C:\\Users\\Utente\\Desktop\\test\\midi_f.midi"
    );

    private static final long MILLIS_TO_SKIP = 0;

    // DEBUG CONSTANTS

    private static final boolean UNLIMITED_SPEED = true;
    private static final double SPEED = 1.0;

    private static final boolean SHOW_KEY_SENSORS = true;


    public static void main(String[] args) throws IOException, MidiUnavailableException, InvalidMidiDataException {

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

        // Setup MIDI
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, 16);
        Track track = sequence.createTrack();

        DebugFrame debugFrame = new DebugFrame(baseFrame);

        int numFrame = 1;
        BufferedImage frame;
        while (null != (frame = videoFrameGrabber.nextFrame())) {
            for (KeySensor keySensor : keySensors) {
                int keyIndex = keySensor.getNote().ordinal();
                boolean isPressed = keySensor.isPressed(frame);

                MidiEvent me;
                if (isPressed) {
                    me = keyboard.pressKey(keyIndex, numFrame);
                } else {
                    me = keyboard.releaseKey(keyIndex, numFrame);
                }

                track.add(me);

                if (SHOW_KEY_SENSORS) {
                    keySensor.drawSensor(frame);
                }

                debugFrame.setKeyboardStatus(keyIndex, isPressed);
            }
            debugFrame.setFrame(frame);
            numFrame++;

            if (!UNLIMITED_SPEED) {
                sleep(videoFrameGrabber.getFrameRate(), SPEED);
            }
        }
        MidiSystem.write(sequence, MidiSystem.getMidiFileTypes()[0], MIDI_OUTPUT);
        videoFrameGrabber.close();

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
        Mask mask = new Mask(baseFrame.getWidth(), baseFrame.getHeight());
        Rectangle[] rects = mask.getRectangles();

        List<KeySensor> keySensors = new ArrayList<>(rects.length);
        for (int i = 0; i < rects.length; i++) {
            Color color = ImageUtils.average(baseFrame, rects[i]);
            Note note = Note.values()[i];
            keySensors.add(new KeySensor(note, rects[i], color));
        }
        return keySensors;
    }

}
