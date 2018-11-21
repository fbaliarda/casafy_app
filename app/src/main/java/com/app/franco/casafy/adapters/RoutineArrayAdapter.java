package com.app.franco.casafy.adapters;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.franco.casafy.ApiManager;
import com.app.franco.casafy.R;
import com.app.franco.casafy.Routine;

import java.io.IOException;
import java.util.List;

public class RoutineArrayAdapter extends ArrayAdapter<Routine> {

    private class ViewHolder {
        TextView routineName;
        ImageButton playButton;
    }

    public RoutineArrayAdapter(Activity context, List<Routine> routines){
        super(context,R.layout.routine_view_item,routines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.routine_view_item, parent, false);
            holder.routineName = (TextView) convertView.findViewById(R.id.routine_name);
            holder.playButton = (ImageButton)convertView.findViewById(R.id.playButton);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final Routine routine = getItem(position);
        holder.routineName.setText(routine.getName());
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RoutinePlayer().execute(routine.getId());
            }
        });
        return convertView;
    }

    private class RoutinePlayer extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... routineId) {
            try{
                ApiManager.executeRoutine(routineId[0]);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        @Override
        protected void onProgressUpdate(Void... v) {
            Toast.makeText(getContext(),R.string.routinePlaying,Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onPostExecute(Boolean result){
            if(result)
                Toast.makeText(getContext(),R.string.routineFinished,Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(),R.string.routineFailed,Toast.LENGTH_SHORT).show();
        }
    }
}
