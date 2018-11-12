package com.app.franco.casafy;

import com.google.gson.Gson;
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
        urlConnection.setRequestProperty("content-type", "application/json");
        urlConnection.setRequestProperty("charset", "utf-8");
        if(requestMethod == "POST" || requestMethod == "PUT") {
            urlConnection.setDoOutput(true);
            if(body != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();
            }
        }
        int responseCode = urlConnection.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_ACCEPTED && responseCode != HttpURLConnection.HTTP_OK)
            throw new IOException();
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

    private static String getJSONList(String json, String listName){
        try {
            JSONObject jsonObj = new JSONObject(json);
            return jsonObj.getString(listName);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
        String listJSON = getJSONList(roomsJSON,"rooms");
        if(listJSON == null)
            return null;
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Room>>() {}.getType();
        return gson.fromJson(listJSON, listType);
    }
    public static List<Device> getDevices(String roomId) throws IOException {
        String devicesJSON;
        if(roomId == null)
            devicesJSON = requestURL(BASE_URL + DEVICES,"GET",null);
        else
            devicesJSON = requestURL(BASE_URL + ROOMS + roomId + "/" + DEVICES,"GET",null);

        String listJSON = getJSONList(devicesJSON,"devices");
        if(listJSON == null)
            return null;
        listJSON = setMetaJSON(listJSON);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
        return gson.fromJson(listJSON, listType);
    }
    public static List<Device> getDevices() throws IOException {
        return getDevices(null);
    }
    public static List<Routine> getRoutines() throws IOException {
        String routinesJSON = requestURL(BASE_URL + ROUTINES,"GET",null);
        String listJSON = getJSONList(routinesJSON,"routines");
        if(listJSON == null)
            return null;
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Routine>>() {}.getType();
        return gson.fromJson(listJSON, listType);
    }

    public static Device createDevice(String name,String roomId,DeviceType type) throws IOException {
        Gson gson = new Gson();
        Device newDevice = new Device(name,type);
        try {
            //Crea el JSON para enviar a la api.
            JSONObject deviceJSON = new JSONObject(gson.toJson(newDevice));
            deviceJSON.remove("id");
            deviceJSON.put("meta",newDevice.getMeta().toString());
            String result = requestURL(BASE_URL + DEVICES,"POST",deviceJSON.toString());
            JSONObject resultJSON = new JSONObject(result);
            if(resultJSON.has("error"))
                return null;
            else {
                //Crea el dispositivo con el JSON que envia de respuesta en la api.
                Type deviceType = new TypeToken<Device>() {}.getType();
                String jsonDevice = resultJSON.get("device").toString();
                jsonDevice = setMetaJSON(jsonDevice);
                Device createdDevice = gson.fromJson(jsonDevice,deviceType);
                //Pasa el dispositivo al cuarto pasado como parametro.
                String url = BASE_URL + DEVICES + createdDevice.getId() + "/" + ROOMS + roomId;
                result = requestURL(url,"POST",null);
                resultJSON = new JSONObject(result);
                if((boolean)resultJSON.get("result"))
                    return createdDevice;
                else {
                    //Si no se pudo asignar al cuarto se elimina el dispositivo.
                    deleteDevice(createdDevice.getId());
                    return null;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean deleteDevice(String deviceId) throws IOException {
        String response = requestURL(BASE_URL + DEVICES + deviceId,"DELETE",null);
        try {
            JSONObject responseJSON = new JSONObject(response);
            return (boolean)responseJSON.get("result");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
