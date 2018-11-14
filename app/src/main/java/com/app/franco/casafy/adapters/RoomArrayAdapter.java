package com.app.franco.casafy.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.franco.casafy.R;
import com.app.franco.casafy.Room;

import java.util.List;

public class RoomArrayAdapter extends ArrayAdapter<Room> {

    private class ViewHolder {
        private ImageView image;
        private TextView name;
    }
    public RoomArrayAdapter(Activity context, List<Room> rooms){
        super(context,R.layout.room_view_item,rooms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_view_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.room_name);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Room room = getItem(position);
        holder.image.setImageResource(R.mipmap.ic_launcher);
        holder.name.setText(room.getName());

        return convertView;
    }
}
