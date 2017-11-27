package com.loscache.firebirdone.gui;

/**
 * Created by cdsac on 22/10/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loscache.firebirdone.R;

import java.util.ArrayList;

public class GesturesFragment extends Fragment {

    ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gestures, container, false);

        listview = (ListView) rootView.findViewById(R.id.gestures_listview);

        ArrayList<GestureRowObject> gesturesRowObject = new ArrayList<>();

        gesturesRowObject.add(
                new GestureRowObject(
                        "Verificación Lumínica",
                        "Mover levemente el dispositivo de manera horizontal para la izquierda y la derecha para comprobar el funcionamiento de la alerta lumínica.",
                        R.drawable.accelerometer));
        gesturesRowObject.add(
                new GestureRowObject(
                        "Verificación Sonora",
                        "Tapar el detector de proximidad para comprobar el funcionamiento de la alerta sonora.",
                        R.drawable.proximity));
        gesturesRowObject.add(
                new GestureRowObject("Verificación de Agua",
                        "Hacer el efecto látigo con el dispositivo para comprobar el funcionamiento de los sapitos expulsadores de agua.",
                        R.drawable.giroscope));

        listview.setAdapter(
                new GesturesRowAdapter(this.getContext(),
                        gesturesRowObject));

        return rootView;
    }
}
