package com.example.parkingreport.data.local.entities;

import com.example.parkingreport.data.local.api.HasID;

public class User implements Comparable<User>, HasID {

    private int ID;
    private String name;
    private String email;
    private String password;
    private String profilePicUrl;
    private String role;
    private boolean alive;
    public static final String USER = "User";
    public static final String ADMIN = "Admin";

    public User(String name, String email, String password, String role, boolean alive){
        this.name = name;
        this.email = email;
//        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = password;
        this.role = role;
        this.alive = alive;
    }

    @Override
    public int compareTo(User other) {
        return Integer.compare(this.ID, other.ID);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public User(){

    }

    public int getID() {
        return ID;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    /**
     * @return for debug use, print user info
     */
    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", alive=" + alive +
                '}';
    }

}
