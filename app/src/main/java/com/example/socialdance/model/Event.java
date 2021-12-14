package com.example.socialdance.model;

import com.example.socialdance.model.enums.Dances;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Event extends AbstractBaseEntity{

    private Date dateEvent;
    private Date dateFinishEvent;
    private Date datePublication;
    private int ownerId;

    public Event() {
    }

    public Event(String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances, int ownerId, Date dateEvent, Date dateFinishEvent, Date datePublication) {
        super(name, description, entityInfo, rating, dances);
        this.ownerId = ownerId;
        this.dateEvent = dateEvent;
        this.dateFinishEvent = dateFinishEvent;
        this.datePublication = datePublication;
    }

    public Event(Integer id, String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances, int ownerId, Date dateEvent, Date dateFinishEvent, Date datePublication) {
        super(id, name, description, entityInfo, rating, dances);
        this.ownerId = ownerId;
        this.dateEvent = dateEvent;
        this.dateFinishEvent = dateFinishEvent;
        this.datePublication = datePublication;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    public Date getDateFinishEvent() {
        return dateFinishEvent;
    }

    public void setDateFinishEvent(Date dateFinishEvent) {
        this.dateFinishEvent = dateFinishEvent;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "dateEvent=" + dateEvent +
                ", dateFinishEvent=" + dateFinishEvent +
                ", datePublication=" + datePublication +
                ", ownerId=" + ownerId +
                '}' + super.toString();
    }
}
