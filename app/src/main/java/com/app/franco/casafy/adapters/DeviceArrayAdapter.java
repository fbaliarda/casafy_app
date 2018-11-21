package com.app.franco.casafy.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.franco.casafy.Device;
import com.app.franco.casafy.DeviceSettingsActivity;
import com.app.franco.casafy.R;
import java.util.List;

public class DeviceArrayAdapter extends ArrayAdapter<Device> {

    public static final String DEVICE_VALUE = "device";
    public static final String DEVICE_TYPE_VALUE = "deviceType";
    public static final String DEVICE_NAME_VALUE = "deviceName";

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private Switch onSwitch;
        private ImageView editDevice;
    }
    public DeviceArrayAdapter(Activity context, List<Device> devices){
        super(context,R.layout.device_view_item,devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_view_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.device_icon);
            holder.name = (TextView) convertView.findViewById(R.id.device_name);
            holder.onSwitch = (Switch)convertView.findViewById(R.id.on_switch);
            holder.editDevice = (ImageView)convertView.findViewById(R.id.edit_device);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final Device device = getItem(position);
        holder.image.setImageResource(device.getIcon());
        holder.name.setText(device.getName());
        holder.editDevice.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),DeviceSettingsActivity.class);
                intent.putExtra(DEVICE_VALUE,device.getId());
                intent.putExtra(DEVICE_TYPE_VALUE,device.getType().getTypeId());
                intent.putExtra(DEVICE_NAME_VALUE,device.getName());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
