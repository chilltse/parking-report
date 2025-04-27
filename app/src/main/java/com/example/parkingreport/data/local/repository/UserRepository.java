package com.example.parkingreport.data.local.repository;

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

    public UserRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "users.json");
        this.userDao = new JsonUserDao(jsonFile);
        this.allUserLive = userDao.getAllUsersLive();
        // get the instance of userLog, when creating userRepository
        this.userLogRepository = UserLogRepository.getInstance(context);
    }

    public static synchronized UserRepository getInstance(Context context) {
        if (Instance == null) {
            Instance = new UserRepository(context.getApplicationContext());
        }
        return Instance;
    }

    public LiveData<List<User>> getAllUserLive() {
        return allUserLive;
    }

    public void clearUser(){
        userDao.clearUser();
    }

    /**
     *  generated id | insert new user | insert userLog
     * @param user
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
            int newId = generateNextAvailableID(list);
            user.setID(newId);

            //insert
            try{
//                Log.d("userDao", userDao.toString());
                userDao.insertUser(user);
            } catch (Exception e) {
                Log.e("UserInsertError", "userDao not finish!",e);
            }

            // generated user log : SIGN UP
//            Log.d("userLogRepository", userLogRepository.toString());
            userLogRepository.insertLog(new UserLog(user.getID(), UserLog.SIGN_UP));
        }
    }

    /**
     * @param currentUsers
     * @return find the smallest id number that has not been used, and return
     */
    private int generateNextAvailableID(List<User> currentUsers) {
        Set<Integer> ids = new HashSet<>();
        for (User u : currentUsers) {
            ids.add(u.getID());
        }
        int id = 1;
        while (ids.contains(id)) {
            id++;
        }
        return id;
    }


    public void deleteUser(int userId){
        if(userDao.checkIdExists(userId) == 0){
            return;
        }
        userDao.deleteUser(userId);
        // generated user log : CANCEL_ACCOUNT
        userLogRepository.insertLog(new UserLog(userId,UserLog.CANCEL_ACCOUNT));
    }

    public void modifyUserName(int userId, String name){
        // check userId exist
        if(userDao.checkIdExists(userId) == 0)
            return;
        // check name exist, the new might already used by the others
        String email = "xx";
        if(checkUserExists(name,email))
            return;
        //modify it
        userDao.modifyUserName(userId, name);
        // insert UserLog
        userLogRepository.insertLog(new UserLog(userId,UserLog.MODIFY_NAME));
    }

    public void modifyUserPassword(int userId, String password){
        // check userId exist
        if(userDao.checkIdExists(userId) == 0)
            return;
        //modify it
        userDao.modifyUserPassword(userId, password);
        // insert UserLog
        userLogRepository.insertLog(new UserLog(userId,UserLog.MODIFY_PASSWORD));
    }

    public boolean checkUserExists(String username, String email) {
        return userDao.checkUserExists(username, email) > 0;
    }
}
