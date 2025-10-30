package io.github.GabbyMoon.centroidfinder;

import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VideoFrameExtractorTest {

    @Test
    public void testExtractGroupsFromEnsantinaVideo() throws FrameGrabber.Exception, IOException {
        String filepath = "sampleInput/ensantina.mp4";
        int threshold = 100;
        int hexTargetColor = 0x4A0000;

        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(filepath, threshold, hexTargetColor);

        String outputPath = "sampleOutput/ensantina_groups.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (ImageFrame frame : frames) {
                Group group = frame.group();
                writer.write(group.toCsvRow());
                writer.newLine();
            }
        }

        System.out.println("Group data written to: " + outputPath);
    }

    @Test
    public void testExtractGroupsFromWhiteBox() throws FrameGrabber.Exception, IOException, Exception {
        String filepath = testVideo().toString();
        int threshold = 100;
        int hexTargetColor = 0xffffff;

        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(filepath, threshold, hexTargetColor);

        String outputPath = "sampleOutput/small_groups.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (ImageFrame frame : frames) {
                Group group = frame.group();
                writer.write(group.toCsvRow());
                writer.newLine();
            }
        }

        System.out.println("Group data written to: " + outputPath);
    }

    @Test
    public void testExtractGroupsFromCyanBoxWithAssertions() throws FrameGrabber.Exception, IOException, Exception {
        String filepath = movingTestVideo().toString();
        int threshold = 60;
        int hexTargetColor = 0x00FFFF; // Cyan

        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(filepath, threshold, hexTargetColor);

        String outputPath = "sampleOutput/moving_cyan_groups.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            int prevX = -1;
            int prevY = -1;

            for (ImageFrame frame : frames) {
                Group group = frame.group();
                writer.write(group.toCsvRow());
                writer.newLine();

                int x = group.centroid().x();
                int y = group.centroid().y();
                System.out.println("Centroid: x=" + x + ", y=" + y);

                // if (prevX != -1 && prevY != -1) {
                //     assertTrue(x >= prevX, "X should not decrease: prev=" + prevX + ", current=" + x);
                //     assertTrue(y >= prevY, "Y should not decrease: prev=" + prevY + ", current=" + y);
                // }

                prevX = x;
                prevY = y;
            }
        }

        System.out.println("Group data written to: " + outputPath);
    }



    public Path testVideo() throws Exception {
        Path tempFile = Files.createTempFile("test-video", ".mp4");
        tempFile.toFile().deleteOnExit();

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toFile(), 180, 180)) {
            recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(12);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            int totalFrames = (int) Math.round(4 * 12);
            for (int i = 0; i < totalFrames; i++) {
                java.awt.Color c = new java.awt.Color(255, 255, 255);
                BufferedImage img = new BufferedImage(180, 180, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = img.createGraphics();
                g.setColor(c);
                g.fillRect(0, 0, 180/2, 180/2);
                g.dispose();

                recorder.record(converter.convert(img));
            }

            recorder.stop();
            converter.close();
        }

        return tempFile;
    }

    public Path movingTestVideo() throws Exception {
        Path tempFile = Files.createTempFile("test-video", ".mp4");
        tempFile.toFile().deleteOnExit();

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toFile(), 180, 180)) {
            recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(12);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            int totalFrames = 4 * 12;
            int rectWidth = 90;
            int rectHeight = 90;
            int maxX = 180 - rectWidth;
            int maxY = 180 - rectHeight;

            for (int i = 0; i < totalFrames; i++) {
                BufferedImage img = new BufferedImage(180, 180, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = img.createGraphics();

                java.awt.Color background = new java.awt.Color(0, 0, 0);
                java.awt.Color rectColor = new java.awt.Color(0, 255, 255); // Cyan

                g.setColor(background);
                g.fillRect(0, 0, 180, 180);

                int x = (int) ((i / (double)(totalFrames - 1)) * maxX);
                int y = (int) ((i / (double)(totalFrames - 1)) * maxY);

                g.setColor(rectColor);
                g.fillRect(x, y, rectWidth, rectHeight);

                g.dispose();
                recorder.record(converter.convert(img));
            }

            recorder.stop();
            converter.close();
        }

        return tempFile;
    }

}
