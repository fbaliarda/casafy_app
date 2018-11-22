package com.app.franco.casafy;

import com.google.gson.annotations.SerializedName;

public enum DeviceType {

    @SerializedName("eu0v2xgprrhhg41g")
    BLIND("eu0v2xgprrhhg41g","devices/blind.jpg", false),

    @SerializedName("go46xmbqeomjrsjr")
    LAMP("go46xmbqeomjrsjr","devices/lamp.jpg", true),

    @SerializedName("im77xxyulpegfmv8")
    OVEN("im77xxyulpegfmv8","devices/oven.jpg", true),

    @SerializedName("li6cbv5sdlatti0j")
    AC("li6cbv5sdlatti0j","devices/airconditioner.jpg", true),

    @SerializedName("lsf78ly0eqrjbz91")
    DOOR("lsf78ly0eqrjbz91","devices/door.jpg", true),

    @SerializedName("mxztsyjzsrq7iaqc")
    ALARM("mxztsyjzsrq7iaqc","devices/alarm.jpg", false),

    @SerializedName("ofglvd9gqX8yfl3l")
    TIMER("ofglvd9gqX8yfl3l",null, false),

    @SerializedName("rnizejqr2di0okho")
    REFRIGERATOR("rnizejqr2di0okho","devices/refrigerator.jpg", true);

    private String typeId;
    private String image;
    private boolean supported;

    private DeviceType(String typeId, String image, boolean supported){
        this.typeId = typeId;
        this.image = image;
        this.supported = supported;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getImage() {
        return image;
    }
    @Override
    public String toString() {
        return typeId;
    }

    public boolean isSupported() {
        return supported;
    }
}
