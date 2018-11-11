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
        return "name: " + name + ", id: " + id + ", meta: " + meta;
    }

    private class Meta {
        private boolean isFavourite;
        private String image;

        public Meta(boolean isFavourite, String image){
            this.isFavourite = isFavourite;
            this.image = image;
        }
    }
}
