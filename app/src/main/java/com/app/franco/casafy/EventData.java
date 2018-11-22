package com.app.franco.casafy;

import java.util.HashMap;
import java.util.Map;

public class EventData {

    private String id;
    private String timestamp;
    private String deviceId;
    private String event;
    private Map<String,String> args;

    public EventData() {
        this.args = new HashMap<>();
    }
    public EventData(EventData original) {
        this.id = new String(original.id);
        this.timestamp = new String(original.timestamp);
        this.deviceId = new String(original.deviceId);
        this.event = new String(original.event);
        this.args = new HashMap<>(original.args);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Map<String,String> getArgs() {
        return args;
    }

    public void addArgs(String name,String value) {
        args.put(name,value);
    }

    @Override
    public String toString(){
        return "id: " + id + ", timestamp: " + timestamp + ", deviceId: " + deviceId
                + ", event: " + event + "\n args: " + args.toString();
    }
}
