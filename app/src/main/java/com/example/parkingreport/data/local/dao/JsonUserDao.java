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
    private final File file;
    private final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
    private final AVLTree<User> userTree = new AVLTree<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Map<String, Integer> nameMap = new HashMap<>();


    public JsonUserDao(File file) {
        this.file = file;
        loadFromFile();
        Log.d("JSON_PATH", "User path=" + file.getAbsolutePath() + ", exists=" + file.exists());
    }

    /**
     * Finds the user ID associated with the given username.
     *
     * - Retrieves the ID from nameMap by the provided name.
     * - Returns the ID if found; otherwise returns -1 to indicate not found.
     *
     * @param name  the username to look up
     * @return int  the corresponding user ID, or -1 if no such user exists
     */
    public int findIdByName(String name)
    {
        if(nameMap.get(name)!=null){
            return nameMap.get(name);
        }else{
//            throw new RuntimeException("No such user for "+ name);
            return -1;
        }
    }

    /**
     * Loads the list of Users from a local JSON file, creating the file with an empty array if missing.
     *
     * Synchronized to ensure thread safety and prevent concurrent access issues.
     * If the file does not exist, creates it and writes "[]" so Gson can parse it next time.
     * Uses Gson to deserialize the file into a List<User>; logs any I/O errors and falls back to an empty list.
     * Inserts alive users into userTree and populates nameMap with username→ID mappings.
     * Finally calls LiveData.setValue to deliver the updated user list to observers.
     */
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

    /**
     * Serializes the given list of Users to a local JSON file, then updates LiveData on the main thread.
     *
     * Synchronized to ensure thread safety and prevent concurrent write conflicts.
     * Uses Gson PrettyPrinting to format the JSON output.
     * Catches and logs any exceptions that occur during file writing.
     * After saving, creates a snapshot of the list and posts it to LiveData via mainHandler on the main thread to notify observers.
     *
     * @param list the list of User objects to save
     */
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

    /**
     * Creates a copy of the given User, preserving all its properties.
     *
     * Constructs a new User using src’s name, email, password, profilePicUrl, role, and alive status.
     * Sets the new User’s ID to match the source, ensuring consistent unique identification.
     *
     * @param src  the source User to duplicate
     * @return User  a new User instance with identical field values
     */
    public User copyUser(User src) {
        User c = new User(src.getName(), src.getEmail(), src.getPassword(), src.getProfilePicUrl(),src.getRole(), src.isAlive());
        c.setID(src.getID());
        return c;
    }

    /**
     * Inserts a new User into the list, updates indexes, mappings, and persists changes.
     *
     * Synchronized to ensure thread safety and prevent concurrent insertions.
     * Retrieves the current user list from LiveData; initializes it if null.
     * Adds the new User to the list and inserts it into userTree.
     * Registers the username→ID mapping in nameMap.
     * Logs the insertion and the current list state.
     * Calls saveToFile to serialize the updated list to the local JSON file and refresh LiveData.
     *
     * @param user the User object to insert
     */
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

    /**
     * Tree find
     * @param id the id of User
     * @return The user object corresponding to the id
     */
    @Override
    public synchronized User findUser(int id) {
        return userTree.find(id);
    }

    /**
     * Updates an existing User, synchronizes index structures, and persists changes.
     *
     * Synchronized to ensure thread safety and prevent concurrent modifications.
     * Retrieves the current user list from LiveData; initializes to an empty list if null.
     * Iterates through the list to locate and replace the old User with the provided instance; logs a warning and returns if not found.
     * Deletes the old node from userTree and inserts the updated User to refresh the index.
     * Updates the username→ID mapping in nameMap.
     * Calls saveToFile to serialize the updated list to the local JSON file and refresh LiveData.
     *
     * @param user the User object to update; must have a valid ID
     */
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

    /**
     * Clears all user data by saving an empty list to the local file.
     */
    @Override
    public synchronized void clearUser() {
        saveToFile(new ArrayList<>());
    }

    /**
     * Returns the LiveData object containing the list of all Users.
     *
     * @return LiveData<List<User>> that observers can subscribe to for updates
     */
    @Override public LiveData<List<User>> getAllUsersLive() { return liveData; }

    /**
     * Checks for users matching the specified name or email and returns the count.
     *
     * @param name  the username to check
     * @param email  the email address to check
     * @return int  the number of matching users, or 0 if none found or list is null
     */
    @Override public int checkUserExists(String name, String email) {
        List<User> l = liveData.getValue();
        if (l == null) return 0;
        return (int) l.stream().filter(u -> name.equals(u.getName()) || email.equals(u.getEmail())).count();
    }
}
