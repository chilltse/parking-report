package com.example.parkingreport.data.local.dao;
import com.example.parkingreport.data.local.entities.User;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface UserDao {
    void insertUser(User user);
    void updateUser(User user);
    User findUser(int ID);
    User copyUser(User user);
    void clearUser();
    void deleteUser(User user);
    int findIdByName(String name);
    void modifyUserName(int userId, String name);
    void modifyUserPassword(int userId, String password);
    LiveData<List<User>> getAllUsersLive();
    int checkIdExists(int userId);
    int checkUserExists(String username, String email);

}
