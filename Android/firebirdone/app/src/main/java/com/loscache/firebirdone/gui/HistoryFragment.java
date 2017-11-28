package com.loscache.firebirdone.gui;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loscache.firebirdone.R;
import com.loscache.firebirdone.background.HistoryRefresh;
import com.loscache.firebirdone.background.InfoRefresh;
import com.loscache.firebirdone.data.DataReaderDbContext;
import com.loscache.firebirdone.data.MeasurementModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private ListView listview;

    // Db Context
    public DataReaderDbContext dbContext;

    // Async Task
    private HistoryRefresh historyRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listview = rootView.findViewById(R.id.history_listview);

        ArrayList<MeasurementModel> historyRowObjects = new ArrayList<>();

        if(dbContext != null){
           historyRowObjects = dbContext.getAllHistories();
        }


        listview.setAdapter(
                new HistoryRowAdapter(this.getContext(),
                        historyRowObjects));

        // How to add items to the list view
        Log.i("DEBUG", "HistoryCreateFragment");

        runAsyncTask();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // run async task
        if(historyRefresh != null && historyRefresh.isCancelled())
            runAsyncTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        // cancel async task
        historyRefresh.cancel(true);
    }


    public void runAsyncTask(){
        // run async task
        historyRefresh = new HistoryRefresh(
                (HistoryRowAdapter) listview.getAdapter(), dbContext);
        historyRefresh.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }
}
