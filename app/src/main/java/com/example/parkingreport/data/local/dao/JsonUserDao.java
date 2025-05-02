package com.example.parkingreport.data.local.dao;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingreport.data.local.entities.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.parkingreport.utils.structures.AVLTree;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class JsonUserDao implements UserDao{
    private final File file;
    private final MutableLiveData<List<User>> liveData = new MutableLiveData<>();

    private final AVLTree<User> userTree = new AVLTree<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    public JsonUserDao(File file) {
        this.file = file;
        loadFromFile();
        //use for debug
        Log.d("JSON_PATH",
                "path=" + file.getAbsolutePath() +
                        ", exists=" + file.exists());
    }

    /**
     * load user info from Files
     */
    private synchronized void loadFromFile() {
        List<User> list = new ArrayList<>();
//        Gson读取的底层逻辑
//        Gson 是用**反射（Reflection）**去动态地 new User()，然后给每个字段赋值。
//        并且是调用默认的无参构造函数，再通过**Field.set()**一个个把 JSON 里的值塞进去的！
        if (file.exists()) {
            //Gson version
            Gson gson = new Gson();
            JsonReader jsonReader = null;

            final Type USER_LIST_TYPE = new TypeToken<List<User>>() {}.getType();

            try {
                jsonReader = new JsonReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            list = gson.fromJson(jsonReader, USER_LIST_TYPE);

        }
        if (list == null) list = new ArrayList<>();
        final List<User> initial = list;

        // 创建AVL树,仅仅添加alive的User
        // 直接从list来，id保证正确
        for (User user : list) {
            if(user.isAlive()) userTree.insert(user);
        }
//        mainHandler.post(() -> liveData.setValue(initial));

        // 不管是否alive，所有的user都要添加到liveData中。
        liveData.setValue(initial); // When starting up, load the file for the first time
//        // and it cannot be executed at the background.
    }

    public AVLTree<User> getUserTree() {
        return userTree;
    }

    /**
     * Save user info to files
     * @param list
     */
    private synchronized void saveToFile(List<User> list) {
        // Gson version
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter fw = new FileWriter(file)){
            gson.toJson(list, fw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final List<User> insertList = list;
        mainHandler.post(() -> liveData.setValue(insertList));
//        liveData.setValue(insertList);
    }

    /**
     *  insert userinfo Atomicly
     * @param user
     */
    @Override
    public synchronized void insertUser(User user) {
        synchronized (this) {
            List<User> list = liveData.getValue();
            if (list == null) list = new ArrayList<>();
            list.add(user);
            userTree.insert(user);
            //TODO 这里频繁读写文件，可以改成只在onDestroy的时候回写或者怎样？
            saveToFile(list);
        }
    }

    /**
     *  find userinfo Atomicly
     * @param ID
     */
    @Override
    public synchronized User findUser(int ID) {
        synchronized (this) {
            return userTree.find(ID);
        }
    }

    public User copyUser(User user){
        User newUser =  new User(user.getName(),user.getEmail(),user.getPassword(),user.getRole(),user.isAlive());
        newUser.setID(user.getID());
        return newUser;
    }

    /**
     * Update user info atomically.
     * 使用方法：创建一个ID一致的user,然后直接调用这个函数。
     * @param user the updated user object
     */
    @Override
    public synchronized void updateUser(User user) {
        synchronized (this) {
            List<User> list = liveData.getValue();
            if (list == null) list = new ArrayList<>();

            // 1️⃣ 先在 List 里找到并更新对应的User
            User foundedUser = null;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getID() == user.getID()) {
                    foundedUser = list.get(i);
                    list.set(i, user); // 替换
                    break;
                }
            }

            if (foundedUser == null) {
                // 可选：如果没找到，可能是异常，可以选择抛异常或直接return
                Log.d("JSON_PATH", "Warning: User with ID " + user.getID() + " not found for update.");
                return;
            }

            // 2️⃣ 在 AVL Tree 里也更新
            userTree.delete(foundedUser); // 删除旧的
            userTree.insert(user); // 插入新的

            // 3️⃣ 保存回文件
            saveToFile(list);
        }
    }



    /**
     * Currently it is used to clear all user info, we will have one method to delete single info.
     * This will not save to user log
     */
    @Override
    public synchronized void clearUser() {
        // 更新内存和观察者
        List<User> empty = new ArrayList<>();
        saveToFile(empty);
        mainHandler.post(() -> liveData.setValue(empty));
//        liveData.setValue(empty);
    }

    /**
     * @param userId the id of user that we want to delete
     */
    // 变更：只改原数据的状态
    @Override
    public synchronized void deleteUser(int userId) {
        synchronized (this){
            List<User> list = liveData.getValue();
            User userInstance = new User();
            if (list != null){
                // find the instance in livedata, or remove won't work.
                for (User u : list) {
                    if(userId == u.getID()){
//                        userInstance = u;
                        u.setAlive(false);
                        userTree.delete(u);
                    }
                }
//                list.remove(userInstance);
            }
            saveToFile(list);
        }
    }


    /**
     * @param userId the id of user that need to modified
     * @param name new name
     */
    @Override
    public void modifyUserName(int userId, String name) {
        synchronized (this){
            List<User> list = liveData.getValue();
            if (list != null){
                // find the instance in livedata, or remove won't work.
                for (User u : list) {
                    if(userId == u.getID()) {
                        u.setName(name);
                        break;
                    }
                }
                saveToFile(list);
            }
        }
    }

    /**
     * @param userId the id of user that need to modified
     * @param password new password
     */
    @Override
    public void modifyUserPassword(int userId, String password) {
        synchronized (this){
            List<User> list = liveData.getValue();
            if (list != null){
                // find the instance in livedata, or remove won't work.
                for (User u : list) {
                    if(userId == u.getID()) {
                        u.setPassword(password);
                        break;
                    }
                }
                saveToFile(list);
            }
        }
    }

    @Override
    public LiveData<List<User>> getAllUsersLive() {
        return liveData;
    }

    @Override
    public int checkIdExists(int userId) {
        List<User> list = liveData.getValue();
        if (list == null) return 0;
        for (User u : list) {
            if (userId == u.getID()) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int checkUserExists(String username, String email) {
        List<User> list = liveData.getValue();
        if (list == null) return 0;
        int cnt = 0;
        for (User u : list) {
            if (username.equals(u.getName()) || email.equals(u.getEmail())) {
                cnt++;
            }
        }
        return cnt;
    }
}

