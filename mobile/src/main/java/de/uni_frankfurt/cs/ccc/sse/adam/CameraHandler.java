package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

public class CameraHandler implements CameraBridgeViewBase.CvCameraViewListener2 {
    BackgroundSubtractor backgroundSubtractor;
    LaneDetectProcessor laneDetector = new LaneDetectProcessor();
    VehicleTrackingProcessor vehicleTracker = new VehicleTrackingProcessor();
    int fps = 0;

    public void onCameraViewStarted(int width, int height) {
        backgroundSubtractor = Video.createBackgroundSubtractorMOG2();
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat input = inputFrame.rgba();
        if (fps % 5 == 0) {
            laneDetector.process(input);
            vehicleTracker.process(input);
            fps = 0;
        } else {
            fps ++;
        }
        laneDetector.visualize(input);
        vehicleTracker.visualize(input);
        return input;
    }
}
