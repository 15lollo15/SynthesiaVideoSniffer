package sniffer;

import image.ImageUtils;

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
    private static final int DELTA_E_SENSITIVITY = 3;

    private final Rectangle sensorArea;
    private final Color baseColor;

    public KeySensor(Rectangle sensorArea, Color baseColor) {
        this.sensorArea = sensorArea;
        this.baseColor = baseColor;
    }

    public boolean isPressed(BufferedImage img) {
        Color areaColor = ImageUtils.average(img, sensorArea);
        return ImageUtils.colorDifference(baseColor, areaColor) > DELTA_E_SENSITIVITY;
    }

    public void drawSensor(BufferedImage img) {
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(sensorArea.x, sensorArea.y, sensorArea.width, sensorArea.height);
        g2d.dispose();
    }

}