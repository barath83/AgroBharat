package com.example.agrobharat;

public class BlogPost {

    public  String description,imageURL,username;

    public BlogPost()
    {}


    public BlogPost(String description, String imageURL, String username) {
        this.description = description;
        this.imageURL = imageURL;
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
