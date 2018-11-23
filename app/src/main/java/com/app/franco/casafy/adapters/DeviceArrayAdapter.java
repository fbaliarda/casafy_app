package com.app.franco.casafy.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.franco.casafy.Action;
import com.app.franco.casafy.ApiManager;
import com.app.franco.casafy.Device;
import com.app.franco.casafy.DeviceSettingsActivity;
import com.app.franco.casafy.DeviceType;
import com.app.franco.casafy.MainActivity;
import com.app.franco.casafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceArrayAdapter extends ArrayAdapter<Device> {

    public static final String DEVICE_VALUE = "device";
    public static final String DEVICE_TYPE_VALUE = "deviceType";
    public static final String DEVICE_NAME_VALUE = "deviceName";

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView nameNoStatus;
        private Switch onSwitch;
        private ImageView editDevice;
    }
    public DeviceArrayAdapter(Activity context, List<Device> devices){
        super(context,R.layout.device_view_item,devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_view_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.device_icon);
            holder.name = (TextView) convertView.findViewById(R.id.device_name);
            holder.nameNoStatus = (TextView) convertView.findViewById(R.id.device_nameNoStatus);
            holder.onSwitch = (Switch)convertView.findViewById(R.id.on_switch);
            holder.editDevice = (ImageView)convertView.findViewById(R.id.edit_device);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final Device device = getItem(position);
        holder.image.setImageResource(device.getIcon());
        /*if(!device.getType().isSupported())
            holder.name.setText("Desconocido");*/
        if(!device.getType().isSupported())
            holder.editDevice.setAlpha(65);
        holder.editDevice.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!device.getType().isSupported()) {
                    /*Toast.makeText(MainActivity.this.getApplicationContext(), "Esta dispositivo no está soportado en la aplicación móvil. Controlar desde la interfaz web",
                            Toast.LENGTH_LONG).show();*/
                    return;
                }
                Intent intent = new Intent(getContext(),DeviceSettingsActivity.class);
                intent.putExtra(DEVICE_VALUE,device.getId());
                intent.putExtra(DEVICE_TYPE_VALUE,device.getType().getTypeId());
                intent.putExtra(DEVICE_NAME_VALUE,device.getName());
                getContext().startActivity(intent);
            }
        });

        if(device.getType().hasStatus()) {
            holder.name.setText(device.getName());
            holder.nameNoStatus.setText("");
            holder.onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Action a;
                    if (isChecked)
                        a = new Action("turnOn", device.getId(), new ArrayList<String>());
                    else
                        a = new Action("turnOff", device.getId(), new ArrayList<String>());
                    new ActionPlayer().execute(a);
                }
            });

            Map<String,Switch> loadingValues = new HashMap<>();
            loadingValues.put(device.getId(),holder.onSwitch);
            new SwitchLoader().execute(loadingValues);
        } else {
            holder.onSwitch.setVisibility(View.INVISIBLE);
            holder.name.setText("");
            holder.nameNoStatus.setText(device.getName());
        }

        return convertView;
    }

    private class SwitchLoader extends AsyncTask<Map<String,Switch>,Void,Map<Switch,Boolean>>{

        @Override
        protected Map<Switch,Boolean> doInBackground(Map<String, Switch>... values) {
            String deviceId = null;
            Switch switchBtn = null;
            for(Map.Entry<String,Switch> entry : values[0].entrySet()){
                deviceId = entry.getKey();
                switchBtn = entry.getValue();
            }
            try {
                JSONObject state = ApiManager.getState(deviceId);
                String status = (String) state.get("status");
                Map<Switch,Boolean> returnValues = new HashMap<>();
                if(status.equals("on")){
                    returnValues.put(switchBtn,true);
                }
                else{
                    returnValues.put(switchBtn,false);
                }
                return returnValues;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Map<Switch,Boolean> value){
            if(value == null || value.size() == 0){
                Toast.makeText(getContext(), R.string.connectionFailed, Toast.LENGTH_SHORT).show();
                return;
            }
            Switch switchBtn = null;
            Boolean status = false;
            for(Map.Entry<Switch,Boolean> entry : value.entrySet()){
                switchBtn = entry.getKey();
                status = entry.getValue();
            }
            switchBtn.setChecked(status);
        }
    }

    private class ActionPlayer extends AsyncTask<Action,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Action... actions) {
            try {
                ApiManager.putAction(actions[0].getDeviceId(),actions[0]);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        @Override
        public void onPostExecute(Boolean result){
            if(!result)
                Toast.makeText(getContext(), R.string.connectionFailed, Toast.LENGTH_SHORT).show();
        }
    }

}
