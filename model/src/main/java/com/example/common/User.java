package com.example.common;

public class User {
    private String id;
    private String name;
    private String email;
    private String role;
    private String uid;

    public User() {
    }

    public User(String id, String name, String email, String role, String uid) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getUid() {
        return uid;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
