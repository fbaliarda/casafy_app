package com.app.franco.casafy;

import java.util.List;

public class Routine {

    private String name;
    private String id;
    private List<Action> actions;

    public Routine(String name, String id, List<Action> actions){
        this.name = name;
        this.id = id;
        this.actions = actions;
    }
    public Routine(String name, List<Action> actions){
        this.name = name;
        this.id = null;
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "name: " + name + ", id: " + id + ", actions: " + actions;
    }
}
