package com.example.parkingreport.data.local.repository;

/**
 * @author @u7864325 Weimiao Sun
 *
 * Repository class that manages User-related operations and logging.
 *
 * Responsibilities:
 * - Provides a singleton interface for interacting with the user data source
 * - Handles user creation, deletion, password modification, and retrieval
 * - Automatically assigns unique user IDs during insertion
 * - Delegates data persistence to {UserDao} (e.g. JsonUserDao)
 * - Integrates with {UserLogRepository} to record user activities
 *
 * This repository acts as a bridge between the UI/ViewModel layer and the data access layer.
 */

import android.content.Context;
import android.util.Log;

import com.example.parkingreport.data.local.dao.JsonUserDao;
import com.example.parkingreport.data.local.dao.UserDao;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.entities.UserLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private static UserRepository Instance;
    private UserDao userDao;
    private LiveData<List<User>> allUserLive;
    private UserLogRepository userLogRepository;

    /**
     * Constructor
     * @param context
     */
    public UserRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "users.json");
        this.userDao = new JsonUserDao(jsonFile);
        this.allUserLive = userDao.getAllUsersLive();
        // get the instance of userLog, when creating userRepository
        this.userLogRepository = UserLogRepository.getInstance(context);
    }

    /**
     * For Singleton
     * @param context
     * @return Instance of Repository
     */
    public static synchronized UserRepository getInstance(Context context) {
        if (Instance == null) {
            Instance = new UserRepository(context.getApplicationContext());
        }
        return Instance;
    }

    /**
     * Returns the LiveData object containing the list of all User instances.
     *
     * @return LiveData<List<User>> for observers to subscribe to user list updates
     */
    public LiveData<List<User>> getAllUserLive() {
        return allUserLive;
    }

    /**
     * Clears all user data.
     */
    public void clearUser(){
        userDao.clearUser();
    }

    /**
     * Changes the password of the user with the given ID and logs the password change.
     *
     * Finds the user by ID; if found, creates a copy, sets the new password, and updates it via userDao.
     * After updating, calls userLogRepository.insertLog to record a MODIFY_PASSWORD UserLog.
     * Returns false immediately if no user is found for the given ID, without performing an update or logging.
     *
     * @param ID  the unique identifier of the user whose password is to be changed
     * @param pwd  the new password to set
     * @return Boolean  true if the user exists and the password was successfully changed; false otherwise
     */
    public boolean changeUserPassword(int ID, String pwd){
        User user = userDao.findUser(ID);
        if(user!=null){
            User newUser = userDao.copyUser(user);
            newUser.setPassword(pwd);

            userDao.updateUser(newUser);
            // insert UserLog
            userLogRepository.insertLog(new UserLog(ID, UserLog.MODIFY_PASSWORD));
            return true;
        }else{
            return false;
        }
    }

    /**
     * Finds and returns a User by its unique ID.
     *
     * @param ID  the unique identifier of the user to find
     * @return User  the matching User object, or null if not found
     */
    public User findUser(int ID) {
        return userDao.findUser(ID);
    }

    /**
     * Inserts a new user; returns immediately if a user with the same name or email already exists.
     *
     * Calls checkUserExists to ensure uniqueness of username or email; exits if a duplicate is found.
     * Retrieves the current user list from allUserLive; initializes to an empty list if null.
     * Generates a new ID for the user as (list size + 1).
     * Attempts to persist the user via userDao.insertUser, catching and logging any exceptions.
     * Records a SIGN_UP UserLog by invoking userLogRepository.insertLog.
     *
     * @param user the User object to insert
     */
    public void insertUser(User user) {
        if(checkUserExists(user.getName(), user.getEmail())){
            return;
        }else{
            //generated id
            List<User> list = allUserLive.getValue();
            if (list == null) {
                list = new ArrayList<>();
            }
            int newId = list.size() + 1;
            user.setID(newId);
            //insert
            try{
                Log.d("userDao inserting" , user.getName() + userDao.toString());
                userDao.insertUser(user);
            } catch (Exception e) {
                Log.e("UserInsertError", "userDao not finish!",e);
            }
            // generated user log : SIGN UP
            userLogRepository.insertLog(new UserLog(user.getID(), UserLog.SIGN_UP));
        }
    }

    /**
     * Finds the user ID for the given username.
     *
     * @param name  the username to look up
     * @return int  the matching user ID, or -1 if no such user exists
     */
    public int findIdByName(String name)
    {
        return userDao.findIdByName(name);
    }

    /**
     * Checks whether a user exists with the specified username or email.
     *
     * @param username  the username to check
     * @param email  the email address to check
     * @return Boolean  true if at least one matching user exists; false otherwise
     */
    public boolean checkUserExists(String username, String email) {
        return userDao.checkUserExists(username, email) > 0;
    }
}
