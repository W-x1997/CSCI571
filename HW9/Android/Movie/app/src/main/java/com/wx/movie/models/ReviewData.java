package com.wx.movie.models;

public class ReviewData {
    private String title;
    private String rating;
    private String content;

    public ReviewData(String title, String rating, String content) {
        this.title = title;
        this.rating = rating;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
