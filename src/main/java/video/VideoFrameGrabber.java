package video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;

public class VideoFrameGrabber implements AutoCloseable {

    private final FFmpegFrameGrabber grabber;
    private final Java2DFrameConverter toBufferedImageConverter;

    public VideoFrameGrabber(File videoFile) throws FFmpegFrameGrabber.Exception {
        grabber = new FFmpegFrameGrabber(videoFile);
        toBufferedImageConverter = new Java2DFrameConverter();
        grabber.start();
    }

    public int getFrameNumber() {
        return grabber.getLengthInFrames();
    }

    public double getFrameRate() {
        return grabber.getFrameRate();
    }

    public void skipFrames(int framesNumber) {
        for (int i = 0; i < framesNumber; i++)
            nextFrame();
    }

    public void skipSeconds(int seconds) {
        int framesToSkip = (int) (grabber.getFrameRate() * seconds);
        skipFrames(framesToSkip);
    }

    public BufferedImage nextFrame() {
        try {
            Frame frame = grabber.grabImage();
            if (frame != null)
                return toBufferedImageConverter.convert(frame);
        } catch (FFmpegFrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws FFmpegFrameGrabber.Exception {
        grabber.stop();
    }

}