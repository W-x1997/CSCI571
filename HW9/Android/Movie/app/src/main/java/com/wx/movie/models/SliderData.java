package com.wx.movie.models;

public class SliderData {
    // image url is used to
    // store the url of image
    private String imgUrl;
    private String type;
    private String id;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }



    // Constructor method.
    public SliderData(String id, String imgUrl, String type, String title) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.type = type;
        this.title = title;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
