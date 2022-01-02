package com.example.socialdance.model;

import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.model.enums.Role;

import java.util.Date;
import java.util.List;

public class Dancer extends AbstractBaseEntity{

    private String avatar;
    private String surname;
    private String sex;
    private Date birthday;
    private Role role;
    private LoginPassword loginPassword;

    public Dancer() {
        this.role = Role.DANCER;
    }

    public Dancer(String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances, String surname, String sex, Date birthday, Role role) {
        super(name, description, entityInfo, rating, dances);
        this.surname = surname;
        this.sex = sex;
        this.birthday = birthday;
        this.role = role;
    }

    public Dancer(Integer id, String name, String description, EntityInfo entityInfo, String rating, List<Dances> dances, String surname, String sex, Date birthday, Role role) {
        super(id, name, description, entityInfo, rating, dances);
        this.surname = surname;
        this.sex = sex;
        this.birthday = birthday;
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Date getBirthday() {

        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public LoginPassword getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(LoginPassword loginPassword) {
        this.loginPassword = loginPassword;
    }

    @Override
    public String toString() {
        return "Dancer{" +
                "surname='" + surname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthDay=" + birthday +
                ", role=" + role +
                '}' + super.toString();
    }
}
