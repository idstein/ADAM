package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

public class CameraHandler implements CameraBridgeViewBase.CvCameraViewListener2 {
    BackgroundSubtractor backgroundSubtractor;
    LaneDetectProcessor laneDetector = new LaneDetectProcessor();
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
            fps = 0;
        } else {
            fps ++;
        }
        laneDetector.drawLanes(input);
        laneDetector.drawVanishingPoints(input);
        return input;
    }
}
