package com.example.parkingreport.data.local.entities;

public class User {

    private int ID;
    private String name;

    private String email;

    private String password;

    private int role;

    public static final int USER = 1;
    public static final int ADMIN = 0;

    public User(String name, String email, String password, int role){
        this.name = name;
        this.email = email;
//        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = password;
        this.role = role;
    }

    public User(){

    }

    public int getID() {
        return ID;
    }

    public int getRole() {
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
                '}';
    }

}
