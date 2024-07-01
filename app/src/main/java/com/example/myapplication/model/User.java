package com.example.myapplication.model;

public class User {
    private String gmailAddress;
    private String token;

    public User(String gmailAddress, String password) {
        this.gmailAddress = gmailAddress;
        this.token = token;
    }

    public String getGmailAddress() {
        return gmailAddress;
    }

    public String getToken() {
        return token;
    }
}