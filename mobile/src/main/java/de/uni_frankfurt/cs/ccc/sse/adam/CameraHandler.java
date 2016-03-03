package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

public class CameraHandler implements CameraBridgeViewBase.CvCameraViewListener2 {
    BackgroundSubtractor backgroundSubtractor;
    LaneDetectProcessor laneDetector = new LaneDetectProcessor();

    public void onCameraViewStarted(int width, int height) {
        backgroundSubtractor = Video.createBackgroundSubtractorMOG2();
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat input = inputFrame.rgba();
        Mat roi = input.rowRange((int) (input.rows() * 0.48), input.rows());
        Mat mask = new Mat();
        backgroundSubtractor.apply(roi, mask);
        Mat output = new Mat();
        roi.copyTo(output, mask);
        laneDetector.process(output);
        laneDetector.drawLanes(roi);
        return input;
    }
}
