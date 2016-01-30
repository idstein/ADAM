package de.uni_frankfurt.cs.ccc.sse.adam;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.opencv.core.CvType.CV_8UC1;

public class OpenCVTest {
    static {
        System.load("/usr/local/Cellar/opencv3/3.1.0_1/share/OpenCV/java/libopencv_java310.so");
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
        Mat testImg = Imgcodecs.imread(OpenCVTest.class.getResource("/road.png").getFile(),Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        FrameProcessor filter = new FrameProcessor();
        Mat result = filter.process(testImg);
        LaneDetectProcessor laneDetector = new LaneDetectProcessor();
        Mat horizon = laneDetector.process(result);
        Imgcodecs.imwrite("road_result.png",horizon);
    }
}
