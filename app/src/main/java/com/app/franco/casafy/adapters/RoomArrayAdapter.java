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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomArrayAdapter extends ArrayAdapter<Room> {

    public static final String ROOM_VALUE = "room";

    private class ViewHolder {
        private TextView name;
        private Button enterButton;
        private Switch favoriteButton;
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
            holder.name = (TextView) convertView.findViewById(R.id.room_name);
            holder.enterButton = (Button)convertView.findViewById(R.id.enterButton);
            holder.favoriteButton = (Switch)convertView.findViewById(R.id.favoriteButton);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final Room room = getItem(position);
        holder.name.setText(room.getName());
        holder.enterButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                new RoomLoader().execute(room.getName());
            }
        });
        holder.favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<Room,Boolean> values = new HashMap<>();
                values.put(room,isChecked);
                new UpdateFavorite().execute(values);
            }
        });
        if(room.getMeta().isFavorite())
            holder.favoriteButton.setChecked(true);
        else
            holder.favoriteButton.setChecked(false);
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
    private class UpdateFavorite extends AsyncTask<Map<Room,Boolean>,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Map<Room, Boolean>... value) {
            Room room = null;
            Boolean favorite = null;
            for(Map.Entry<Room,Boolean> entry : value[0].entrySet()){
                room = entry.getKey();
                favorite = entry.getValue();
            }
            if(room == null)
                return false;
            room.getMeta().setFavorite(favorite);
            try {
                ApiManager.updateRoom(room);
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
