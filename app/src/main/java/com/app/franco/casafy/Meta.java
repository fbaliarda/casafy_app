package com.app.franco.casafy;

import java.io.Serializable;

public class Meta implements Serializable {
    private boolean isFavourite;
    private String image;

    public Meta(boolean isFavorite, String image){
        this.isFavourite = isFavorite;
        this.image = image;
    }

    @Override
    public String toString() {
        //String como esta guardado en la api.
        return "{\"isFavorite\": " + isFavourite + ", \"image\": \"" + image + "\"}";
    }
}
