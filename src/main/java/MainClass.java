import gui.DebugFrame;
import image.ImageUtils;
import mask.Mask;
import sniffer.KeySensor;
import sniffer.Keyboard;
import utils.Timer;
import video.VideoFrameGrabber;

import javax.sound.midi.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainClass {

    public static final int FRAME_TO_SKIP = 0;
    public static final String TEST_VIDEO_URI = "C:\\Users\\Utente\\Desktop\\test\\fr.mp4";
    public static final String TEST_MIDI_URI = "C:\\Users\\Utente\\Desktop\\test\\midi_f.midi";
    public static final int RESOLUTION = 16;
    public static final boolean IS_TEST = false;

    public static final boolean SHOW_KEY_SENSORS = true;


    public static void main(String[] args) throws IOException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        //Util Timer
        Timer timer = new Timer();

        //Grabber start
        VideoFrameGrabber videoFrameGrabber = new VideoFrameGrabber(new File(TEST_VIDEO_URI));
        videoFrameGrabber.skipFrames(FRAME_TO_SKIP);
//
        //Read base frame
        BufferedImage baseFrame = videoFrameGrabber.nextFrame();
//
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
            if (IS_TEST)
                Thread.sleep(100);
        }


        System.out.println("END");
        MidiSystem.write(sequence, MidiSystem.getMidiFileTypes()[0], new File(TEST_MIDI_URI));
        videoFrameGrabber.end();
        System.exit(0);

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
