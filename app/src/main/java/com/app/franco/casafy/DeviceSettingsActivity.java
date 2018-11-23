package com.app.franco.casafy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.franco.casafy.adapters.DeviceArrayAdapter;
import com.app.franco.casafy.devices.ColorPickerView;
import com.app.franco.casafy.devices.DeviceView;
import com.app.franco.casafy.devices.DoorView;
import com.app.franco.casafy.devices.AcView;
import com.app.franco.casafy.devices.LampView;
import com.app.franco.casafy.devices.OvenView;
import com.app.franco.casafy.devices.RefrigeratorView;

public class DeviceSettingsActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private DeviceView device;
    private Button buttonSave;

    View.OnClickListener saveButtonClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new SettingsSaver().execute();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);

        new ColorPickerView(DeviceSettingsActivity.this, getLayoutInflater());
        this.linearLayout = findViewById(R.id.device_settings_list);
        this.buttonSave = findViewById(R.id.btnSave);
        ColorPickerView.defaultColorPicker = new ColorPickerView(DeviceSettingsActivity.this, getLayoutInflater());

        Intent intent = getIntent();
        if(intent != null){
            getSupportActionBar().setTitle(intent.getStringExtra(DeviceArrayAdapter.DEVICE_NAME_VALUE));
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
                } else if (deviceTypeId.equals(DeviceType.OVEN.getTypeId())) {
                    device = new OvenView(deviceStrings[1], DeviceSettingsActivity.this, getLayoutInflater());
                } else {
                    device = null;
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

            buttonSave.setOnClickListener(saveButtonClickHandler);
            device.addViews(linearLayout);
        }
    }

    private class SettingsSaver extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                device.saveCurrentSettings();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Void elem) {
            finish();
        }
    }
}
