package com.app.franco.casafy.devices;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.app.franco.casafy.Action;
import com.app.franco.casafy.ApiManager;
import com.app.franco.casafy.DeviceType;
import com.app.franco.casafy.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LampView extends DeviceView {
    private View onOffSwitch;
    private Map<String, SeekbarView> seekbarsMap = new HashMap<>();
    private ColorPickerView colorPicker;

    public LampView(String deviceId, Context context, LayoutInflater layoutInflater) throws IOException {
        super(deviceId);

        onOffSwitch = layoutInflater.inflate(R.layout.content_switch, null);
        TextView leftText = onOffSwitch.findViewById(R.id.leftTextSwitch);
        leftText.setText(R.string.off);
        TextView rightText = onOffSwitch.findViewById(R.id.rightTextSwitch);
        rightText.setText(R.string.on);

        colorPicker = ColorPickerView.defaultColorPicker;

        loadSettings(context, layoutInflater);
    }

    public void addViews(ViewGroup viewGroup) {
        viewGroup.addView(onOffSwitch);

        for (SeekbarView seekbarView: seekbarsMap.values()) {
            viewGroup.addView(seekbarView.getView());
        }

        viewGroup.addView(colorPicker.getView());

        loadCurrentSettings();
    }

    private void loadSettings(Context context, LayoutInflater layoutInflater) {
        try {
            loadSeekbars(DeviceType.LAMP.getTypeId(), seekbarsMap, context, layoutInflater);
            colorPicker.setColor(Color.parseColor("#" + state.getString("color")));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadCurrentSettings() {
        try {
            Switch switch1 = onOffSwitch.findViewById(R.id.state_switch);
            if (state.getString("status").equals("off")) {
                switch1.setChecked(false);
            } else {
                switch1.setChecked(true);
            }

            loadCurrentSeekbars(seekbarsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void saveCurrentSettings() throws IOException{
        Switch switch1 = onOffSwitch.findViewById(R.id.state_switch);
        if (switch1.isChecked()) {
            ApiManager.putAction(deviceId, new Action("turnOn", deviceId, null));
        } else {
            ApiManager.putAction(deviceId, new Action("turnOff", deviceId, null));
        }

        saveCurrentSeekbars(seekbarsMap);

        List<String> params = new ArrayList<String>();
        params.add(colorPicker.getColor().substring(2, 8));
        ApiManager.putAction(deviceId, new Action("setColor", deviceId, params));
    }
}
