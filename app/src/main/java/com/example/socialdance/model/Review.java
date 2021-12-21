package com.example.socialdance.model;

import java.util.Date;

public class Review {
    private int id;

    private int abstractBaseEntityId;

    private int schoolId;

    private String review;

    private Date dateTime;

    public Review() {
    }

    public Review(int abstractBaseEntityId, int school, String review, Date dateTime) {
        this.abstractBaseEntityId = abstractBaseEntityId;
        this.schoolId = school;
        this.dateTime = dateTime;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAbstractBaseEntityId() {
        return abstractBaseEntityId;
    }

    public void setAbstractBaseEntityId(int abstractBaseEntityId) {
        this.abstractBaseEntityId = abstractBaseEntityId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", abstractBaseEntityId=" + abstractBaseEntityId +
                ", schoolId=" + schoolId +
                ", review='" + review + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
