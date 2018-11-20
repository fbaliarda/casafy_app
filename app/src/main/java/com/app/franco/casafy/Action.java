package com.app.franco.casafy;

import java.util.List;

public class Action {

    private String actionName;
    private String deviceId;
    private List<String> params;

    public Action(String actionName, String deviceId, List<String> params){
        this.actionName = actionName;
        this.deviceId = deviceId;
        this.params = params;
    }

    public List<String> getParams() {
        return params;
    }
    @Override
    public String toString() {
        return "name: " + actionName + ", id: " + deviceId + ", params: " + params;
    }
}
