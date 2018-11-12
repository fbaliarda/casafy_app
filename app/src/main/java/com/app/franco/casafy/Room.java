package com.app.franco.casafy;

public class Room {

    private String name;
    private String id;
    private Meta meta;

    public Room(String name, String id, Meta meta){
        this.name = name;
        this.id = id;
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "name: " + name + ", id: " + id + ", meta: {" + meta + "}";
    }
}
