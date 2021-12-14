package com.example.socialdance.model;

import com.example.socialdance.model.enums.Dances;

import java.util.List;
import java.util.Set;

public abstract class AbstractBaseEntity {

    private Integer id;
//    private boolean avatar;
    private String name;
    private String description;
    private EntityInfo entityInfo;
    private String rating;
    private List<Dances> dances;

    public AbstractBaseEntity() {
    }

    public AbstractBaseEntity(String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances) {
        this.name = name;
        this.description = description;
        this.entityInfo = entityInfo;
        this.rating = rating;
        this.dances = dances;
    }

    public AbstractBaseEntity(Integer id, String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances) {
        this.id = id;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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
