package io.github.GabbyMoon.centroidfinder.utils;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility for generating tiny MP4s for testing.
 */
public class VideoTestUtilities {

    /**
     * Creates a temporary MP4 file with N solid-color frames.
     *
     * @param durationSeconds total duration of the video
     * @param fps frames per second
     * @param width video width
     * @param height video height
     * @param colors array of colors for each frame
     * @return Path to the generated MP4 file
     */
    public static Path createTestVideo(
            int durationSeconds,
            double fps,
            int width,
            int height,
            Color[] colors
    ) throws Exception {

        Path tempFile = Files.createTempFile("test-video", ".mp4");
        tempFile.toFile().deleteOnExit();

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toFile(), width, height)) {
            recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(fps);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            int totalFrames = (int) Math.round(durationSeconds * fps);
            for (int i = 0; i < totalFrames; i++) {
                Color c = colors[i % colors.length];
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = img.createGraphics();
                g.setColor(c);
                g.fillRect(0, 0, width, height);
                g.dispose();

                recorder.record(converter.convert(img));
            }

            recorder.stop();
            converter.close();
        }

        return tempFile;
    }
    
    /**
     * Creates a solid-color BufferedImage of the given size.
     */
    public static BufferedImage makeSolidImage(Color color, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = img.createGraphics();  
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return img;
    }

    // private static Color yuvToRgb(int y, int u, int v) {
    // int r = (int)Math.round(y + 1.402 * (v - 128));
    // int g = (int)Math.round(y - 0.344136 * (u - 128) - 0.714136 * (v - 128));
    // int b = (int)Math.round(y + 1.772 * (u - 128));

    // // clamp to 0–255
    // r = Math.min(255, Math.max(0, r));
    // g = Math.min(255, Math.max(0, g));
    // b = Math.min(255, Math.max(0, b));

    // return new Color(r, g, b);
}

