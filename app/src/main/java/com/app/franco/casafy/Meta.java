package com.app.franco.casafy;

public class Meta {
    private boolean isFavourite;
    private String image;

    public Meta(boolean isFavourite, String image){
        this.isFavourite = isFavourite;
        this.image = image;
    }

    @Override
    public String toString() {
        return "isFavourite: " + isFavourite + " image: " + image;
    }
}
