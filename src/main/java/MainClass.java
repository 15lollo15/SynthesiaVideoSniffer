import controllers.DebugController;
import gui.DebugFrame;
import gui.MaskInputController;
import gui.MaskInputFrame;
import image.ImageUtils;
import javafx.scene.input.KeyCode;
import mask.MaskReader;
import midi.Note;
import org.opencv.video.Video;
import sniffer.KeySensor;
import sniffer.Keyboard;
import utils.Timer;
import video.VideoFrameGrabber;

import javax.imageio.ImageIO;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static final int FRAME_TO_SKIP = 0;
    public static final String TEST_VIDEO_URI = "song2.mp4";
    public static final String TEST_MIDI_URI = "midi_f.midi";
    public static final String MASK_88_URI = "src/main/resources/mask88.png";
    public static final int RESOLUTION = 16;
    public static final boolean IS_TEST = false;


    public static void main(String[] args) throws IOException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {
//        MaskInputController controller = new MaskInputController();
//        controller.start();
//        controller.addSensor();
//        controller.addSensor();
//        controller.addSensor();

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
//        timer.start();
//        System.out.println("Frames number: " + videoFrameGrabber.getFrameNumber());
//        System.out.println("Frames extraction...");
//        List<BufferedImage> frames = videoFrameGrabber.getAllNextFrames();
//        System.out.println("Frames extracted!!");
//        System.out.println(timer.stop() + " millis elapsed");
//        System.out.println("Extracted Frames: " + frames.size());
        BufferedImage frame = null;
        while(null != (frame = videoFrameGrabber.nextFrame())) {

            for(int i = 0; i<keySensors.length; i++) {
                boolean isPressed = keySensors[i].isPressed(frame);
                MidiEvent me;
                if(isPressed) {
                    me = keyboard.pressKey(i, frameN);
                }else{
                    me = keyboard.releaseKey(i, frameN);
                }
                debugFrame.setKeyboardStatus(i, isPressed);
                debugFrame.setFrame(frame);
                track.add(me);
//                if (me != null)
//                    System.out.println(i + " pressed/released");
            }
            frameN++;
            if(IS_TEST)
                Thread.sleep(100);
        }


        System.out.println("END");
        MidiSystem.write(sequence, MidiSystem.getMidiFileTypes()[0], new File(TEST_MIDI_URI));
        videoFrameGrabber.end();
        System.exit(0);

    }



    public static KeySensor[] loadSensors(BufferedImage baseFrame){
        Rectangle[] rects = MaskReader.readMask(ImageUtils.readImage(new File(MASK_88_URI)));
        KeySensor[] keySensors = new KeySensor[rects.length];
        for(int i = 0; i<rects.length; i++){
            Color c = ImageUtils.average(baseFrame, rects[i]);
            keySensors[i] = new KeySensor(rects[i], c);
        }
        return keySensors;
    }

}
