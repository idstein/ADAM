package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

public class SignDetector implements MatrixProcessor {

    final FeatureDetector detector;
    final DescriptorExtractor desciptor;
    final DescriptorMatcher matcher;

    public SignDetector() {
        detector = FeatureDetector.create(FeatureDetector.ORB);
        desciptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
        String fileName = SignDetector.class.getResource("/righ-of-way/z102.gif").getFile();
        Mat testImg = Imgcodecs.imread(fileName, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        MatOfKeyPoint kp = new MatOfKeyPoint();
        detector.detect(testImg,kp);
        Mat despp = new Mat();
        desciptor.compute(testImg,kp,despp);
        //matcher.match();
    }

    @Override
    public Mat process(Mat input) {

        return input;
    }

    @Override
    public void visualize(Mat input) {

    }
}
