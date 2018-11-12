package com.app.franco.casafy;

public class Device {

    private String name;
    private String id;
    private String typeId;
    private Meta meta;

    public Device(String name, String id, String typeId, Meta meta){
        this.name = name;
        this.id = id;
        this.typeId = typeId;
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "name: " + name + ", id: " + id + ", typeId: " + typeId + ", meta: {" + meta + "}";
    }
}
