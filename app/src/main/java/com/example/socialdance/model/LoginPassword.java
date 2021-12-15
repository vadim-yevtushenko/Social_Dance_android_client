package com.example.socialdance.model;


public class LoginPassword {

    private int Id;

    private String email;
    private String password;

    public LoginPassword() {
    }

    public LoginPassword(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginPassword{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
