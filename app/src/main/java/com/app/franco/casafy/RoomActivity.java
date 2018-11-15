package com.app.franco.casafy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.franco.casafy.adapters.DeviceArrayAdapter;
import com.app.franco.casafy.adapters.RoomArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RoomActivity extends AppCompatActivity {

    private TextView title;
    private Room room;
    private DeviceArrayAdapter deviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.title = findViewById(R.id.room_name);

        Intent intent = getIntent();
        if(intent != null){
            String roomId = intent.getStringExtra(RoomArrayAdapter.ROOM_VALUE);
            deviceAdapter = new DeviceArrayAdapter(RoomActivity.this,new ArrayList<Device>());
            new ActivityLoader().execute(roomId);
        }

    }

    private class ActivityLoader extends AsyncTask<String,Void,List<Device>> {

        @Override
        protected List<Device> doInBackground(String... roomId) {
            Set<Room> rooms = ApiManager.getRoomsCache();
            for(Room r : rooms){
                if(r.getId().equals(roomId[0]))
                    room = r;
            }
            try {
                return ApiManager.getDevices(roomId[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(List<Device> devices) {
            if(devices == null)
                return;
            deviceAdapter.clear();
            deviceAdapter.addAll(devices);
            ListView list = (ListView)findViewById(R.id.device_list);
            if(list != null)
                list.setAdapter(deviceAdapter);
            title.setText(room.getName());

        }
    }
}
