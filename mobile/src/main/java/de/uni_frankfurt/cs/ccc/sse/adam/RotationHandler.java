package de.uni_frankfurt.cs.ccc.sse.adam;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RotationHandler implements SensorEventListener {

    private final double ROTATION_RATE_TRESHOLD = 5.00;


    private ImageView right_arrow;
    private ImageView left_arrow;
    private SensorManager sensorManager;
    private Sensor senGyro;
    private double currOrientation;
    private TextView txtCurrentOrientation;


    public RotationHandler(ImageView right_arrow, ImageView left_arrow, TextView textView, SensorManager sensorManager){

        this.left_arrow = left_arrow;
        this.right_arrow = right_arrow;
        this.sensorManager = sensorManager;
        this.txtCurrentOrientation = textView;
        senGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, senGyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_GYROSCOPE:
                currOrientation = Math.toDegrees(event.values[1]);
                double orientation = Math.round((currOrientation)* 100.0) / 100.0;
                setRotationText(orientation);

                if(isHigherThanTreshold(orientation)){
                    signalRotation(orientation);
                }else{
                    hideAllSignals();
                }
            default:
                return;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private boolean isHigherThanTreshold(double rate){
        return Math.abs(rate) > ROTATION_RATE_TRESHOLD;
    }

    private void signalRotation(double orientation){
        if(orientation > 0.00){
            right_arrow.setVisibility(View.INVISIBLE);
            left_arrow.setVisibility(View.VISIBLE);
        }else{
            left_arrow.setVisibility(View.INVISIBLE);
            right_arrow.setVisibility(View.VISIBLE);
        }
    }

    private void hideAllSignals(){
        left_arrow.setVisibility(View.INVISIBLE);
        right_arrow.setVisibility(View.INVISIBLE);
    }

    private void setRotationText(double rate){
        txtCurrentOrientation.setText("Rotation Rate: " +rate);
    }
}
