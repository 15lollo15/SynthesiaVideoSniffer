import gui.DebugFrame;
import image.ImageUtils;
import mask.Mask;
import sniffer.KeySensor;
import sniffer.Keyboard;
import video.VideoFrameGrabber;

import javax.sound.midi.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainClass {

    public static final File SYNTHESIA_VIDEO = new File(
            "C:\\Users\\Utente\\Desktop\\test\\test2.mp4"
    );

    public static final File MIDI_OUTPUT = new File(
            "C:\\Users\\Utente\\Desktop\\test\\midi_f.midi"
    );

    public static final int RESOLUTION = 16;

    // TEST CONSTANTS
    public static final boolean SHOW_KEY_SENSORS = true;
    public static final boolean UNLIMITED_VIDEO_SPEED = true;
    public static final double VIDEO_SPEED = 1.0;
    public static final int SECONDS_TO_SKIP = 1;

    public static void main(String[] args) throws IOException, MidiUnavailableException, InvalidMidiDataException {

        //Grabber start
        VideoFrameGrabber videoFrameGrabber = new VideoFrameGrabber(SYNTHESIA_VIDEO);
        videoFrameGrabber.skipSeconds(SECONDS_TO_SKIP);

        //Read base frame
        BufferedImage baseFrame = videoFrameGrabber.nextFrame();

        //Mask upload
        KeySensor[] keySensors = loadSensors(baseFrame);

        //Setup Keyboard
        Keyboard keyboard = new Keyboard();

        //Setup MIDI
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION);
        Track track = sequence.createTrack();

        DebugFrame debugFrame = new DebugFrame(baseFrame);

        int frameN = 1;
        BufferedImage frame;
        while (null != (frame = videoFrameGrabber.nextFrame())) {
            for (int i = 0; i < keySensors.length; i++) {
                boolean isPressed = keySensors[i].isPressed(frame);

                if (SHOW_KEY_SENSORS) {
                    keySensors[i].drawSensor(frame);
                }

                MidiEvent me;
                if (isPressed) {
                    me = keyboard.pressKey(i, frameN);
                } else {
                    me = keyboard.releaseKey(i, frameN);
                }
                debugFrame.setKeyboardStatus(i, isPressed);
                debugFrame.setFrame(frame);
                track.add(me);
            }
            frameN++;

            if (!UNLIMITED_VIDEO_SPEED) {
                sleep(videoFrameGrabber.getFrameRate(), VIDEO_SPEED);
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

    public static KeySensor[] loadSensors(BufferedImage baseFrame) {
        Mask mask = new Mask(baseFrame.getWidth(), baseFrame.getHeight());

        Rectangle[] rects = mask.getRectangles();
        KeySensor[] keySensors = new KeySensor[rects.length];
        for (int i = 0; i < rects.length; i++) {
            Color c = ImageUtils.average(baseFrame, rects[i]);
            keySensors[i] = new KeySensor(rects[i], c);
        }
        return keySensors;
    }

}