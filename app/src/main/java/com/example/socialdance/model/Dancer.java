package com.example.socialdance.model;

import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.model.enums.Role;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dancer extends AbstractBaseEntity{

    private String surname;
    private String sex;
    private Date birthDay;
    private Role role;

    public Dancer() {
    }

    public Dancer(String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances, String surname, String sex, Date birthDay, Role role) {
        super(name, description, entityInfo, rating, dances);
        this.surname = surname;
        this.sex = sex;
        this.birthDay = birthDay;
        this.role = role;
    }

    public Dancer(Integer id, String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances, String surname, String sex, Date birthDay, Role role) {
        super(id, name, description, entityInfo, rating, dances);
        this.surname = surname;
        this.sex = sex;
        this.birthDay = birthDay;
        this.role = role;
    }

    public String getSurname() {
        if (surname == null){
            return "";
        }
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDay() {

        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Dancer{" +
                "surname='" + surname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthDay=" + birthDay +
                ", role=" + role +
                '}' + super.toString();
    }
}
