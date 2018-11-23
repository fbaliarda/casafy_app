package com.app.franco.casafy.devices;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.franco.casafy.Action;
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

    public abstract void saveCurrentSettings() throws IOException;

    void loadSpinners(String typeId, Map<String, View> spinnersMap, Context context, LayoutInflater layoutInflater) throws IOException, JSONException {
        JSONArray jsonArray;
        jsonArray = ApiManager.getActions(typeId);
        int arrayLength = jsonArray.length();

        for (int i = 0; i < arrayLength; i++) {
            JSONObject curr = jsonArray.getJSONObject(i);
            JSONArray params = curr.getJSONArray("params");
            if (params.length() > 0 && (curr.getString("name").equals("setMode") ||
                    params.getJSONObject(0).getString("type").equals("string"))) {
                spinnersMap.put(params.getJSONObject(0).getString("name"),
                        loadSpinner(params.getJSONObject(0).getJSONArray("supportedValues"),
                                context, layoutInflater, params.getJSONObject(0).getString("name")));
            }
        }
    }

    private View loadSpinner(JSONArray arrayParams, Context context, LayoutInflater layoutInflater, String name) throws JSONException {
        View spinnerView = layoutInflater.inflate(R.layout.content_spinner, null);
        Spinner spinner = spinnerView.findViewById(R.id.spinner);
        TextView spinnerText = spinnerView.findViewById(R.id.spinnerText);
        spinnerText.setText(byIdName(context, name));

        List<SpinnerItem> listParams = new ArrayList<>();
        int numParams = arrayParams.length();

        for (int j = 0; j < numParams; j++) {
            listParams.add(new SpinnerItem(arrayParams.getString(j), byIdName(context, arrayParams.getString(j))));
        }
        ArrayAdapter<SpinnerItem> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listParams);
        spinner.setAdapter(arrayAdapter);
        return spinnerView;
    }

    class SpinnerItem {
        String itemId;
        String itemText;

        SpinnerItem(String itemId, String itemText) {
            this.itemId = itemId;
            this.itemText = itemText;
        }

        @Override
        public String toString() {
            return itemText;
        }

        String getItemId() {
            return itemId;
        }
    }

    void loadSeekbars(String typeId, Map<String, SeekbarView> seekbarsMap, Context context, LayoutInflater layoutInflater) throws IOException, JSONException {
        JSONArray jsonArray;
        jsonArray = ApiManager.getActions(typeId);
        int arrayLength = jsonArray.length();

        for (int i = 0; i < arrayLength; i++) {
            JSONObject curr = jsonArray.getJSONObject(i);
            JSONArray params = curr.getJSONArray("params");
            if (params.length() > 0 && !curr.getString("name").equals("setMode") &&
                    (params.getJSONObject(0).getString("type").equals("number") ||
                           params.getJSONObject(0).getString("type").equals("integer"))) {
                seekbarsMap.put(curr.getString("name"), loadSeekbar(params.getJSONObject(0).getInt("minValue"),
                        params.getJSONObject(0).getInt("maxValue"), context, layoutInflater, byIdName(context, curr.getString("name"))));
            }
        }
    }

    private SeekbarView loadSeekbar(int minValue, int maxValue, Context context, LayoutInflater layoutInflater, final String text) {
        View seekbarView = layoutInflater.inflate(R.layout.content_seekbar, null);
        return new SeekbarView(seekbarView, minValue, maxValue, text);
    }

    void loadCurrentSpinners(Map<String, View> spinnersMap) throws JSONException{
        Iterator<String> iterator = state.keys();

        while(iterator.hasNext()) {
            String next = iterator.next();
            if (spinnersMap.containsKey(next)) {
                Spinner spinner = spinnersMap.get(next).findViewById(R.id.spinner);

                for (int i = 0; i < spinner.getCount(); i++) {
                    if (((SpinnerItem)spinner.getItemAtPosition(i)).getItemId().equals(state.getString(next))) {
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

    void saveCurrentSpinners(Map<String, View> spinnersMap) throws IOException{
        for (String string: spinnersMap.keySet()) {
            String actionName = "set" + string.substring(0, 1).toUpperCase() + string.substring(1);
            List<String> params = new ArrayList<>();
            Spinner spinner = spinnersMap.get(string).findViewById(R.id.spinner);
            params.add(((SpinnerItem)spinner.getSelectedItem()).getItemId());
            ApiManager.putAction(deviceId, new Action(actionName, deviceId, params));
        }
    }

    void saveCurrentSeekbars(Map<String, SeekbarView> seekbarsMap) throws IOException{
        for (String string: seekbarsMap.keySet()) {
            List<String> params = new ArrayList<>();
            params.add(String.valueOf(seekbarsMap.get(string).getValue()));
            ApiManager.putAction(deviceId, new Action(string, deviceId, params));
        }
    }

    private static String byIdName(Context context, String name) {
        Resources res = context.getResources();
        int resId = res.getIdentifier(name, "string", context.getPackageName());

        if (resId == 0 || String.valueOf(resId).equals(name)) {
            return name;
        } else {
            return res.getString(resId);
        }
    }
}
