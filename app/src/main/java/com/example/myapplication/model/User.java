package com.example.myapplication.model;

public class User {
    private String  id;
    private String displayName;
    private String loginToken;
    private String image;
    private String email;
    private String password;

    public User() {
    }

    public User(String id, String displayName, String loginToken, String image, String email, String password) {
        this.id = id;
        this.displayName = displayName;
        this.loginToken = loginToken;
        this.image = image;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", loginToken='" + loginToken + '\'' +
                ", image='" + image + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
