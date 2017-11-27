package com.loscache.firebirdone.gui;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loscache.firebirdone.R;
import com.loscache.firebirdone.background.GesturesListener;
import com.loscache.firebirdone.background.HistorySaver;
import com.loscache.firebirdone.background.MeasurementRequest;
import com.loscache.firebirdone.data.DataReaderDbContext;
import com.loscache.firebirdone.data.DataReaderDbHelper;

import java.io.IOException;
import java.util.UUID;

import static android.hardware.Sensor.TYPE_PROXIMITY;

public class InfoActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SensorManager sensorManager;
    private GesturesListener gesturesListener;

    // Bluetooth
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;

    // Db Context
    private DataReaderDbContext dbContext;

    // Tabs
    private Fragment infoTab;
    private Fragment historyTab;
    private Fragment gesturesTab;

    // Async Task
    private MeasurementRequest measurementRequest;
    private HistorySaver historySaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);




/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Conectarme con el bluetooth

        bluetoothDevice = (BluetoothDevice) getIntent().getExtras().getParcelable("BluetoothDevice");

       /* TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);*/
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
       // gesturesListener = new GesturesListener((sensorManager.getDefaultSensor(TYPE_PROXIMITY)).getMaximumRange(), bluetoothSocket);




    }

    @Override
    public void onResume() {
        super.onResume();

        // on resume de los fragments
        if(infoTab != null)
            infoTab.onResume();
        if(historyTab != null)
            historyTab.onResume();

        // Conectarme al bluetooth
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No pude crear socket", Toast.LENGTH_SHORT).show();
            return;
        }

        // escuchar los sensores
        gesturesListener = new GesturesListener((sensorManager.getDefaultSensor(TYPE_PROXIMITY)).getMaximumRange(), bluetoothSocket);
        sensorManager.registerListener(gesturesListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(gesturesListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gesturesListener, sensorManager.getDefaultSensor(TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

        // abrir la base de datos
        dbContext = new DataReaderDbContext(new DataReaderDbHelper(getApplicationContext()));

        // poner a correr la async task
        measurementRequest = new MeasurementRequest(dbContext, bluetoothSocket);
        measurementRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

        historySaver = new HistorySaver(dbContext);
        historySaver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);



    }

    @Override
    public void onStop() {
        super.onStop();

        // fragments on stops
        if(infoTab != null)
            infoTab.onStop();
        if(historyTab != null)
            historyTab.onStop();

        // Desconectar Bluetooth
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "No pude cerrar socket", Toast.LENGTH_SHORT).show();
            return;
        }

        // dejar de escuchar los sensores
        sensorManager.unregisterListener(gesturesListener);

        // cerrar la base de datos
        dbContext.close();

        // Frenar la task
        measurementRequest.cancel(true);
        historySaver.cancel(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //Bundle dbContextBundle = new Bundle();
            //dbContextBundle.putSerializable("dbcontext", dbContext);
            //dbContextBundle.putString("dbcontext", "hola");
            switch (position){
                case 0:
                    Log.i("TABS", "cree 0");
                    infoTab = new InfoFragment();
                    ((InfoFragment) infoTab).dbContext = dbContext;
                    return infoTab;
                case 1:
                    Log.i("TABS", "cree 1");
                    historyTab = new HistoryFragment();
                    ((HistoryFragment) historyTab).dbContext = dbContext;
                    return historyTab;
                case 2:
                    Log.i("TABS", "cree 2");
                    gesturesTab = new GesturesFragment();
                    return gesturesTab;
                default:
                    infoTab = new InfoFragment();
                    return infoTab;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "INFORMACION";
                case 1:
                    return "HISTORIAL";
                case 2:
                    return "GESTOS";
            }
            return null;
        }
    }
}
