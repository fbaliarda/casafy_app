package com.app.franco.casafy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ApiManager {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private static final String ROOMS = "rooms/";
    private static final String DEVICES = "devices/";
    private static final String ROUTINES = "routines/";
    private static final int TIMEOUT = 5000;

    private static String requestURL(String urlString, String requestMethod, String body) throws IOException{
        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestMethod);
        urlConnection.setReadTimeout(TIMEOUT);
        urlConnection.setConnectTimeout(TIMEOUT);
        if(requestMethod == "POST" || requestMethod == "PUT" ) {
            urlConnection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();
        }
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        String resultJSON = readStream(in);
        if (urlConnection != null)
            urlConnection.disconnect();

        return resultJSON;
    }
    private static String readStream(InputStream inputStream) throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int i = inputStream.read();
        while (i != -1) {
            outputStream.write(i);
            i = inputStream.read();
        }
        String result = outputStream.toString();
        outputStream.close();
        return result;
    }

    private static String setMetaJSON(String json){
        /* Elimina la barra para escapar caracteres y las comillas que aparecen antes y despues
         * de las llaves del atributo "meta" para que quede como un objeto JSON. */
        json = json.replace("\\","");
        json = json.replace("\"{","{");
        return json.replace("}\"","}");
    }
    public static List<Room> getRooms() throws IOException {
        String roomsJSON = requestURL(BASE_URL + ROOMS,"GET",null);
        String listJSON = null;
        try {
            JSONObject jsonObj = new JSONObject(roomsJSON);
            listJSON = jsonObj.getString("rooms");
            listJSON = setMetaJSON(listJSON);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Room>>() {}.getType();
        return gson.fromJson(listJSON, listType);
    }
    public static List<Device> getDevices() throws IOException {
        String devicesJSON = requestURL(BASE_URL + DEVICES,"GET",null);
        String listJSON = null;
        try {
            JSONObject jsonObj = new JSONObject(devicesJSON);
            listJSON = jsonObj.getString("devices");
            listJSON = setMetaJSON(listJSON);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
        return gson.fromJson(listJSON, listType);
    }
    public static List<Routine> getRoutines() throws IOException {
        String routinesJSON = requestURL(BASE_URL + ROUTINES,"GET",null);
        String listJSON = null;
        try {
            JSONObject jsonObj = new JSONObject(routinesJSON);
            listJSON = jsonObj.getString("routines");
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Routine>>() {}.getType();
        return gson.fromJson(listJSON, listType);
    }

}
