package com.loscache.firebirdone.background;

import android.os.AsyncTask;
import android.util.Log;

import com.loscache.firebirdone.data.DataReaderDbContext;
import com.loscache.firebirdone.data.MeasurementModel;
import com.loscache.firebirdone.gui.HistoryRowAdapter;

import java.util.ArrayList;

/**
 * Created by ClaudioSaccella on 7/11/2017.
 */

public class HistoryRefresh extends AsyncTask<Integer, MeasurementModel, Integer> {


    private DataReaderDbContext dbContext;

    // Acá reflejaremos los datos
    private HistoryRowAdapter historyRowAdapter;

    public HistoryRefresh(HistoryRowAdapter historyRowAdapter, DataReaderDbContext dbContext){
        super();
        this.historyRowAdapter = historyRowAdapter;
        this.dbContext = dbContext;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        try {
            while(!isCancelled()){

                Log.i("REQUEST", "HistoryRefresh ");

                ArrayList<MeasurementModel> measurementHistoryModels = dbContext.getAllHistories();
                // Publish
                for (MeasurementModel m : measurementHistoryModels) {
                    publishProgress(m);
                }
                Thread.sleep(10000);
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

        // Show only 3 items
        for (MeasurementModel m : values) {
            if(historyRowAdapter.getCount() >= 3){
                historyRowAdapter.removeLastItem();
            }
            historyRowAdapter.addItem(m);
        }

        // Show in list view
        historyRowAdapter.notifyDataSetChanged();

    }
}