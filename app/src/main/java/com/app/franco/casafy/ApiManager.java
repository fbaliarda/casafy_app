package com.app.franco.casafy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class ApiManager {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private static final String ROOMS = "rooms/";
    private static final String DEVICES = "devices/";
    private static final String DEVICE_TYPES = "devicetypes/";
    private static final String ROUTINES = "routines/";
    private static final int TIMEOUT = 5000;
    private static final Set<Room> roomsCache = new HashSet<>();
    private static final Set<Device> devicesCache = new HashSet<>();
    private static final Set<Routine> routinesCache = new HashSet<>();

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
    private static boolean getResult(String resultJSON){
        try {
            return (boolean)new JSONObject(resultJSON).get("result");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
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

    public static void updateCache() throws IOException {
        getRooms();
        getDevices();
        getRoutines();
    }
    public static Set<Room> getRoomsCache() {
        return roomsCache;
    }
    public static Set<Device> getDevicesCache() {
        return devicesCache;
    }
    public static Set<Routine> getRoutinesCache() {
        return routinesCache;
    }
    public static List<Room> getRooms() throws IOException {
        String roomsJSON = requestURL(BASE_URL + ROOMS,"GET",null);
        String listJSON = getJSONList(roomsJSON,"rooms");
        if(listJSON == null)
            return null;
        listJSON = setMetaJSON(listJSON);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Room>>() {}.getType();
        roomsCache.addAll((List<Room>)gson.fromJson(listJSON,listType));
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
        devicesCache.addAll((List<Device>)gson.fromJson(listJSON,listType));
        return gson.fromJson(listJSON, listType);
    }
    public static Device getDevice(String deviceId) throws IOException {
        String deviceJSON;
        deviceJSON = requestURL(BASE_URL + DEVICES + deviceId, "GET", null);
        String device;
        try {
            JSONObject jsonObj = new JSONObject(deviceJSON);
            device = jsonObj.getString("device");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        device = setMetaJSON(device);
        Gson gson = new Gson();
        return gson.fromJson(device, Device.class);
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
        routinesCache.addAll((List<Routine>)gson.fromJson(listJSON,listType));
        return gson.fromJson(listJSON, listType);
    }

    public static JSONArray getActions(String typeId) throws IOException {
        String actionsJSON;
        actionsJSON = requestURL(BASE_URL + DEVICE_TYPES + typeId,"GET",null);
        JSONObject jsonObj;
        JSONArray jsonArray;
        try {
            jsonObj = new JSONObject(actionsJSON);
            jsonObj = jsonObj.getJSONObject("device");
            jsonArray = jsonObj.getJSONArray("actions");
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void putAction(String deviceId, Action action) throws IOException {
        Gson gson = new Gson();
        List<String> params = action.getParams();
        String paramsJSON = gson.toJson(params);
        String result = requestURL(BASE_URL + DEVICES + deviceId + "/" + action.getActionName(),"PUT",paramsJSON);
        try {
            JSONObject resultJSON = new JSONObject(result);
            if(resultJSON.has("result") && resultJSON.get("result") == null)
                throw new IOException();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static JSONObject getState(String deviceId) throws IOException {
        String resultJSON;
        resultJSON = requestURL(BASE_URL + DEVICES + deviceId + "/getState","PUT",null);
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(resultJSON);
            jsonObj = jsonObj.getJSONObject("result");
            return jsonObj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Device createDevice(String name,String roomId,DeviceType type) throws IOException {
        Gson gson = new Gson();
        Device deviceInfo = new Device(name,type);
        try {
            //Crea el JSON para enviar a la api.
            JSONObject deviceJSON = new JSONObject(gson.toJson(deviceInfo));
            deviceJSON.remove("id");
            deviceJSON.put("meta",deviceInfo.getMeta().toString());
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
                devicesCache.add(createdDevice);
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
    public static Room createRoom(String name) throws IOException {
        Gson gson = new Gson();
        Room roomInfo = new Room(name);
        try {
            //Crea el JSON para enviar a la api.
            JSONObject roomJSON = new JSONObject(gson.toJson(roomInfo));
            roomJSON.remove("id");
            roomJSON.put("meta",roomInfo.getMeta().toString());
            String result = requestURL(BASE_URL + ROOMS,"POST",roomJSON.toString());
            JSONObject resultJSON = new JSONObject(result);
            if(resultJSON.has("error"))
                return null;
            else {
                //Crea el cuarto con el JSON que envia de respuesta en la api.
                Type roomType = new TypeToken<Room>() {}.getType();
                String jsonRoom = resultJSON.get("room").toString();
                jsonRoom = setMetaJSON(jsonRoom);
                Room createdRoom = gson.fromJson(jsonRoom,roomType);
                roomsCache.add(createdRoom);
                return createdRoom;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Routine createRoutine(String name, List<Action> actions) throws IOException {
        Gson gson = new Gson();
        Routine routineInfo = new Routine(name,actions);
        try {
            //Crea el JSON para enviar a la api.
            JSONObject routineJSON = new JSONObject(gson.toJson(routineInfo));
            routineJSON.remove("id");
            //Agrega el meta vacio a la rutina y a las acciones.
            routineJSON.put("meta","{}");
            JSONArray actionsJSON = routineJSON.getJSONArray("actions");
            for(int i=0 ; i < actionsJSON.length(); i++){
                JSONObject action = actionsJSON.getJSONObject(i);
                action.put("meta","{}");
            }
            //Almacena el arreglo de acciones como un string para poder enviar a la api.
            routineJSON.put("actions",actionsJSON.toString());
            String result = requestURL(BASE_URL + ROUTINES,"POST",routineJSON.toString());
            JSONObject resultJSON = new JSONObject(result);
            if(resultJSON.has("error"))
                return null;
            else {
                //Crea el dispositivo con el JSON que envia de respuesta en la api.
                Type routineType = new TypeToken<Routine>() {}.getType();
                String jsonRoutine = resultJSON.get("routine").toString();
                jsonRoutine = setMetaJSON(jsonRoutine);
                Routine createdRoutine = gson.fromJson(jsonRoutine,routineType);
                routinesCache.add(createdRoutine);
                return createdRoutine;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean deleteDevice(String deviceId) throws IOException {
        String response = requestURL(BASE_URL + DEVICES + deviceId,"DELETE",null);
        if(getResult(response)){
            Device deletedDevice = null;
            for(Device device : devicesCache)
                if(device.getId().equals(deviceId))
                    deletedDevice = device;
            devicesCache.remove(deletedDevice);
        }
        return getResult(response);
    }
    public static boolean deleteRoom(String roomId) throws IOException {
        String response = requestURL(BASE_URL + ROOMS + roomId,"DELETE",null);
        if(getResult(response)){
            Room deletedRoom = null;
            for(Room room : roomsCache)
                if(room.getId().equals(roomId))
                    deletedRoom = room;
            roomsCache.remove(deletedRoom);
        }
        return getResult(response);
    }
    public static boolean deleteRoutine(String routineId) throws IOException {
        String response = requestURL(BASE_URL + ROUTINES + routineId,"DELETE",null);
        if(getResult(response)){
            Routine deletedRoutine = null;
            for(Routine routine : routinesCache)
                if(routine.getId().equals(routineId))
                    deletedRoutine = routine;
            routinesCache.remove(deletedRoutine);
        }
        return getResult(response);
    }
}
