package com.wx.movie.models;

public class SearchData {
    private String media_type;
    private String id;
    private String title;
    private double rating;
    private String backdrop_path;
    private String year;

    public SearchData(String media_type, String id, String title, double rating, String backdrop_path, String year) {
        this.media_type = media_type;
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.backdrop_path = backdrop_path;
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
