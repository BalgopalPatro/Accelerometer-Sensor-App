package com.balgopal.accelerometer;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        text = findViewById(R.id.my_text);

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onSensorChanged(SensorEvent event) {
////        int sensor = event.type;
//        Log.w("Called","onSensor"+event.sensor);
//        float[] values = event.values;
//        int i;
//        StringBuffer str = new StringBuffer();
//        TextView text = findViewById(R.id.my_text);
//        float[] R = new float[9]; // rotation matrix
//        float[] magnetic = new float[3];
//        float[] orientation = new float[3];
//
//        magnetic[0] = 0;
//        magnetic[1] = 1;
//        magnetic[2] = 0;
//
//        str.append("From Sensor :\n");
//        for (i = 0; i < values.length; i++) {
//            double t = values[i]/360 * 255 ;
////            str.append(values[i] + " " +t);
//            str.append(Math.abs ((int)t));
//            str.append(", ");
//
//        }
//
//
//
//        SensorManager.getRotationMatrix(R, null, values, magnetic);
//        SensorManager.getOrientation(R, orientation);
//
//
//        str.append("\n\nGives :\n");
//        for (i = 0; i < orientation.length; i++) {
//            str.append(orientation[i]);
//            str.append(", ");
//        }
//
//        int r  =  Math.abs((int)values[0]/360 * 255);
//        int g =   Math.abs((int)values[1]/360 * 255 );
//        int b =   Math.abs((int)values[2]/360 * 255 );
//
//
//        str.append("\n"+"red"+r+"Green"+g+"Blue"+b);
//        text.setBackgroundColor(Color.rgb((int)values[0]/360 * 255,values[1]/360 * 255,(int)values[2]/360 * 255 ));
//
//        Log.w("Color"," "+(int)values[0]/360 * 255 +" " +values[1]/360 * 255 +" "+(int)values[2]/360 * 255 );
//        text.setText(str);
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // register this class as a listener for the orientation and
//        // accelerometer sensors
//        sensorManager.registerListener(this,
//                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onPause() {
//        // unregister listener
//        super.onPause();
//        sensorManager.unregisterListener(this);
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    // Get readings from accelerometer and magnetometer. To simplify calculations,
    // consider storing these readings as unit vectors.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }
        updateOrientationAngles();
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        int r  =  Math.abs((int) (orientationAngles[0]/3.14 *255 ));
        int g =   Math.abs((int)(orientationAngles[1]/3.14 *255 ) );
        int b =   Math.abs((int)(orientationAngles[2]/3.14 *255));

        text.setText(r+" "+g +" "+b +" ");
        text.setBackgroundColor(Color.rgb(r,g,b));

    }
}
