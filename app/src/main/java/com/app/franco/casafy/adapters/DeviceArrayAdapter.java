package com.app.franco.casafy.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.franco.casafy.Device;
import com.app.franco.casafy.R;
import com.app.franco.casafy.Room;

import java.util.List;

public class DeviceArrayAdapter extends ArrayAdapter<Device> {

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private Switch onSwitch;
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
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Device device = getItem(position);
        holder.image.setImageResource(R.mipmap.ic_launcher);
        holder.name.setText(device.getName());

        return convertView;
    }
}
