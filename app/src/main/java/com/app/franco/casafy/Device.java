package com.app.franco.casafy;

import com.google.gson.annotations.SerializedName;

public class Device {

    private String name;
    private String id;
    @SerializedName("typeId")
    private DeviceType type;
    private Meta meta;

    public Device(String name, String id, DeviceType type, Meta meta){
        this.name = name;
        this.id = id;
        this.type = type;
        this.meta = meta;
    }
    public Device(String name, DeviceType type){
        this.name = name;
        this.id = null;
        this.type = type;
        this.meta = new Meta(false,type.getImage());
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public DeviceType getType() {
        return type;
    }
    public Meta getMeta() {
        return meta;
    }
    @Override
    public String toString() {
        return "name: " + name + ", id: " + id + ", typeId: " + type + ", meta: " + meta;
    }
}
