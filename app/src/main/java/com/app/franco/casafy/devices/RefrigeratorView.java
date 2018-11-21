package com.app.franco.casafy.devices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.app.franco.casafy.DeviceType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RefrigeratorView extends DeviceView {
    private Map<String, SeekbarView> seekbarsMap = new HashMap<>();

    public RefrigeratorView(String deviceId, Context context, LayoutInflater layoutInflater) throws IOException {
        super(deviceId);

        loadSettings(context, layoutInflater);
    }

    public void addViews(ViewGroup viewGroup) {
        for (SeekbarView seekbarView: seekbarsMap.values()) {
            viewGroup.addView(seekbarView.getView());
        }

        loadCurrentSettings();
    }

    private void loadSettings(Context context, LayoutInflater layoutInflater) {
        try {
            loadSeekbars(DeviceType.REFRIGERATOR.getTypeId(), seekbarsMap, context, layoutInflater);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadCurrentSettings() {
        try {
            loadCurrentSeekbars(seekbarsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void saveCurrentSettings() throws IOException {
        saveCurrentSeekbars(seekbarsMap);
    }
}
