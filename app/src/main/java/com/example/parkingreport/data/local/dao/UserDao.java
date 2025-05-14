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
    /**
     * Inserts a new User into the list, updates indexes, mappings, and persists changes.
     * @param user the User object to insert
     */
    void insertUser(User user);

    /**
     * Updates an existing User, synchronizes index structures, and persists changes.
     * @param user the User object to update; must have a valid ID
     */
    void updateUser(User user);

    /**
     * Tree find
     * @param ID the id of User
     * @return The user object corresponding to the id
     */
    User findUser(int ID);

    /**
     * Creates a copy of the given User, preserving all its properties.
     * @param user  the source User to duplicate
     * @return User  a new User instance with identical field values
     */
    User copyUser(User user);

    /**
     * Clears all user data by saving an empty list to the local file.
     */
    void clearUser();

    /**
     * Finds the user ID associated with the given username.
     * @param name  the username to look up
     * @return int  the corresponding user ID, or -1 if no such user exists
     */
    int findIdByName(String name);

    /**
     * Returns the LiveData object containing the list of all Users.
     * @return LiveData<List<User>> that observers can subscribe to for updates
     */
    LiveData<List<User>> getAllUsersLive();

    /**
     * Checks for users matching the specified name or email and returns the count.
     * @param username  the username to check
     * @param email  the email address to check
     * @return int  the number of matching users, or 0 if none found or list is null
     */
    int checkUserExists(String username, String email);

}
