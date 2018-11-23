package com.app.franco.casafy.devices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.app.franco.casafy.DeviceType;
import com.app.franco.casafy.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RefrigeratorView extends DeviceView {
    private Map<String, SeekbarView> seekbarsMap = new HashMap<>();
    private Map<String, View> spinnersMap = new HashMap<>();

    public RefrigeratorView(String deviceId, Context context, LayoutInflater layoutInflater) throws IOException {
        super(deviceId);

        loadSettings(context, layoutInflater);
    }

    public void addViews(ViewGroup viewGroup) {
        for (View view: spinnersMap.values()) {
            viewGroup.addView(view);
        }

        for (SeekbarView seekbarView: seekbarsMap.values()) {
            viewGroup.addView(seekbarView.getView());
        }

        loadCurrentSettings();
    }

    private void loadSettings(Context context, LayoutInflater layoutInflater) {
        try {
            loadSpinners(DeviceType.REFRIGERATOR.getTypeId(), spinnersMap, context, layoutInflater);
            loadSeekbars(DeviceType.REFRIGERATOR.getTypeId(), seekbarsMap, context, layoutInflater);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadCurrentSettings() {
        try {
            loadCurrentSpinners(spinnersMap);
            loadCurrentSeekbars(seekbarsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void saveCurrentSettings() throws IOException {
        saveCurrentSpinners(spinnersMap);

        Spinner spinner = spinnersMap.get("mode").findViewById(R.id.spinner);
        if (((SpinnerItem)spinner.getSelectedItem()).getItemId().equals("default")) {
            saveCurrentSeekbars(seekbarsMap);
        }
    }
}
