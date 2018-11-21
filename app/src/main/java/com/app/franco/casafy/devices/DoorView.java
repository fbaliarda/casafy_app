package com.app.franco.casafy.devices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.app.franco.casafy.Action;
import com.app.franco.casafy.ApiManager;
import com.app.franco.casafy.R;
import java.io.IOException;

public class DoorView extends DeviceView {
    private View openedSwitch;
    private View lockedSwitch;

    public DoorView(String deviceId, Context context, LayoutInflater layoutInflater) throws IOException {
        super(deviceId);

        openedSwitch = layoutInflater.inflate(R.layout.content_switch, null);
        TextView leftText = openedSwitch.findViewById(R.id.leftTextSwitch);
        leftText.setText(R.string.closed);
        TextView rightText = openedSwitch.findViewById(R.id.rightTextSwitch);
        rightText.setText(R.string.opened);

        lockedSwitch = layoutInflater.inflate(R.layout.content_switch, null);
        leftText = lockedSwitch.findViewById(R.id.leftTextSwitch);
        leftText.setText(R.string.locked);
        rightText = lockedSwitch.findViewById(R.id.rightTextSwitch);
        rightText.setText(R.string.unlocked);
    }

    public void addViews(ViewGroup viewGroup) {
        viewGroup.addView(openedSwitch);
        viewGroup.addView(lockedSwitch);

        loadCurrentSettings();
    }

    private void loadCurrentSettings() {
        try {
            Switch switch1 = openedSwitch.findViewById(R.id.state_switch);
            if (state.getString("status").equals("closed")) {
                switch1.setChecked(false);
            } else {
                switch1.setChecked(true);
            }

            switch1 = lockedSwitch.findViewById(R.id.state_switch);
            if (state.getString("lock").equals("locked")) {
                switch1.setChecked(false);
            } else {
                switch1.setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    public void saveCurrentSettings() throws IOException{
        Switch switch1 = openedSwitch.findViewById(R.id.state_switch);
        if (switch1.isChecked()) {
            ApiManager.putAction(deviceId, new Action("open", deviceId, null));
        } else {
            ApiManager.putAction(deviceId, new Action("close", deviceId, null));
        }

        switch1 = lockedSwitch.findViewById(R.id.state_switch);
        if (switch1.isChecked()) {
            ApiManager.putAction(deviceId, new Action("unlock", deviceId, null));
        } else {
            ApiManager.putAction(deviceId, new Action("lock", deviceId, null));
        }
    }
}
