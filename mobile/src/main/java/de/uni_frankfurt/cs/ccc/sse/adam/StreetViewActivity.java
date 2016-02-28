package de.uni_frankfurt.cs.ccc.sse.adam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.List;

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
public class StreetViewActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, IBaseGpsListener, SensorEventListener {
    private static final String TAG = "StreetViewActivity";
    private Bitmap edgeBitmap;
    private CameraBridgeViewBase mDashcamView;

    private Location mLastLocation;

    private SensorManager senSensorManager;
    private Sensor senGyro;
    private double currOrientation;

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


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senGyro = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        senSensorManager.registerListener(this, senGyro, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onDestroy() {
        mDashcamView.disableView();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senGyro, SensorManager.SENSOR_DELAY_NORMAL);

        Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        if (valueOf(System.getProperty("UNIT_TEST", Boolean.FALSE.toString()))) {
            loaderCallback.onManagerConnected(initDebug(false) ? SUCCESS : INIT_FAILED);
        } else {
            initAsync(OPENCV_VERSION_3_1_0, getApplicationContext(), loaderCallback);
        }
    }

    /**
     * Image segmentation and edge detection
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
        Imgproc.adaptiveThreshold(matEdges, matEdges, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 3, -1.5);

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
        if (BuildConfig.DEBUG)
            mDashcamView.enableFpsMeter();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        return inputFrame.rgba();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "changed");
        Log.d("Location", location.hasSpeed()+" hasSpeed");
        Log.d("Location", location.getAccuracy() + " Accuracy");
        Log.d("Location", location.getAltitude() + " Altitude");
        Log.d("Location", location.hasBearing() + " hasBearing");
        Log.d("Location", location.getLatitude() + " Lat");
        Log.d("Location", location.getLongitude() + " Lng");
        if(location != null)
        {
            CLocation myLocation = new CLocation(location);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onGpsStatusChanged(int event) {
    }

    private void updateSpeed(CLocation pCurrentLocation) {
        //calcul manually speed
        double speed = 0;
        if (this.mLastLocation != null){

              double dist = distance_on_geoid(mLastLocation.getLatitude(), mLastLocation.getLongitude(), pCurrentLocation.getLatitude(), pCurrentLocation.getLongitude());
              double time_s = (mLastLocation.getTime() - pCurrentLocation.getTime()) / 1000.0;
              double speed_mps = dist / time_s;
              speed = Math.round(Math.abs((speed_mps * 3600.0) / 1000.0) * 100.0) / 100.0;
        }


        this.mLastLocation = pCurrentLocation;

        String strCurrentSpeed = speed+"";
        String strUnits = "km/hour";

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
    }

    double distance_on_geoid(double lat1, double lon1, double lat2, double lon2) {
        double M_PI = 3.14159265359;
        // Convert degrees to radians
        lat1 = lat1 * M_PI / 180.0;
        lon1 = lon1 * M_PI / 180.0;

        lat2 = lat2 * M_PI / 180.0;
        lon2 = lon2 * M_PI / 180.0;

        // radius of earth in metres
        double r = 6378100;

        // P
        double rho1 = r * Math.cos(lat1);
        double z1 = r * Math.sin(lat1);
        double x1 = rho1 * Math.cos(lon1);
        double y1 = rho1 * Math.sin(lon1);

        // Q
        double rho2 = r * Math.cos(lat2);
        double z2 = r * Math.sin(lat2);
        double x2 = rho2 * Math.cos(lon2);
        double y2 = rho2 * Math.sin(lon2);

        // Dot product
        double dot = (x1 * x2 + y1 * y2 + z1 * z2);
        double cos_theta = dot / (r * r);

        double theta = Math.acos(cos_theta);

        // Distance in Metres
        return r * theta;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("SENSOR", event.sensor.getStringType());
        switch (event.sensor.getType()){
            case Sensor.TYPE_GYROSCOPE:
                currOrientation = Math.toDegrees(event.values[1]);
                TextView txtCurrentOrientation = (TextView) this.findViewById(R.id.txtCurrentOrientation);
                double orientation = Math.round((currOrientation)* 100.0) / 100.0;
                txtCurrentOrientation.setText("Orientation: "+orientation);
            default:
                return;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
