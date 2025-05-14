package com.example.parkingreport.data.local.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.utils.structures.AVLTree;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author u7864325 Weimiao Sun
 * DAO implementation for {User} backed by a JSON file and an in‑memory
 * {AVLTree} for fast ID look‑ups.
 */
public class JsonUserDao implements UserDao {

    /*──────────────────────── fields ────────────────────────*/

    private final File file;
    private final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
    private final AVLTree<User> userTree = new AVLTree<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Map<String, Integer> nameMap = new HashMap<>();

    /*──────────────────────── ctor ────────────────────────*/

    public JsonUserDao(File file) {
        this.file = file;
        loadFromFile();
        Log.d("JSON_PATH", "User path=" + file.getAbsolutePath() + ", exists=" + file.exists());
    }

    /*──────────────────────── private helpers ────────────────────────*/

    public int findIdByName(String name)
    {
        if(nameMap.get(name)!=null){
            return nameMap.get(name);
        }else{
//            throw new RuntimeException("No such user for "+ name);
            return -1;
        }
    }


    /** Load entire JSON once at startup and build the AVL index. */
    private synchronized void loadFromFile() {
        List<User> list = new ArrayList<>();

        /* If the JSON file does not exist yet, create an empty one */
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    // write an empty JSON array "[]" so Gson can parse next time
                    try (FileWriter fw = new FileWriter(file)) { fw.write("[]"); }
                    Log.d("JSON_PATH", "Created new user file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                Log.e("JSON_PATH", "Cannot create user file", e);
            }
        }

        /* now try loading (file definitely exists) */
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<User>>() {}.getType();
        try (JsonReader reader = new JsonReader(new FileReader(file))) {
            list = gson.fromJson(reader, type);
        } catch (IOException e) {
            Log.e("JSON_PATH", "User file read error", e);
        }
        if (list == null) list = new ArrayList<>();

        for (User u : list) {
            if (u.isAlive()) userTree.insert(u);
            nameMap.put(u.getName(), u.getID());
        }
        liveData.setValue(list);
    }

    /** Persist full list to disk and emit new snapshot. */
    private synchronized void saveToFile(List<User> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(list, fw);
        } catch (Exception e) {
            Log.e("JSON_PATH", "Error saving users", e);
        }
        List<User> snap = new ArrayList<>(list);
        mainHandler.post(() -> liveData.setValue(snap));
    }


    /*──────────────────────── exposed helpers ────────────────────────*/

    public AVLTree<User> getUserTree() { return userTree; }

    public User copyUser(User src) {
        User c = new User(src.getName(), src.getEmail(), src.getPassword(), src.getProfilePicUrl(),src.getRole(), src.isAlive());
        c.setID(src.getID());
        return c;
    }

    /*──────────────────────── CRUD ────────────────────────*/

    @Override
    public synchronized void insertUser(User user) {
        List<User> list = liveData.getValue();
        if (list == null) list = new ArrayList<>();
        list.add(user);
        userTree.insert(user);
        nameMap.put(user.getName(), user.getID());
        Log.w("JSON_PATH", "Insert User, now list is : " + list.toString());

        saveToFile(list);
    }

    @Override
    public synchronized User findUser(int id) {
        return userTree.find(id);
    }

    @Override
    public synchronized void updateUser(User user) {
        List<User> list = liveData.getValue();
        if (list == null) list = new ArrayList<>();

        User old = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getID() == user.getID()) {
                old = list.get(i);
                list.set(i, user);
                break;
            }
        }
        if (old == null) {
            Log.w("JSON_PATH", "User " + user.getID() + " not found");
            return;
        }
        userTree.delete(old);
        userTree.insert(user);
        // Replace
        nameMap.put(user.getName(), user.getID());
        saveToFile(list);
    }

    @Override
    public synchronized void clearUser() {
        saveToFile(new ArrayList<>());
    }

    public synchronized void deleteUser(User user) {
        List<User> list = liveData.getValue();
        if (list == null) return;
        for (User u : list) {
            if (u.getID() == user.getID()) {
                u.setAlive(false);
                userTree.delete(u);
                nameMap.remove(user.getName());
                break;
            }
        }
        saveToFile(list);
    }

    @Override
    public synchronized void modifyUserName(int id, String name) {
        List<User> list = liveData.getValue();
        if (list == null) return;
        for (User u : list) if (u.getID() == id) { u.setName(name); break; }
        saveToFile(list);
    }

    @Override
    public synchronized void modifyUserPassword(int id, String pwd) {
        List<User> list = liveData.getValue();
        if (list == null) return;
        for (User u : list) if (u.getID() == id) { u.setPassword(pwd); break; }
        saveToFile(list);
    }

    /*──────────────────────── Query ────────────────────────*/

    @Override public LiveData<List<User>> getAllUsersLive() { return liveData; }

    @Override public int checkIdExists(int id) {
        List<User> l = liveData.getValue();
        return (l != null && l.stream().anyMatch(u -> u.getID() == id)) ? 1 : 0;
    }

    @Override public int checkUserExists(String name, String email) {
        List<User> l = liveData.getValue();
        if (l == null) return 0;
        return (int) l.stream().filter(u -> name.equals(u.getName()) || email.equals(u.getEmail())).count();
    }
}
