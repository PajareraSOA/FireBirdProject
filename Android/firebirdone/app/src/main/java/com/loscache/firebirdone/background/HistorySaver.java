package com.loscache.firebirdone.background;

import android.os.AsyncTask;
import android.util.Log;

import com.loscache.firebirdone.data.DataReaderDbContext;
import com.loscache.firebirdone.data.MeasurementModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by cdsac on 06/11/2017.
 */

public class HistorySaver extends AsyncTask<Integer, MeasurementModel, Integer> {

    private DataReaderDbContext dbContext;

    public HistorySaver(DataReaderDbContext dbContext){
        super();
        this.dbContext = dbContext;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {

            //Random rnd = new Random();

            try {
                while(!isCancelled()){

                    Log.i("REQUEST", "HistorySaver");
                    // Aca debería leer la ultima medición en la base de datos
               /*     String temperature = (rnd.nextInt(10) + 30) + "." + rnd.nextInt(10) + "º";
                    String flame = rnd.nextBoolean() ? "Detectado" : "No detectado";
                    String smoke = rnd.nextBoolean() ? "Detectado" : "No detectado";
                    String food = rnd.nextBoolean() ? "Alto" : "Medio";
                    String water = rnd.nextBoolean() ? "Alto" : "Bajo";*/
                    ArrayList<MeasurementModel> measurementModels = dbContext.getLastMeasurement();
                    // fin leer ultima medicion
/*                    publishProgress(
                            new MeasurementModel(
                                    Calendar.getInstance().getTime().toLocaleString(),
                                    temperature,
                                    smoke,
                                    flame,
                                    food,
                                    water,
                                    true));*/
                    if(measurementModels.size() > 0)
                        publishProgress(measurementModels.get(0));
                    Thread.sleep(5000);
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

        // Store in db
        if(dbContext.countHistories() >= 3){
            dbContext.deleteLastHistory();
            //historyRowAdapter.removeLastItem();
        }
        measurementModel.setIsHistory(true);
        dbContext.insert(measurementModel);

/*        // show in info fragment
        txtTemperature.setText(measurementHistoryModel.getTemperature());
        txtSmoke.setText(measurementHistoryModel.getSmoke());
        txtFlame.setText(measurementHistoryModel.getFlame());
        txtFood.setText(measurementHistoryModel.getFood());
        txtWater.setText(measurementHistoryModel.getWater());*/
/*        // Show in list view
       historyRowAdapter.addItem(values[0]);
        historyRowAdapter.notifyDataSetChanged();*/

    }
}
