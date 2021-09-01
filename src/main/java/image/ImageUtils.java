package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    private ImageUtils(){}

    public static Color average(BufferedImage img, Rectangle rect) {
        int startX = rect.x;
        int startY = rect.y;
        int endX = startX + rect.width;
        int endY = startY + rect.width;
        int totPixels = rect.height * rect.width;

        int sumR = 0;
        int sumG = 0;
        int sumB = 0;

        for(int x = startX; x < endX; x++){
            for(int y = startY; y <endY; y++){
                Color pixelColor = new Color(img.getRGB(x, y));
                sumR += pixelColor.getRed();
                sumG += pixelColor.getGreen();
                sumB += pixelColor.getBlue();
            }
        }

        int r = sumR / totPixels;
        int g = sumG / totPixels;
        int b = sumB / totPixels;

        return new Color(r, g, b);
    }

    public static double colorDifference(Color c1, Color c2) {
//        int diff = 0;
//
//        diff += Math.abs(c1.getRed() - c2.getRed());
//        diff += Math.abs(c1.getGreen() - c2.getGreen());
//        diff += Math.abs(c1.getBlue() - c2.getBlue());
//
//        return diff;

        double rq = Math.pow(c1.getRed() - c2.getRed(), 2);
        double gq = Math.pow(c1.getGreen() - c2.getGreen(), 2);
        double bq = Math.pow(c1.getBlue() - c2.getBlue(), 2);

        return Math.sqrt(rq + gq + bq);
    }

    public static BufferedImage readImage(File imgFile) {
        try {
            return ImageIO.read(imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
