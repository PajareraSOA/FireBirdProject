package com.loscache.firebirdone.gui;

/**
 * Created by cdsac on 22/10/2017.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loscache.firebirdone.R;
import com.loscache.firebirdone.background.HistoryRefresh;
import com.loscache.firebirdone.background.InfoRefresh;
import com.loscache.firebirdone.data.DataReaderDbContext;

public class InfoFragment extends Fragment {

    // Db Context
    public DataReaderDbContext dbContext;

    // Async Task
    private InfoRefresh infoRefresh;

    // Text views
    private TextView txtTemperature;
    private TextView txtSmoke;
    private TextView txtFlame;
    private TextView txtFood;
    private TextView txtWater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        // get the text views
        txtTemperature = rootView.findViewById(R.id.txtTemperature);
        txtSmoke = rootView.findViewById(R.id.txtSmoke);
        txtFlame = rootView.findViewById(R.id.txtFlame);
        txtFood = rootView.findViewById(R.id.txtFood);
        txtWater = rootView.findViewById(R.id.txtWater);

        runAsyncTask();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(infoRefresh != null && infoRefresh.isCancelled())
            runAsyncTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        // cancel async task
        infoRefresh.cancel(true);
    }

    public void runAsyncTask(){
        // run async task
        infoRefresh = new InfoRefresh(
                txtTemperature,
                txtSmoke,
                txtFlame,
                txtFood,
                txtWater,
                dbContext);
        infoRefresh.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

}
