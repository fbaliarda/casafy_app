package com.app.franco.casafy;

import android.util.Log;

import java.io.Serializable;

public class Meta implements Serializable {
    private boolean isFavorite;
    private String image;

    public Meta(boolean isFavorite, String image){
        this.isFavorite = isFavorite;
        this.image = image;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        //String como esta guardado en la api.
        return "{\"isFavorite\": " + isFavorite + ", \"image\": \"" + image + "\"}";
    }
}
