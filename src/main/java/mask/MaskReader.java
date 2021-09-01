package mask;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;

public class MaskReader {
    public static final Color Y_LINE_COLOR = Color.BLUE;
    public static final Color X_RECT_COLOR = Color.RED;
    public static final int RECT_DIMENSIONS = 5;

    private MaskReader() {}

    private static int findYLine(BufferedImage img, int startingPoint) {
        int line= -1;
        for(int y = startingPoint; y < img.getHeight() && line == -1; y++)
            if(img.getRGB(0, y) == Y_LINE_COLOR.getRGB())
                line = y;
        return line;
    }

    public static Rectangle[] readMask(BufferedImage img) {
        int blacksLine = findYLine(img, 0);
        int whitesLine = findYLine(img, blacksLine + 1);

        ArrayList<Rectangle> rects = new ArrayList<>();
        ArrayList<Rectangle> blacksRects = readRects(img, blacksLine);
        ArrayList<Rectangle> whiteRects = readRects(img, whitesLine);
        rects.addAll(blacksRects);
        rects.addAll(whiteRects);

        rects.sort(new Comparator<Rectangle>() {
            @Override
            public int compare(Rectangle o1, Rectangle o2) {
                return (int)(o1.getX() - o2.getX());
            }
        });

        return rects.toArray(new Rectangle[0]);
    }

    private static ArrayList<Rectangle> readRects(BufferedImage img, int line) {
        ArrayList<Rectangle> rects = new ArrayList<>();

        for(int x = 0; x < img.getWidth(); x++){
            if(img.getRGB(x,line) == X_RECT_COLOR.getRGB()){
                Rectangle tmpRect = new Rectangle(x, line, RECT_DIMENSIONS, RECT_DIMENSIONS);
                rects.add(tmpRect);
                x += RECT_DIMENSIONS;
            }
        }

        return rects;
    }

}
