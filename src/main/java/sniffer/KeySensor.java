package sniffer;

import colorspace.coordinates.data_types.CIELab;
import colorspace.sRGB;
import image.ImageUtils;
import midi.Note;

import java.awt.*;
import java.awt.image.BufferedImage;

public class KeySensor {

    /**
     * DeltaE = 0 : equal colors
     * DeltaE < 3 : colors difference difficult to notice 
     *
     * TODO: DeltaE = 5 is high enough to say that there's a
     *       difference between two colors but testing is needed
     *       to calibrate this value for Synthesia videos
     */
    private static final int DELTA_E_SENSITIVITY = 5;

    private final Note note;
    private final Rectangle sensorArea;
    private final CIELab baseColorLab;

    public KeySensor(Note note, Rectangle sensorArea, Color baseColor) {
        this.note = note;
        this.sensorArea = sensorArea;
        this.baseColorLab = sRGB.toCIELab(baseColor);
    }

    public Note getNote() {
        return note;
    }

    public boolean isPressed(BufferedImage img) {
        Color areaColor = ImageUtils.average(img, sensorArea);
        CIELab areaColorLab = sRGB.toCIELab(areaColor);
        return ImageUtils.colorDifference(baseColorLab, areaColorLab) > DELTA_E_SENSITIVITY;
    }

    public void drawSensor(BufferedImage img) {
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(sensorArea.x, sensorArea.y, sensorArea.width, sensorArea.height);
        g2d.dispose();
    }

}
