package com.app.franco.casafy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.franco.casafy.adapters.DeviceArrayAdapter;
import com.app.franco.casafy.devices.DeviceView;
import com.app.franco.casafy.devices.DoorView;
import com.app.franco.casafy.devices.AcView;
import com.app.franco.casafy.devices.LampView;
import com.app.franco.casafy.devices.RefrigeratorView;

public class DeviceSettingsActivity extends AppCompatActivity {
    private TextView title;
    private LinearLayout linearLayout;
    private DeviceView device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.linearLayout = findViewById(R.id.device_settings_list);
        this.title = findViewById(R.id.device_name);

        Intent intent = getIntent();
        if(intent != null){
            title.setText(intent.getStringExtra(DeviceArrayAdapter.DEVICE_NAME_VALUE));
            new ActivityLoader().execute(intent.getStringExtra(DeviceArrayAdapter.DEVICE_TYPE_VALUE), intent.getStringExtra(DeviceArrayAdapter.DEVICE_VALUE));
        }
    }

    private class ActivityLoader extends AsyncTask<String,Void,DeviceView> {

        @Override
        protected DeviceView doInBackground(String... deviceStrings) {
            try {
                String deviceTypeId = deviceStrings[0];
                if (deviceTypeId.equals(DeviceType.DOOR.getTypeId())) {
                    device = new DoorView(deviceStrings[1], DeviceSettingsActivity.this, getLayoutInflater());
                } else if (deviceTypeId.equals(DeviceType.AC.getTypeId())) {
                    device = new AcView(deviceStrings[1], DeviceSettingsActivity.this, getLayoutInflater());
                } else if (deviceTypeId.equals(DeviceType.REFRIGERATOR.getTypeId())) {
                    device = new RefrigeratorView(deviceStrings[1], DeviceSettingsActivity.this, getLayoutInflater());
                } else if (deviceTypeId.equals(DeviceType.LAMP.getTypeId())) {
                    device = new LampView(deviceStrings[1], DeviceSettingsActivity.this, getLayoutInflater());
                }

                return device;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(DeviceView device) {
            if(device == null)
                return;

            device.addViews(linearLayout);
        }
    }
}
