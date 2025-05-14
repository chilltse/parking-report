package com.example.parkingreport.data.local.dao;
import com.example.parkingreport.data.local.entities.User;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * @author @u7864325 Weimiao Sun
 * Data Access Object (DAO) interface for managing User entities.
 *
 * Provides a standard set of operations for:
 * - Creating, updating, and deleting user accounts
 * - Querying users by ID or name
 * - Checking for user existence by ID or credentials
 * - Modifying user attributes such as name and password
 * - Observing all users via LiveData for UI updates
 *
 * This interface abstracts the data layer, allowing different implementations
 * (e.g., file-based, in-memory, or database-backed) to handle user persistence.
 */

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
