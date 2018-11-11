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

    private static String readURL(String urlString) throws IOException {
        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(5000);
        urlConnection.setConnectTimeout(5000);
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
        return outputStream.toString();
    }
    public static List<Room> getRooms() throws IOException {
        String roomsJSON = readURL(BASE_URL + ROOMS);
        String listJSON = null;
        try {
            JSONObject jsonObj = new JSONObject(roomsJSON);
            listJSON = jsonObj.getString("rooms");
            /* Elimina la barra para escapar caracteres y las comillas que aparecen antes y despues
             * de las llaves del atributo "meta" para que quede como un objeto JSON. */
            listJSON = listJSON.replace("\\","");
            listJSON = listJSON.replace("\"{","{");
            listJSON = listJSON.replace("}\"","}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Room>>() {}.getType();
        return gson.fromJson(listJSON, listType);
    }

}
