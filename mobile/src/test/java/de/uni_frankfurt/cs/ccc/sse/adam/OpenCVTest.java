package de.uni_frankfurt.cs.ccc.sse.adam;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.opencv.core.CvType.CV_8UC1;

public class OpenCVTest {
    static {
        System.load(System.getProperty("OPENCV_JAVA_LIB","/usr/local/Cellar/opencv3/3.1.0_1/share/OpenCV/java") + "/libopencv_java310.so");
    }

    public static void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = javax.imageio.ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOpenCVLoaded() {
        Mat testMat = Mat.ones(1, 1, CV_8UC1);
        assertNotNull(testMat);
        assertThat(testMat.nativeObj, not(0l));
        System.out.println(testMat.size().toString());
    }

    @Test
    public void findLanes() throws Exception {
        Mat testImg = Imgcodecs.imread(OpenCVTest.class.getResource("/road.png").getFile(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        FrameProcessor filter = new FrameProcessor();
        Mat result = filter.process(testImg);
        LaneDetectProcessor laneDetector = new LaneDetectProcessor();
        Mat lanes = laneDetector.process(testImg);

        //Draw line segments
        for (int i = 0; i < lanes.rows(); i++) {
            //double[] vec = lanes.get(0, i);
            double[] vec = lanes.get(i, 0);
            double x1 = vec[0],
                    y1 = vec[1],
                    x2 = vec[2],
                    y2 = vec[3];

            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);

            Imgproc.line(testImg, start, end, new Scalar(0, 0, 255), 3);
        }
        Imgshow.show(testImg);

        // Should find 2 lanes, with 3 lines in sum
        assertThat(lanes.rows(), CoreMatchers.equalTo(3));
        Thread.sleep(2000);
    }

    @Test
    public void findLanesDay() throws Exception {

        VideoCapture capture = new VideoCapture(OpenCVTest.class.getResource("/day.mp4").getFile());
        assertThat(capture.isOpened(), CoreMatchers.equalTo(true));
        int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        int frame_rate = (int) capture.get(Videoio.CAP_PROP_FPS);
        Imgshow win = new Imgshow("Day", width, height);
        BackgroundSubtractor backgroundSubtractor = //Video.createBackgroundSubtractorKNN(frame_rate, 200, false);
         Video.createBackgroundSubtractorMOG2(frame_rate*90, 90, false);
        LaneDetectProcessor laneDetector = new LaneDetectProcessor();
        HorizonDetectProcessor horizonDetector = new HorizonDetectProcessor();
        int count = 0;
        int fps = 0;
        long currentTimeMillis = System.currentTimeMillis();
        Mat inputImg = new Mat();
        Mat lanes = new Mat();
        Random rng = new Random();
        while (capture.read(inputImg) && fps < 5000) {
            // this should be the vanishing point, instead of an arbitrary bottom half of the image
            Mat roi = inputImg.rowRange((int) (inputImg.rows()*0.48), inputImg.rows());
            Mat mask = new Mat();
            backgroundSubtractor.apply(roi, mask);
            Mat output = new Mat();
            roi.copyTo(output, mask);
            //if(fps % frame_rate == 0) {
            inputImg = laneDetector.process(output);
            //}
            laneDetector.drawLanes(roi);
            win.showImage(inputImg);
            fps++;
        }
        long duration = System.currentTimeMillis() - currentTimeMillis;
        System.out.println("Executed " + fps + " in " + duration + " msec (" + fps * 1000 / duration + "fps/sec)");
    }

    @Test
    public void findLanesNight() throws Exception {
        VideoCapture capture = new VideoCapture(OpenCVTest.class.getResource("/night.mp4").getFile());
        assertThat(capture.isOpened(), CoreMatchers.equalTo(true));
        int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        int frame_rate = (int) capture.get(Videoio.CAP_PROP_FPS);
        Imgshow win = new Imgshow("Day", width, height);
        LaneDetectProcessor laneDetector = new LaneDetectProcessor();
        HorizonDetectProcessor horizonDetector = new HorizonDetectProcessor();
        BackgroundSubtractor backgroundSubtractor = Video.createBackgroundSubtractorMOG2(frame_rate*90, 30, false);
        int count = 0;
        int fps = 0;
        long currentTimeMillis = System.currentTimeMillis();
        Mat inputImg = new Mat();
        Mat lanes = new Mat();
        Random rng = new Random();
        while (capture.read(inputImg) && fps < 5000) {
            // this should be the vanishing point, instead of an arbitrary bottom half of the image
            Mat roi = inputImg.rowRange((int) (inputImg.rows()*0.48), inputImg.rows());
            Mat mask = new Mat();
            backgroundSubtractor.apply(roi, mask);
            Mat output = new Mat();
            roi.copyTo(output, mask);

            Mat grayscale = new Mat();
            Imgproc.cvtColor(roi, grayscale, Imgproc.COLOR_BGR2GRAY);

            // Night mode
            Imgproc.equalizeHist(grayscale, grayscale);

            //Imgproc.Canny(grayscale, grayscale, 200, 250, 3, false);
            //Mat mask = new Mat();
            /*backgroundSubtractor.apply(roi, mask);
            Mat output = new Mat();
            roi.copyTo(output, mask);*/
            //if(fps % frame_rate == 0) {
            laneDetector.process(output);
            //}
            laneDetector.drawLanes(roi);
            win.showImage(grayscale);
            fps++;
        }
        long duration = System.currentTimeMillis() - currentTimeMillis;
        System.out.println("Executed " + fps + " in " + duration + " msec (" + fps * 1000 / duration + "fps/sec)");
    }

    @Test
    public void findHorizon() throws Exception {
        HorizonDetectProcessor horizonDetector = new HorizonDetectProcessor();
        Mat testImg = Imgcodecs.imread(OpenCVTest.class.getResource("/road.png").getFile(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Imgshow win = new Imgshow("Day", testImg.cols(), testImg.rows());
        win.showImage(testImg);
        win.showImage(horizonDetector.process(testImg));
    }
}
