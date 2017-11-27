package com.loscache.firebirdone.background;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.loscache.firebirdone.data.DataReaderDbContext;
import com.loscache.firebirdone.data.MeasurementModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by ClaudioSaccella on 8/11/2017.
 */

public class MeasurementRequest extends AsyncTask<Integer, MeasurementModel, Integer> {

    private DataReaderDbContext dbContext;

    private BluetoothSocket bluetoothSocket;

    public MeasurementRequest(DataReaderDbContext dbContext, BluetoothSocket bluetoothSocket){
        super();
        this.dbContext = dbContext;
        this.bluetoothSocket = bluetoothSocket;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {

        Random rnd = new Random();

        try {
            while(!isCancelled()){

                    Log.i("REQUEST", "MeasurementRequest");

                    bluetoothSocket.getOutputStream().write(50);



                    // Aca debería solicitar los datos al arduino

                    byte[] buffer = new byte[512];
                    int bytes = bluetoothSocket.getInputStream().read(buffer);
                    String readMessage =  new String(buffer, 0, bytes);

                    Log.i("JSON", readMessage);

                try {
                    JSONObject result =  new JSONObject(readMessage);



                    String temperature = String.valueOf(result.getInt("temperature"));
                    String flame = String.valueOf(result.getInt("flame"));
                    String smoke = String.valueOf(result.getInt("smoke"));
                    String food = String.valueOf(result.getInt("food"));
                    String water = String.valueOf(result.getInt("water"));
                    //String temperature = (rnd.nextInt(10) + 30) + "." + rnd.nextInt(10) + "º";
                    /*String flame = rnd.nextBoolean() ? "Detectado" : "No detectado";
                    String smoke = rnd.nextBoolean() ? "Detectado" : "No detectado";
                    String food = rnd.nextBoolean() ? "Alto" : "Medio";
                    String water = rnd.nextBoolean() ? "Alto" : "Bajo";*/

                    // fin leer ultima medicion
                    publishProgress(
                            new MeasurementModel(
                                    Calendar.getInstance().getTime().toLocaleString(),
                                    temperature,
                                    smoke,
                                    flame,
                                    food,
                                    water,
                                    false));     // is not history

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onProgressUpdate(MeasurementModel... values) {

        if(isCancelled())
            return;

        // Get the value
        MeasurementModel measurementModel = values[0];

        // Store in db
        dbContext.insert(measurementModel);
        dbContext.clearMeasurements();

    }
}
