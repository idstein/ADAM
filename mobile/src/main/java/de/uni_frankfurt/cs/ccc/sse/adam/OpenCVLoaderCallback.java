package de.uni_frankfurt.cs.ccc.sse.adam;

import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

class OpenCVLoaderCallback extends BaseLoaderCallback {
    private StreetViewActivity streetViewActivity;

    public OpenCVLoaderCallback(StreetViewActivity streetViewActivity) {
        super(streetViewActivity);
        this.streetViewActivity = streetViewActivity;
    }

    @Override
    public void onManagerConnected(int status) {
        switch (status) {
            case LoaderCallbackInterface.SUCCESS: {
                Log.i("OpenCVLoaderCallback", "OpenCV library loaded successfully");
                streetViewActivity.onOpenCVLoaded();
            }
            break;
            default: {
                super.onManagerConnected(status);
            }
            break;
        }
    }

}
