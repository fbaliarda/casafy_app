package com.app.franco.casafy.devices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.app.franco.casafy.DeviceType;
import com.app.franco.casafy.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OvenView extends DeviceView {
    private View onOffSwitch;
    private Map<String, View> spinnersMap = new HashMap<>();
    private Map<String, SeekbarView> seekbarsMap = new HashMap<>();

    public OvenView(String deviceId, Context context, LayoutInflater layoutInflater) throws IOException {
        super(deviceId);

        onOffSwitch = layoutInflater.inflate(R.layout.content_switch, null);
        TextView leftText = onOffSwitch.findViewById(R.id.leftTextSwitch);
        leftText.setText(R.string.off);
        TextView rightText = onOffSwitch.findViewById(R.id.rightTextSwitch);
        rightText.setText(R.string.on);

        loadSettings(context, layoutInflater);
    }

    public void addViews(ViewGroup viewGroup) {
        viewGroup.addView(onOffSwitch);

        for (SeekbarView seekbarView: seekbarsMap.values()) {
            viewGroup.addView(seekbarView.getView());
        }

        for (View view: spinnersMap.values()) {
            viewGroup.addView(view);
        }

        loadCurrentSettings();
    }

    private void loadSettings(Context context, LayoutInflater layoutInflater) {
        try {
            loadSpinners(DeviceType.OVEN.getTypeId(), spinnersMap, context, layoutInflater);
            loadSeekbars(DeviceType.OVEN.getTypeId(), seekbarsMap, context, layoutInflater);
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

            loadCurrentSpinners(spinnersMap);
            loadCurrentSeekbars(seekbarsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
