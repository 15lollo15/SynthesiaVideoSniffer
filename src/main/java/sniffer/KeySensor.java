package sniffer;

import image.ImageUtils;
import midi.Note;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class KeySensor {
    private static final int DEFAULT_SENSIBILITY = 50;

    private Rectangle sensorArea;
    private Color baseColor;
    private int sensibility = DEFAULT_SENSIBILITY;

    public KeySensor(Rectangle sensorArea, Color baseColor) {
        this.sensorArea = sensorArea;
        this.baseColor = baseColor;
    }

    public boolean isPressed(BufferedImage img){
        Color areaColor = ImageUtils.average(img, sensorArea);
        return ImageUtils.colorDifference(baseColor, areaColor) > sensibility;
    }

    public void drawSensor(BufferedImage img) {
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(sensorArea.x, sensorArea.y, sensorArea.width, sensorArea.height);
        g2d.dispose();
    }

}
