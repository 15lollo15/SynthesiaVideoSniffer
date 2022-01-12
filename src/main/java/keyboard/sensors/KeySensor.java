package keyboard.sensors;

import colorspace.coordinates.data_types.CIELab;
import colorspace.sRGB;
import image.ImageUtils;
import keyboard.Note;

import java.awt.*;
import java.awt.image.BufferedImage;

public class KeySensor {

    /**
     * DeltaE = 0 : equal colors
     * DeltaE < 3 : colors difference difficult to notice 
     *
     * TODO: DeltaE = 10 is high enough to say that there's a
     *       difference between two colors big enough to differentiate
     *       pressed notes from simple color variations but testing is
     *       needed to calibrate this value for Synthesia videos
     */
    private static final int DELTA_E_SENSITIVITY = 10;

    private final Note note;
    private final Rectangle sensorArea;
    private final CIELab baseColorLab;
    private int sensitivity = DELTA_E_SENSITIVITY;

    public KeySensor(Note note, Rectangle sensorArea, Color baseColor) {
        this.note = note;
        this.sensorArea = sensorArea;
        this.baseColorLab = sRGB.toCIELab(baseColor);
    }

    public boolean isPressed(BufferedImage img) {
        Color areaColor = ImageUtils.average(img, sensorArea);
        CIELab areaColorLab = sRGB.toCIELab(areaColor);
        double deltaE = ImageUtils.colorDifference(baseColorLab, areaColorLab);
        return deltaE > sensitivity;
    }

    public Note getNote() {
        return note;
    }

    public Rectangle getSensorArea() {
        return sensorArea;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

}
