package com.example.parkingreport.data.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "role")
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


}
