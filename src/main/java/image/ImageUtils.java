package image;

import colorspace.coordinates.data_types.CIELab;
import colorspace.sRGB;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    private ImageUtils() {}

    public static Color average(BufferedImage img, Rectangle rect) {
        int startX = rect.x;
        int startY = rect.y;
        int endX = startX + rect.width;
        int endY = startY + rect.width;
        int totPixels = rect.height * rect.width;

        double sumR = 0;
        double sumG = 0;
        double sumB = 0;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Color pixelColor = new Color(img.getRGB(x, y));
                sumR += linearize(pixelColor.getRed());
                sumG += linearize(pixelColor.getGreen());
                sumB += linearize(pixelColor.getBlue());
            }
        }

        int r = (int) delinearize(sumR / totPixels);
        int g = (int) delinearize(sumG / totPixels);
        int b = (int) delinearize(sumB / totPixels);

        return new Color(r, g, b);
    }

    public static double linearize(int channelValue) {
        return sRGB.linearize(sRGB.normalize(channelValue));
    }

    public static double delinearize(double channelValue) {
        return sRGB.denormalize(sRGB.delinearize(channelValue));
    }

    public static double colorDifference(Color c1, Color c2) {
        CIELab lab1 = sRGB.toCIELab(c1);
        CIELab lab2 = sRGB.toCIELab(c2);
        return sRGB.deltaE94(lab1, lab2);
    }

    public static double colorDifference(CIELab c1, CIELab c2) {
        return sRGB.deltaE94(c1, c2);
    }

}
