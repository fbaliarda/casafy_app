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

    public int getIcon() {
        if (type == DeviceType.REFRIGERATOR) {
            return R.drawable.ic_fridge;
        } else if (type == DeviceType.AC) {
            return R.drawable.ic_ac;
        } else if (type == DeviceType.LAMP) {
            return R.drawable.ic_lamp;
        } else if (type == DeviceType.OVEN) {
            return R.drawable.ic_oven;
        } else if (type == DeviceType.DOOR) {
            return R.drawable.ic_door;
        }

        return R.drawable.ic_unknown;
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
    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof Device))
            return false;
        Device other = (Device)obj;
        return this.id.equals(other.id);
    }
}
