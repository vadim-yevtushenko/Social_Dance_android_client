package com.example.socialdance.model;

import com.example.socialdance.model.enums.Dances;

import java.util.List;
import java.util.Set;

public abstract class AbstractBaseEntity {

    private Integer id;
    private String image;
    private String name;
    private String description;
    private EntityInfo entityInfo;
    private AverageRating rating;
    private List<Dances> dances;

    public AbstractBaseEntity() {
    }

    public AbstractBaseEntity(String image, String name, String description, EntityInfo entityInfo, AverageRating rating, List<Dances> dances) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.entityInfo = entityInfo;
        this.rating = rating;
        this.dances = dances;
    }

    public AbstractBaseEntity(Integer id, String image, String name, String description, EntityInfo entityInfo, AverageRating rating, List<Dances> dances) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.entityInfo = entityInfo;
        this.rating = rating;
        this.dances = dances;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        if (name == null){
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AverageRating getRating() {
        return rating;
    }

    public void setRating(AverageRating rating) {
        this.rating = rating;
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public void setEntityInfo(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public List<Dances> getDances() {
        return dances;
    }

    public void setDances(List<Dances> dances) {
        this.dances = dances;
    }

    @Override
    public String toString() {
        return "AbstractBaseEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", entityInfo=" + entityInfo +
                ", rating='" + rating + '\'' +
                ", dances=" + dances +
                '}';
    }
}
