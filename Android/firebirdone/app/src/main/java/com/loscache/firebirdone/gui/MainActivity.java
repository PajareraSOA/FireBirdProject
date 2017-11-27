package com.loscache.firebirdone.gui;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loscache.firebirdone.R;
import com.loscache.firebirdone.data.MeasurementModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Request Codes
    private static int REQUEST_ENABLE_BT = 1000;

    // Progress Dialog
    private ProgressDialog progressDialog;

    // List view
    private ListView listview;

    // Bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crear el progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando Dispositivos.");

        // List View
        listview = (ListView) findViewById(R.id.devices_listview);
        listview.setAdapter(
                new DevicesRowAdapter(getApplicationContext(),
                        new ArrayList<BluetoothDevice>()));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBluetoothAdapter.cancelDiscovery();

                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                DevicesRowAdapter devicesRowAdapter = (DevicesRowAdapter) listview.getAdapter();
                BluetoothDevice bluetoothDevice = (BluetoothDevice) devicesRowAdapter.getItem(i);
                  /*  bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                    bluetoothSocket.connect();*/


                // Iniciar activity con el bluetooth socket
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("BluetoothDevice", bluetoothDevice);


                Toast.makeText(getApplicationContext(), bluetoothDevice.getName(), Toast.LENGTH_SHORT);
                startActivity(intent);

            }
        });


        // Obtener el adpatador de bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        // Botón Conectarse
        Button btnConecction = (Button) findViewById(R.id.btn_connection);
        btnConecction.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent i = new Intent(MainActivity.this, InfoActivity.class);
                        startActivity(i);*/
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        } else {
                            mBluetoothAdapter.startDiscovery();
                        }
                    }
                }
        );

        // Register los broadcast que escucharemos
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    // Creamos el BroadcastReceiver para el Bluetooth.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            DevicesRowAdapter devicesRowAdapter = (DevicesRowAdapter) listview.getAdapter();
            //Toast.makeText(getApplicationContext(), action, Toast.LENGTH_SHORT).show();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetoothDevices.add((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                //Toast.makeText(getApplicationContext(), "Encontré uno", Toast.LENGTH_SHORT).show();
                devicesRowAdapter.addItem((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                devicesRowAdapter.notifyDataSetChanged();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //bluetoothDevices.clear();
                devicesRowAdapter.clear();
                progressDialog.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressDialog.dismiss();
                //ArrayList<BluetoothDevice> bluetoothPairedDevices = new ArrayList<BluetoothDevice>(mBluetoothAdapter.getBondedDevices());

                // Aca mostrarlos en la lista
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            mBluetoothAdapter.startDiscovery();
        }
    }
}
