package video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;

public class VideoFrameGrabber extends FFmpegFrameGrabber {

    private final Java2DFrameConverter frameConverter;

    public VideoFrameGrabber(File video) {
        super(video);
        frameConverter = new Java2DFrameConverter();
    }

    /**
     * @return the next frame, null if there are no more frames
     */
    public BufferedImage nextFrame() throws Exception {
        return frameConverter.convert(grabImage());
    }

    /**
     * @param frames frames to skip
     * @return number of skipped frames
     */
    public int skipFrames(int frames) throws Exception {
        for (int skippedFrames = 0; skippedFrames < frames; skippedFrames++) {
            if (grabImage() == null) {
                return skippedFrames;
            }
        }
        return frames;
    }

    /**
     * @param millis number of milliseconds to skip
     * @return the number of skipped frames
     */
    public int skipMillis(long millis) throws Exception {
        int frames = (int) ((getFrameRate() * millis) / 1000.0);
        return skipFrames(frames);
    }

}
