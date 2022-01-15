package com.example.socialdance.model;

import com.example.socialdance.model.enums.Dances;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class School extends AbstractBaseEntity{

    private int ownerId;
    private List<String> reviews;

    public School() {
    }

    public School(String image, String name, String description, EntityInfo entityInfo, AverageRating rating, List<Dances> dances, int ownerId, List<String> reviews) {
        super(image, name, description, entityInfo, rating, dances);
        this.ownerId = ownerId;
        this.reviews = reviews;
    }

    public School(Integer id, String image, String name, String description, EntityInfo entityInfo, AverageRating rating, List<Dances> dances, int ownerId, List<String> reviews) {
        super(id, image, name, description, entityInfo, rating, dances);
        this.ownerId = ownerId;
        this.reviews = reviews;
    }

    public List<String> getReviews() {
        if (reviews == null){
            return new ArrayList<>();
        }
        return reviews;
    }



    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "School{" +
                "ownerId=" + ownerId +
                ", reviews=" + reviews +
                '}' + super.toString();
    }
}
