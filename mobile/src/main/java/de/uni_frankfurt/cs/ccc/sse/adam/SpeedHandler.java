package de.uni_frankfurt.cs.ccc.sse.adam;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SpeedHandler implements IBaseGpsListener {

    private Location mLastLocation;
    private LocationManager locationManager;
    private TextView txtCurrentSpeed;

    public SpeedHandler(TextView txt, LocationManager locationManager) {
        this.txtCurrentSpeed = txt;
        this.locationManager = locationManager;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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


    private double distance_on_geoid(double lat1, double lon1, double lat2, double lon2) {
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

        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
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
}
