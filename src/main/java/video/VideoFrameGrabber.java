package video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoFrameGrabber {

    private FFmpegFrameGrabber grabber;
    private Java2DFrameConverter toBufferedImageConverter;

    public VideoFrameGrabber(File videoFile) {
        try {
            grabber = new FFmpegFrameGrabber(videoFile);
            toBufferedImageConverter = new Java2DFrameConverter();
            grabber.start();
        } catch (FFmpegFrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    public List<BufferedImage> getAllNextFrames() {
        List<BufferedImage> frames = new ArrayList<>();
        BufferedImage frame = nextFrame();
        while (frame != null) {
            frames.add(frame);
            frame = nextFrame();
        }
        return frames;
    }

    public int getFrameNumber() {
        return grabber.getLengthInFrames();
    }

    public int getFrameRate() {
        return (int)grabber.getFrameRate();
    }

    public void skipFrames(int framesNumber) {
        for(int i = 0; i<framesNumber; i++)
            nextFrame();
    }

    public BufferedImage nextFrame() {
        try {
            Frame frame = grabber.grabImage();
            if(frame != null)
                return toBufferedImageConverter.convert(frame);
        } catch (FFmpegFrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void end() {
        try {
            grabber.stop();
            grabber.releaseUnsafe();
            Thread.currentThread().interrupt();

            grabber = null;
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }
}
