package com.app.franco.casafy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.franco.casafy.adapters.RoomArrayAdapter;
import com.app.franco.casafy.adapters.RoutineArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int selectedItemId = R.id.navigation_routines;
    private TextView title;
    private RoomArrayAdapter roomAdapter;
    private RoutineArrayAdapter routineAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ListView list = (ListView)findViewById(R.id.main_list);
            list.setAdapter(null);
            MainActivity.selectedItemId = item.getItemId();
            switch (MainActivity.selectedItemId) {
                case R.id.navigation_routines:
                    loadRoutines();
                    return true;
                case R.id.navigation_devices:
                    loadRooms();
                    return true;
                default: return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        title = (TextView) findViewById(R.id.title);
        roomAdapter = new RoomArrayAdapter(MainActivity.this,new ArrayList<Room>());
        routineAdapter = new RoutineArrayAdapter(MainActivity.this,new ArrayList<Routine>());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(selectedItemId);
    }

    public void loadRoutines() {
        title.setText(R.string.title_routines);
        new AdapterLoader().execute(routineAdapter);
    }
    public void loadRooms() {
        title.setText(R.string.title_devices);
        new AdapterLoader().execute(roomAdapter);
    }
    private class AdapterLoader extends AsyncTask<ArrayAdapter,Void,ArrayAdapter>{

        @Override
        protected ArrayAdapter doInBackground(ArrayAdapter... adapters) {
            ArrayAdapter adapter = adapters[0];
            adapter.clear();
            List<?> items = null;
            if(adapter instanceof RoomArrayAdapter){
                try {
                    items = ApiManager.getRooms();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(adapter instanceof RoutineArrayAdapter){
                try {
                    items = ApiManager.getRoutines();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
            return adapter;
        }
        @Override
        protected void onPostExecute(ArrayAdapter loadedAdapter) {
            ListView list = (ListView)findViewById(R.id.main_list);
            if(list != null)
                list.setAdapter(loadedAdapter);
        }
    }
}
