package com.app.franco.casafy;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {

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
        this.meta = new Meta(true,"rooms/room.jpg");
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
    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof Room))
            return false;
        Room other = (Room)obj;
        return this.id.equals(other.id);
    }
}
