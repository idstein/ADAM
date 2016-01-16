package de.uni_frankfurt.cs.ccc.sse.adam;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StreetViewActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "StreetViewActivity";
    private Bitmap edgeBitmap;
    private CameraBridgeViewBase mDashcamView;
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
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    private int width;
    private int height;
    private Mat matRgba;
    private Mat matGray;
    private Mat matEdges;
    private int midLaneX;
    private int minLaneX;
    private int maxLaneX;
    private int minLeftLaneY;
    private int minRightLaneY;
    private int maxLaneY;
    private boolean isLeftHorizontal;
    private boolean isRightHorizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashcam);

        mDashcamView = (CameraBridgeViewBase) findViewById(R.id.dashcam_java_surface_view);
        mDashcamView.setCvCameraViewListener(this);
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
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getApplicationContext(), loaderCallback);
    }

    /**
     * Image segmentation and edge detection
     *
     */
    private void segmentation() {

        //Bottom half of landscape image
        //if (viewMode == VIEW_MODE_OPENCV_LINES_HORIZON || orientation == 1) {
            matGray.submat(height / 2, height, 0, width).copyTo(matEdges.submat(height / 2, height, 0, width));
        //}
        //Bottom third of portrait image
        //else {
        //    matGray.submat(0, height, (2 * width) / 3, width).copyTo(matEdges.submat(0, height, (2 * width) / 3, width));
        //}

        /* Gaussian blur
         *
         Imgproc.GaussianBlur(matEdges, matEdges, new Size(15,15), 0.5);
         */

        /* Static threshold
         *
         //Imgproc.threshold(matEdges, matEdges, 175, 255, Imgproc.THRESH_BINARY);
         //Imgproc.threshold(matEdges, matEdges, 175, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
         */

        /* Adaptive threshold
         **/
        //Imgproc.adaptiveThreshold(matEdges, matEdges, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 3, -1.5);

        //Delete noise (little white points)
        //Imgproc.dilate(matEdges, matEdges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
        //Imgproc.erode(matEdges, matEdges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
        //Imgproc.medianBlur(matEdges, matEdges, 3);


        /* Canny edge detection
         *
        double mean = Core.mean(matGray).val[0];
        Imgproc.Canny(matEdges, matEdges, 0.66 * mean, 1.33 * mean);
        //Delete white edge at border of segmentation
        if (orientation == 1) {
            matEdges.rowRange(height / 2 - 1, height / 2 + 1).setTo(new Scalar(0));
        } else {
            matEdges.colRange(((2 * width) / 3) - 1, ((2 * width) / 3) + 1).setTo(new Scalar(0));
        }
        */
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        this.width = width;
        this.height = height;

        matRgba = new Mat(height, width, CvType.CV_8UC4);
        matGray = new Mat(height, width, CvType.CV_8UC1);
        matEdges = new Mat(height, width, CvType.CV_8UC1);
        edgeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        //leftLane = new Line(0, height, width, 0);
        //rightLane = new Line(width, height, 0, 0);

        midLaneX = width / 2;
        minLaneX = 0;
        maxLaneX = width;

        minLeftLaneY = 3 * height / 4;
        minRightLaneY = 3 * height / 4;
        maxLaneY = height;

        isLeftHorizontal = true;
        isRightHorizontal = true;
        mDashcamView.enableFpsMeter();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}
