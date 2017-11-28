package com.loscache.firebirdone.background;

import android.os.AsyncTask;
import android.util.Log;

import com.loscache.firebirdone.data.DataReaderDbContext;
import com.loscache.firebirdone.data.MeasurementModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;



public class HistorySaver extends AsyncTask<Integer, MeasurementModel, Integer> {

    private DataReaderDbContext dbContext;

    public HistorySaver(DataReaderDbContext dbContext){
        super();
        this.dbContext = dbContext;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {



            try {
                while(!isCancelled()){

                    Log.i("REQUEST", "HistorySaver");

                    ArrayList<MeasurementModel> measurementModels = dbContext.getLastMeasurement();
                    // fin leer ultima medicion

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
        }
        measurementModel.setIsHistory(true);
        dbContext.insert(measurementModel);



    }
}
