package com.example.parkingreport.data.local.dao;
import com.example.parkingreport.data.local.entities.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    Long insertUser(User user);

    @Query("DELETE FROM User")
    void clearUser();

    @Query("SELECT * FROM User ORDER BY ID DESC")
    LiveData<List<User>> getAllUsersLive();

    @Query("SELECT COUNT(*) FROM User WHERE name = :username OR email = :email")
    int checkUserExists(String username, String email);

}
