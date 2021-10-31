package controllers;

import gui.DebugFrame;
import image.ImageUtils;
import mask.MaskReader;
import sniffer.KeySensor;
import video.VideoFrameGrabber;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.List;

public class DebugController {

    private DebugFrame debugFrame;
    private KeySensor[] keySensors;
    private List<BufferedImage> frames;
    private int index;

    public DebugController(List<BufferedImage> frames, KeySensor[] keySensors) throws IOException {
        this.frames = frames;
        this.keySensors = keySensors;
        BufferedImage baseFrame = frames.get(0);
        debugFrame = new DebugFrame(baseFrame);
        index = 0;
        addListeners();
    }

    public void setFrameAtIndex(int index) {
        BufferedImage tmpFrame = frames.get(index);
        debugFrame.setFrame(tmpFrame);
        for(int i = 0; i<keySensors.length; i++){
            boolean isPressed = keySensors[i].isPressed(tmpFrame);
            debugFrame.setKeyboardStatus(i, isPressed);
        }
        debugFrame.repaint();
    }

    public void addListeners() {
        debugFrame.getNextFrameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(index >= frames.size()) return;
                setFrameAtIndex(++index);
                System.out.println(index);
            }
        });

        debugFrame.getPrevFrameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(index <= 0) return;
                setFrameAtIndex(--index);
                System.out.println(index);
            }
        });
    }

}
