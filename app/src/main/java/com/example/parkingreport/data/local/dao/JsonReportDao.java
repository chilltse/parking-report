package com.example.parkingreport.data.local.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingreport.data.local.entities.Report;

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

public class JsonReportDao implements ReportDao{

    private final File file;
    private final MutableLiveData<List<Report>> liveData = new MutableLiveData<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public JsonReportDao(File file) {
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
        List<Report> list = new ArrayList<>();
        if (file.exists()) {
            //Gson version
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")  //USE ISO VERSION;
                    .create();
            JsonReader jsonReader = null;

            final Type REPORT_LIST_TYPE = new TypeToken<List<Report>>() {}.getType();

            try {
                jsonReader = new JsonReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            list = gson.fromJson(jsonReader, REPORT_LIST_TYPE);

        }
        final List<Report> initial = list;
//        mainHandler.post(() -> liveData.setValue(snapshot));
        liveData.setValue(initial); // When starting up, load the file for the first time
        // and it cannot be executed asynchronously.
    }

    /**
     * Save user info to files
     * @param list
     */
    private synchronized void saveToFile(List<Report> list) {
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

        final List<Report> insertList = list;
        mainHandler.post(() -> liveData.setValue(insertList));
//        liveData.setValue(insertList);
    }

    @Override
    public synchronized void insertReport(Report report) {
        synchronized (this){
            List<Report> list = liveData.getValue();
            if (list == null) list = new ArrayList<>();
            list.add(report);
            saveToFile(list);
        }
    }

    @Override
    public void clearReport() {
        List<Report> empty = new ArrayList<>();
        saveToFile(empty);
        mainHandler.post(() -> liveData.setValue(empty));
//        liveData.setValue(empty);
    }

    @Override
    public synchronized void deleteReport(int reportId) {
        synchronized (this){
            List<Report> list = liveData.getValue();
            Report reportInstance = new Report();
            if (list != null){
                // find the instance in livedata, or remove won't work.
                for (Report r : list) {
                    if(reportId == r.getReportId())
                        reportInstance = r;
                }
                list.remove(reportInstance);
            }
            saveToFile(list);
        }
    }

    @Override
    public synchronized void handleReport(int reportId, int status) {
        synchronized (this){
            List<Report> list = liveData.getValue();
            if (list != null){
                // find the instance in livedata, or remove won't work.
                for (Report r : list) {
                    if(reportId == r.getReportId()) {
                        r.setStatus(status);
                        break;
                    }
                }
                saveToFile(list);
            }
        }
    }

    @Override
    public LiveData<List<Report>> getAllReportsLive() {
        return liveData;
    }

    @Override
    public int checkReportExists(int reportId) {
        List<Report> list = liveData.getValue();
        if(list == null) return 0;
        for (Report r : list) {
            if(reportId == r.getReportId())
                return 1;
        }
        return 0;
    }

    @Override
    public int checkReportStatus(int reportId) {
        List<Report> list = liveData.getValue();
        if(list == null) return 0;
        for (Report r : list) {
            if(reportId == r.getReportId())
                return r.getStatus();
        }
        return 0;
    }
}
