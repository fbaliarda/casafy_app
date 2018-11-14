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
import com.app.franco.casafy.Routine;

import java.util.List;

public class RoutineArrayAdapter extends ArrayAdapter<Routine> {

    public RoutineArrayAdapter(Activity context, List<Routine> routines){
        super(context,R.layout.routine_view_item,routines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView routineName = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.routine_view_item, parent, false);
            routineName = (TextView) convertView.findViewById(R.id.routine_name);
            convertView.setTag(routineName);
        } else
            routineName = (TextView) convertView.getTag();

        Routine routine = getItem(position);
        routineName.setText(routine.getName());

        return convertView;
    }
}
