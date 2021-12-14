package com.example.socialdance.model;


public class Rating {

    private int id;

    private AbstractBaseEntity abstractBaseEntity;

    private int reviewer_id;

    private int rating;

    public Rating() {
    }

    public Rating(AbstractBaseEntity abstractBaseEntity, int reviewer_id, int rating) {
        this.abstractBaseEntity = abstractBaseEntity;
        this.reviewer_id = reviewer_id;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AbstractBaseEntity getAbstractBaseEntity() {
        return abstractBaseEntity;
    }

    public void setAbstractBaseEntity(AbstractBaseEntity abstractBaseEntity) {
        this.abstractBaseEntity = abstractBaseEntity;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(int reviewer) {
        this.reviewer_id = reviewer;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", abstractBaseEntity=" + abstractBaseEntity.getId() +
                ", reviewer=" + reviewer_id +
                ", rating=" + rating +
                '}';
    }
}
