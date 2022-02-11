package com.example.getcleaner.objects;

public class RateAndComment {
    private String comment;
    private float rate;

    public RateAndComment(){

    }

    public String getByHow() {
        return byHow;
    }

    public RateAndComment setByHow(String byHow) {
        this.byHow = byHow;
        return this;
    }

    private String byHow;
    public String getComment() {
        return comment;
    }

    public RateAndComment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public float getRate() {
        return rate;
    }

    public RateAndComment setRate(float rate) {
        this.rate = rate;
        return this;
    }

}
