package com.example.parkingreport.data.local.repository;

import android.content.Context;

import com.example.parkingreport.data.local.AppDatabase;
import com.example.parkingreport.data.local.dao.UserDao;
import com.example.parkingreport.data.local.entities.User;

import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private UserDao userDao;

    private LiveData<List<User>> allUserLive;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        userDao = db.getUserDao();
        allUserLive = userDao.getAllUsersLive();
    }

    public LiveData<List<User>> getAllUserLive() {
        return allUserLive;
    }

    public void clearUser(){
        userDao.clearUser();
    }

    public void insertUser(User user) {
        if(checkUserExists(user.getName(), user.getEmail())){
            return;
        }else{
            userDao.insertUser(user);
        }
    }

    public boolean checkUserExists(String username, String email) {
        return userDao.checkUserExists(username, email) > 0;
    }
}
