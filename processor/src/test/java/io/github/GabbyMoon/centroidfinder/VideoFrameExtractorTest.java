package io.github.GabbyMoon.centroidfinder;

import org.junit.jupiter.api.Test;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.Color;

public class VideoFrameExtractorTest {

    @Test
    public void testExtractGroupsFromEnsantinaVideo() throws FrameGrabber.Exception, IOException {
        String filepath = "../videos/test-video-ensantina.mp4"; // updated video path
        int threshold = 100;
        int hexTargetColor = 0x4A0000;

        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(filepath, threshold, hexTargetColor);

        String outputPath = "../results/ensantina_groups.csv"; // updated to results folder
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
        String filepath = testVideo(4).toString();
        int threshold = 100;
        int hexTargetColor = 0xffffff;

        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(filepath, threshold, hexTargetColor);

        String outputPath = "../results/small_groups.csv";
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

        String outputPath = "../results/moving_cyan_groups.csv"; // updated folder
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

                prevX = x;
                prevY = y;
            }
        }

        System.out.println("Group data written to: " + outputPath);
    }

    @Test
    public void testExtractedCyanCentroids() throws FrameGrabber.Exception, IOException, Exception {
        String filepath = movingTestVideo().toString();
        int threshold = 60;
        int hexTargetColor = 0x00FFFF; // Cyan

        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(filepath, threshold, hexTargetColor);

        Path outputPath = Paths.get("../results/moving_cyan_groups.csv");
        Files.createDirectories(outputPath.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            for (ImageFrame frame : frames) {
                Group group = frame.group();
                writer.write(group.toCsvRow());
                writer.newLine();
            }
        }

        int prevX = -1;
        int prevY = -1;
        int allowedRounding = 1;

        for (ImageFrame frame : frames) {
            Group group = frame.group();
            int x = group.centroid().x();
            int y = group.centroid().y();
            prevX = x;
            prevY = y;
        }
    }

    public Path testVideo(int secs) throws Exception {
        Path tempFile = Files.createTempFile("test-video", ".mp4");
        tempFile.toFile().deleteOnExit();

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toFile(), 180, 180)) {
            recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(12);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            int totalFrames = (int) Math.round(secs * 12);
            for (int i = 0; i < totalFrames; i++) {
                java.awt.Color c = new java.awt.Color(255, 255, 255);
                BufferedImage img = new BufferedImage(180, 180, BufferedImage.TYPE_INT_RGB);
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
        Path tempFile = Files.createTempFile("testing_square", ".mp4"); // updated file name
        tempFile.toFile().deleteOnExit();

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toFile(), 180, 180)) {
            recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(12);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            int totalFrames = 4 * 12; // 4 seconds at 12 FPS
            int rectWidth = 90;
            int rectHeight = 90;
            int maxX = 180 - rectWidth;
            int maxY = 180 - rectHeight;

            for (int i = 0; i < totalFrames; i++) {
                BufferedImage img = new BufferedImage(180, 180, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();

                Color background = new Color(0, 0, 0);
                Color rectColor = new Color(0, 255, 255); // Cyan

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

    @Test
    public void movingSquareVideoWithCsv() throws Exception {
        Path videoPath = Paths.get("../results/moving_cyan_square.mp4"); // results folder
        Files.createDirectories(videoPath.getParent());

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoPath.toFile(), 180, 180)) {
            recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(12);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            int totalFrames = 4 * 12;
            int rectWidth = 30; // smaller for centroid
            int rectHeight = 30;
            int maxX = 180 - rectWidth;
            int maxY = 180 - rectHeight;

            for (int i = 0; i < totalFrames; i++) {
                BufferedImage img = new BufferedImage(180, 180, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = img.createGraphics();

                g.setColor(new java.awt.Color(0, 0, 0));
                g.fillRect(0, 0, 180, 180);

                int x = (int) ((i / (double)(totalFrames - 1)) * maxX);
                int y = (int) ((i / (double)(totalFrames - 1)) * maxY);

                g.setColor(new java.awt.Color(0, 255, 255));
                g.fillRect(x, y, rectWidth, rectHeight);

                g.dispose();
                recorder.record(converter.convert(img));
            }

            recorder.stop();
            converter.close();
        }

        System.out.println("Video saved to: " + videoPath);

        List<ImageFrame> frames = new VideoFrameExtractor().extractFrames(videoPath.toString(), 60, 0x00FFFF);
        Path csvPath = Paths.get("../results/moving_cyan_square.csv");
        Files.createDirectories(csvPath.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            for (ImageFrame frame : frames) {
                writer.write(frame.group().toCsvRow());
                writer.newLine();
            }
        }

        System.out.println("CSV saved to: " + csvPath);
    }

}