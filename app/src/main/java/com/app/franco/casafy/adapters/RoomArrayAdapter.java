package com.app.franco.casafy.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.franco.casafy.ApiManager;
import com.app.franco.casafy.DeviceSettingsActivity;
import com.app.franco.casafy.R;
import com.app.franco.casafy.Room;
import com.app.franco.casafy.RoomActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoomArrayAdapter extends ArrayAdapter<Room> {

    public static final String ROOM_VALUE = "room";

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private Button enterButton;
        private ImageButton favoriteButton;
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
            holder.image = (ImageView) convertView.findViewById(R.id.room_icon);
            holder.name = (TextView) convertView.findViewById(R.id.room_name);
            holder.enterButton = (Button)convertView.findViewById(R.id.enterButton);
            holder.favoriteButton = (ImageButton)convertView.findViewById(R.id.favoriteButton);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final Room room = getItem(position);
        //holder.image.setImageResource(R.mipmap.ic_launcher);
        holder.name.setText(room.getName());
        holder.enterButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                new RoomLoader().execute(room.getName());
            }
        });
        //holder.favoriteButton
        return convertView;
    }

    private class RoomLoader extends AsyncTask<String,Void,Room> {

        @Override
        protected Room doInBackground(String... name) {
            Collection<Room> rooms = ApiManager.getRoomsCache();
            if(rooms == null){
                try {
                    rooms = ApiManager.getRooms();

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            for(Room room : rooms){
                if(room.getName().equals(name[0]))
                    return room;
            }
            return null;
        }
        @Override
        public void onPostExecute(Room room){
            if(room == null)
                return;
            Intent intent = new Intent(getContext(),RoomActivity.class);
            intent.putExtra(ROOM_VALUE,room.getId());
            getContext().startActivity(intent);
        }
    }
}
