package com.example.parkingreport.data.local.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.parkingreport.data.local.entities.ReportLog;
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
import java.util.List;

/**
 * @author @u7864325 Weimiao Sun
 * A JSON-based implementation of the ReportLogDao interface that manages
 * reading and writing report logs to a local file using Gson serialization.
 * This class handles:
 * Loading report logs from a JSON file on initialization
 * Persisting new logs and updates to the file
 * Providing a LiveData stream of current logs for UI observation
 * It ensures thread-safety via synchronized methods and posts updates to the main thread using a Handler to support real-time UI refresh.
 */
public class JsonReportLogDao implements ReportLogDao{
    private final File file;
    private final MutableLiveData<List<ReportLog>> liveData = new MutableLiveData<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public JsonReportLogDao(File file) {
        this.file = file;
        loadFromFile();
        //use for debug
        Log.d("JSON_PATH",
                "path=" + file.getAbsolutePath() +
                        ", exists=" + file.exists());
    }

    /**
     * load reportLog info from Files
     */
    private synchronized void loadFromFile() {
        List<ReportLog> list = new ArrayList<>();
        if (file.exists()) {
            //Gson version
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")  //USE ISO VERSION;
                    .create();
            JsonReader jsonReader = null;

            final Type REPORTLOG_LIST_TYPE = new TypeToken<List<ReportLog>>() {}.getType();

            try {
                jsonReader = new JsonReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            list = gson.fromJson(jsonReader, REPORTLOG_LIST_TYPE);

        }
        final List<ReportLog> initial = list;
//        mainHandler.post(() -> liveData.setValue(snapshot));
        liveData.setValue(initial); // When starting up, load the file for the first time
        // and it cannot be executed asynchronously.
    }

    /**
     * Save user info to files
     * @param list
     */
    private synchronized void saveToFile(List<ReportLog> list) {
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

        final List<ReportLog> insertList = list;
        mainHandler.post(() -> liveData.setValue(insertList));
//        liveData.setValue(insertList);
    }

    @Override
    public void insertLog(ReportLog reportLog) {
        synchronized (this) {
            List<ReportLog> list = liveData.getValue();
            if (list == null) list = new ArrayList<>();
            list.add(reportLog);
            saveToFile(list);
        }
    }

    @Override
    public void clearLog() {
        List<ReportLog> empty = new ArrayList<>();
        saveToFile(empty);
        mainHandler.post(() -> liveData.setValue(empty));
//        liveData.setValue(empty);
    }

    @Override
    public LiveData<List<ReportLog>> getAllReportLogsLive() {
        return liveData;
    }
}

