package com.loscache.firebirdone.background;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.loscache.firebirdone.data.DataReaderDbContext;
import com.loscache.firebirdone.data.MeasurementModel;

import java.util.ArrayList;

/**
 * Created by ClaudioSaccella on 7/11/2017.
 */

public class InfoRefresh extends AsyncTask<Integer, MeasurementModel, Integer> {

    private TextView txtTemperature;
    private TextView txtSmoke;
    private TextView txtFlame;
    private TextView txtFood;
    private TextView txtWater;

    private DataReaderDbContext dbContext;

    public InfoRefresh(
            TextView txtTemperature,
            TextView txtSmoke,
            TextView txtFlame,
            TextView txtFood,
            TextView txtWater,
            DataReaderDbContext dbContext
    ){
        super();
        this.txtTemperature = txtTemperature;
        this.txtSmoke = txtSmoke;
        this.txtFlame = txtFlame;
        this.txtFood = txtFood;
        this.txtWater = txtWater;
        this.dbContext = dbContext;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {


        try {
            while(!isCancelled()){

                Log.i("REQUEST", "InfoRefresh");

                // Leer la ultima medicion
                ArrayList<MeasurementModel> measurementModels = dbContext.getLastMeasurement();

                // Actualizar GUI
                if(measurementModels.size() > 0)
                    publishProgress(measurementModels.get(0));

                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
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

        // show in info fragment
        txtTemperature.setText(measurementModel.getTemperature());
        txtSmoke.setText(measurementModel.getSmoke());
        txtFlame.setText(measurementModel.getFlame());
        txtFood.setText(measurementModel.getFood());
        txtWater.setText(measurementModel.getWater());

    }
}
