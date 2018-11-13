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
    public Room(String name){
        this.name = name;
        this.id = null;
        this.meta = new Meta(false,"rooms/room.jpg");
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public Meta getMeta() {
        return meta;
    }
    @Override
    public String toString() {
        return "name: " + name + ", id: " + id + ", meta: {" + meta + "}";
    }
}
