package com.app.franco.casafy.devices;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.franco.casafy.ApiManager;
import com.app.franco.casafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class DeviceView {
    String deviceId;
    JSONObject state;

    public DeviceView(String deviceId) throws IOException {
        this.deviceId = deviceId;
        getState();
    }

    public abstract void addViews(ViewGroup viewGroup);

    private void getState() throws IOException{
        state = ApiManager.getState(deviceId);
    }

    void loadSpinners(String typeId, Map<String, View> spinnersMap, Context context, LayoutInflater layoutInflater) throws IOException, JSONException {
        JSONArray jsonArray;
        jsonArray = ApiManager.getActions(typeId);
        int arrayLength = jsonArray.length();

        for (int i = 0; i < arrayLength; i++) {
            if (jsonArray.getJSONObject(i).getJSONArray("params").length() > 0 &&
                    jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getString("type").equals("string")) {
                spinnersMap.put(jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getString("name"),
                        loadSpinner(jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getJSONArray("supportedValues"),
                                context, layoutInflater, jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getString("name")));
            }
        }
    }

    private View loadSpinner(JSONArray arrayParams, Context context, LayoutInflater layoutInflater, String name) throws JSONException {
        View spinnerView = layoutInflater.inflate(R.layout.content_spinner, null);
        Spinner spinner = spinnerView.findViewById(R.id.spinner);
        TextView spinnerText = spinnerView.findViewById(R.id.spinnerText);
        if (spinnerText != null) spinnerText.setText(name);

        List<String> listParams = new ArrayList<>();
        int numParams = arrayParams.length();

        for (int j = 0; j < numParams; j++) {
            listParams.add(arrayParams.getString(j));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listParams);
        spinner.setAdapter(arrayAdapter);
        return spinnerView;
    }

    void loadSeekbars(String typeId, Map<String, SeekbarView> seekbarsMap, Context context, LayoutInflater layoutInflater) throws IOException, JSONException {
        JSONArray jsonArray;
        jsonArray = ApiManager.getActions(typeId);
        int arrayLength = jsonArray.length();

        for (int i = 0; i < arrayLength; i++) {
            if (jsonArray.getJSONObject(i).getJSONArray("params").length() > 0 &&
                    !jsonArray.getJSONObject(i).getString("name").equals("setMode") &&
                    (jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getString("type").equals("number") ||
                            jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getString("type").equals("integer"))) {
                seekbarsMap.put(jsonArray.getJSONObject(i).getString("name"),
                        loadSeekbar(jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getInt("minValue"),
                                jsonArray.getJSONObject(i).getJSONArray("params").getJSONObject(0).getInt("maxValue"),
                                context, layoutInflater, jsonArray.getJSONObject(i).getString("name")));
            }
        }
    }

    private SeekbarView loadSeekbar(int minValue, int maxValue, Context context, LayoutInflater layoutInflater, final String name) {
        View seekbarView = layoutInflater.inflate(R.layout.content_seekbar, null);

        return new SeekbarView(seekbarView, minValue, maxValue, name);
    }

    void loadCurrentSpinners(Map<String, View> spinnersMap) throws JSONException{
        Iterator<String> iterator = state.keys();

        while(iterator.hasNext()) {
            String next = iterator.next();
            if (spinnersMap.containsKey(next)) {
                Spinner spinner = spinnersMap.get(next).findViewById(R.id.spinner);

                for (int i = 0; i < spinner.getCount(); i++) {
                    if (spinner.getItemAtPosition(i).equals(state.getString(next))) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    void loadCurrentSeekbars(Map<String, SeekbarView> seekbarsMap) throws JSONException{
        Iterator<String> iterator = state.keys();

        while(iterator.hasNext()) {
            String next = iterator.next();
            String aux = "set" + next.substring(0, 1).toUpperCase() + next.substring(1);
            if (seekbarsMap.containsKey(aux)) {
                seekbarsMap.get(aux).setValue(state.getInt(next));
            }
        }
    }
}
