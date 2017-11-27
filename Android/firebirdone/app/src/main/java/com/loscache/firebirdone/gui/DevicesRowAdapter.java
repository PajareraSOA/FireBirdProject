package com.loscache.firebirdone.gui;

import android.bluetooth.BluetoothDevice;
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
 * Created by cdsac on 17/11/2017.
 */

public class DevicesRowAdapter  extends BaseAdapter {

    Context context;
    ArrayList<BluetoothDevice> data;
    private static LayoutInflater inflater = null;

    public DevicesRowAdapter(Context context, ArrayList<BluetoothDevice> data) {
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
            vi = inflater.inflate(R.layout.devices_row, null);
        BluetoothDevice bluetoothDevice = data.get(position);
        ((TextView) vi.findViewById(R.id.txt_bt_name)).setText(bluetoothDevice.getName());
        ((TextView) vi.findViewById(R.id.txt_bt_address)).setText(bluetoothDevice.getAddress());
        return vi;
    }

    public void addItem(BluetoothDevice item){
        data.add(0, item);
    }

    public void clear(){
        data.clear();
    }
}