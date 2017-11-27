package com.loscache.firebirdone.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loscache.firebirdone.R;
import com.loscache.firebirdone.data.MeasurementModel;

import java.util.ArrayList;

/**
 * Created by cdsac on 23/10/2017.
 */

public class HistoryRowAdapter  extends BaseAdapter {

    Context context;
    ArrayList<MeasurementModel> data;
    private static LayoutInflater inflater = null;

    public HistoryRowAdapter(Context context, ArrayList<MeasurementModel> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.history_row, null);
        MeasurementModel hrObject = data.get(position);
        ((TextView) vi.findViewById(R.id.txt_time)).setText(hrObject.getDate());
        ((TextView) vi.findViewById(R.id.txt_temperature_value)).setText(hrObject.getTemperature());
        ((TextView) vi.findViewById(R.id.txt_smoke_value)).setText(hrObject.getSmoke());
        ((TextView) vi.findViewById(R.id.txt_flame_value)).setText(hrObject.getFlame());
        ((TextView) vi.findViewById(R.id.txt_food_value)).setText(hrObject.getFood());
        ((TextView) vi.findViewById(R.id.txt_water_value)).setText(hrObject.getWater());
        return vi;
    }

    public void addItem(MeasurementModel item){
        data.add(0, item);
    }

    public void removeLastItem(){
        data.remove(data.size() - 1);
    }

    public void clear(){
        data.clear();
    }
}