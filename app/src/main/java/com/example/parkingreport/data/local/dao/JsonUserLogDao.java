package com.example.parkingreport.data.local.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingreport.data.local.entities.UserLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author @u7864325 Weimiao Sun
 *
 * A JSON-based implementation of the UserLogDao interface that manages
 * persistent storage and live access of user log entries using local file storage.
 *
 * This class handles:
 * - Loading user logs from a JSON file during initialization
 * - Saving logs to disk upon insertion or clearing
 * - Exposing logs through LiveData for UI observation and reactivity
 *
 * Thread-safe operations are ensured via synchronized methods, and updates
 * are posted to the main thread using a Handler to keep the UI in sync.
 */
public class JsonUserLogDao implements UserLogDao{
    private final File file;
    private final MutableLiveData<List<UserLog>> liveData = new MutableLiveData<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public JsonUserLogDao(File file) {
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
        List<UserLog> list = new ArrayList<>();
        if (file.exists()) {
            //Gson version
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")  //USE ISO VERSION;
                    .create();
            JsonReader jsonReader = null;

            final Type USERLOG_LIST_TYPE = new TypeToken<List<UserLog>>() {}.getType();

            try {
                jsonReader = new JsonReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            list = gson.fromJson(jsonReader, USERLOG_LIST_TYPE);

        }
        final List<UserLog> initial = list;
//        mainHandler.post(() -> liveData.setValue(snapshot));
        liveData.setValue(initial); // When starting up, load the file for the first time
        // and it cannot be executed asynchronously.
    }

    /**
     * Save user info to files
     * @param list
     */
    private synchronized void saveToFile(List<UserLog> list) {
        // Gson version
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") //USE ISO VERSION
                .setPrettyPrinting()
                .create();
        try(FileWriter fw = new FileWriter(file)){
            gson.toJson(list, fw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final List<UserLog> insertList = list;
        mainHandler.post(() -> liveData.setValue(insertList));
//        liveData.setValue(insertList);
    }

    @Override
    public synchronized void insertLog(UserLog userLog) {
        synchronized (this) {
            List<UserLog> list = liveData.getValue();
            if (list == null) list = new ArrayList<>();
            list.add(userLog);
            saveToFile(list);
        }
    }

    @Override
    public void clearLog() {
        List<UserLog> empty = new ArrayList<>();
        saveToFile(empty);
        mainHandler.post(() -> liveData.setValue(empty));
//        liveData.setValue(empty);
    }

    @Override
    public LiveData<List<UserLog>> getAllUserLogsLive() {
        return liveData;
    }
}
