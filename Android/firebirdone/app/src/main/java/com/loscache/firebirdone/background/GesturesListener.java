package com.loscache.firebirdone.background;

import android.bluetooth.BluetoothSocket;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.Toast;

import com.loscache.firebirdone.data.DataReaderDbContext;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by cdsac on 05/11/2017.
 */

public class GesturesListener implements SensorEventListener {



    // Accelerometer
    private long lastRefresh = 0, lastMovement = 0;
    private float x = 0, y = 0, z = 0, lastX = 0, lastY = 0, lastZ = 0;

    // Giroscope
    private boolean antiClockWise = false, clockWise = false;

    // Proximity
    private float maxRange;

    // Bluetooth
    private BluetoothSocket bluetoothSocket;
    OutputStream outputStream;
    private static int ACTION_LIGHT = 11;
    private static int ACTION_SOUND = 22;
    private static int ACTION_WATER= 33;

    public GesturesListener(float proximityMaxRange, BluetoothSocket bluetoothSocket){
        maxRange = proximityMaxRange;
        this.bluetoothSocket = bluetoothSocket;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {

            float [] values =  event.values;


            // Bluetooth
            try {
                outputStream =  bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch(event.sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER:

                    long currentTime = event.timestamp;

                    x = values[0];
                    y = values[1];
                    z = values[2];

                    if(lastX == 0 && lastY == 0 && lastZ == 0) {
                        lastRefresh = currentTime;
                        lastMovement = currentTime;
                        lastX = x;
                        lastY = y;
                        lastZ = z;
                    }

                    long difference = currentTime - lastRefresh;

                    if(difference > 0) {

                        float movement = Math.abs((x + y + z) - (lastX - lastY - lastZ)) / difference;
                        int limit = 1500;
                        float minMovement = 1E-8f;

                        if(movement > minMovement) {

                            if(currentTime - lastMovement >= limit) {

                                if(Math.abs(y - lastY) < 1 && Math.abs(x - lastX) > 8 && Math.abs(z - lastZ) < 1) {

                                    // TO DO MOVEMENT
                                    Log.i("SENSOR", "ACCELEROMETER");
                                    sendToDevice(ACTION_LIGHT);

                                }
                            }
                        }

                        lastMovement = currentTime;
                    }

                    lastX = x;
                    lastY = y;
                    lastZ = z;
                    lastRefresh = currentTime;

                    break;

                case Sensor.TYPE_GYROSCOPE:

                    if(event.values[0] > 5f) { // anticlockwise
                        antiClockWise = !antiClockWise;
                    } else if(event.values[0] < - 5f) { // clockwise
                        clockWise = !clockWise;
                    }

                    if(antiClockWise && clockWise) {
                        // TO DO WHIP
                        Log.i("SENSOR", "GIROSCOPE");
                        sendToDevice(ACTION_WATER);
                        antiClockWise = false;
                        clockWise = false;
                    }
                    break;

                case Sensor.TYPE_PROXIMITY:
                   if(event.values[0] < maxRange) {
                       Log.i("SENSOR", "PROXIMITY_NEAR");
                       sendToDevice(ACTION_SOUND);
                        // TO DO NEAR
                    } else {
                       Log.i("SENSOR", "PROXIMITY_AWAY");
                        // TO DO AWAY
                    }
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void sendToDevice(int action){
        if(outputStream != null){
            try {
                outputStream.write(action);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
