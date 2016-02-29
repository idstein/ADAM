package de.uni_frankfurt.cs.ccc.sse.adam;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

import static java.lang.Boolean.valueOf;
import static org.opencv.android.LoaderCallbackInterface.INIT_FAILED;
import static org.opencv.android.LoaderCallbackInterface.SUCCESS;
import static org.opencv.android.OpenCVLoader.OPENCV_VERSION_3_1_0;
import static org.opencv.android.OpenCVLoader.initAsync;
import static org.opencv.android.OpenCVLoader.initDebug;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StreetViewActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "StreetViewActivity";
    private CameraBridgeViewBase mDashcamView;

    private Mat lanes;
    /**
     * Load OpenCV Manager
     */
    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV library loaded successfully");
                    mDashcamView.enableView();
                    lanes = new Mat();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashcam);

        mDashcamView = (CameraBridgeViewBase) findViewById(R.id.dashcam_java_surface_view);
        mDashcamView.setCvCameraViewListener(this);
        mDashcamView.setMaxFrameSize(640,480);
    }

    @Override
    protected void onDestroy() {
        mDashcamView.disableView();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        if (valueOf(System.getProperty("UNIT_TEST", Boolean.FALSE.toString()))) {
            loaderCallback.onManagerConnected(initDebug(false) ? SUCCESS : INIT_FAILED);
        } else {
            initAsync(OPENCV_VERSION_3_1_0, getApplicationContext(), loaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        if (BuildConfig.DEBUG)
            mDashcamView.enableFpsMeter();
        backgroundSubtractor = Video.createBackgroundSubtractorMOG2();
    }

    @Override
    public void onCameraViewStopped() {

    }

    BackgroundSubtractor backgroundSubtractor;

    LaneDetectProcessor laneDetector = new LaneDetectProcessor();
    int count = 0;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat input = inputFrame.rgba();
        Mat roi = input.rowRange((int) (input.rows()*0.48), input.rows());
        Mat mask = new Mat();
        backgroundSubtractor.apply(roi, mask);
        Mat output = new Mat();
        roi.copyTo(output, mask);
        //if(fps % 25 == 0) {
        //if(count%25==0) {
            laneDetector.process(output);
        //    count = 0;
        //} else {
        //    count ++;
        //}
        laneDetector.drawLanes(roi);
        //}

        return input;
    }
}
