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
     * Loads a list of ReportLog entries from a local JSON file and updates LiveData.
     *
     * Synchronized to ensure thread safety and prevent concurrent access issues.
     * If the file exists, creates a Gson instance with ISO 8601 date format
     * and deserializes the JSON via JsonReader into a List<ReportLog>.
     * Catches FileNotFoundException; if the file is missing or cannot be read, retains an empty list.
     * Finally, calls LiveData.setValue to push the loaded data to observers.
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
        liveData.setValue(initial); // When starting up, load the file for the first time
    }

    /**
     * Serializes the given list of ReportLog entries to a local JSON file,
     * then updates LiveData on the main thread.
     *
     * Synchronized to ensure thread safety.
     * Uses ISO 8601 date format and pretty printing for JSON output.
     * Catches and logs any exceptions during file writing.
     * After saving, posts the updated list to LiveData via the main thread Handler to notify observers.
     *
     *  @param list
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
    }

    /**
     * Inserts a new ReportLog entry into the list and persists it to file.
     *
     * Executes in a synchronized block to ensure thread safety.
     * Retrieves the current list from LiveData; initializes to an empty list if null.
     * Adds the provided ReportLog to the list and calls saveToFile to save changes.
     *
     * @param reportLog the ReportLog object to insert and save
     */
    @Override
    public void insertLog(ReportLog reportLog) {
        synchronized (this) {
            List<ReportLog> list = liveData.getValue();
            if (list == null) list = new ArrayList<>();
            list.add(reportLog);
            saveToFile(list);
        }
    }

    /**
     * Clears all ReportLog entries by saving an empty list and updating LiveData.
     */
    @Override
    public void clearLog() {
        List<ReportLog> empty = new ArrayList<>();
        saveToFile(empty);
        mainHandler.post(() -> liveData.setValue(empty));
    }

    /**
     * Returns the LiveData object containing the list of all ReportLog entries.
     *
     * @return LiveData<List<ReportLog>> that observers can subscribe to for data updates
     */
    @Override
    public LiveData<List<ReportLog>> getAllReportLogsLive() {
        return liveData;
    }
}

