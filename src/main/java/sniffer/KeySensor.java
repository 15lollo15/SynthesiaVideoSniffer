package sniffer;

import colorspace.coordinates.data_types.CIELab;
import colorspace.sRGB;
import image.ImageUtils;
import midi.Note;

import java.awt.*;
import java.awt.image.BufferedImage;

public class KeySensor {

    /**
     * Delta E = 0:
     * two colors are equal
     *
     * Delta E < 1:
     * two colors are almost indistinguishable
     *
     * Delta 1 < E < 2:
     * two colors are distinguishable if you watch them closely
     *
     * Delta 2 < E < 3:
     * two colors are distinguishable
     *
     * Delta E > 3:
     * two colors are definitely different
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
