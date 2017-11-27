package com.loscache.firebirdone.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loscache.firebirdone.R;

import java.util.ArrayList;

/**
 * Created by ClaudioSaccella on 4/11/2017.
 */

public class GesturesRowAdapter extends BaseAdapter {

    Context context;
    ArrayList<GestureRowObject> data;
    private static LayoutInflater inflater = null;

    public GesturesRowAdapter(Context context, ArrayList<GestureRowObject> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.gestures_row, null);
        GestureRowObject grObject = data.get(position);
        ((TextView) vi.findViewById(R.id.txt_title)).setText(grObject.getTitle());
        ((TextView) vi.findViewById(R.id.txt_description)).setText(grObject.getDescription());
        ((ImageView) vi.findViewById(R.id.img_gesture)).setImageResource(grObject.getImage());
        return vi;
    }
}
