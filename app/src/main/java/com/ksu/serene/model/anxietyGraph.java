package com.ksu.serene.model;

import android.widget.ImageView;

public class anxietyGraph {

    public anxietyGraph(){}

    public anxietyGraph(int id , String imageUrl, String description){
        this.id = id;
        this.ImageUrl = ImageUrl;
        this.description = description;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    ImageView image;
    int id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    String ImageUrl;


}
