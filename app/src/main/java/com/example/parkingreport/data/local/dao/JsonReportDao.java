package com.example.parkingreport.data.local.dao;



import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.utils.structures.AVLTree;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
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
 *
 * DAO backed by a JSON file and two AVL trees.
 * <ul>
 *     <li>{@code reportTree}        — all reports</li>
 *     <li>{@code waitingReportTree} — reports with status {@link Report#WAIT_FOR_REVIEW}</li>
 * </ul>
 * <p>
 * Two additional maps provide O(1) reverse look‑ups:
 * <ul>
 *     <li>{@code userIdMap} — userId → list of report IDs</li>
 *     <li>{@code plateMap}  — plate  → list of report IDs</li>
 * </ul>
 * <p>
 * All mutating operations are {@code synchronized} to guard internal state.
 */
public class JsonReportDao implements ReportDao {

    /* ───────────────────────── fields ───────────────────────── */
    private final File file;

    private final AVLTree<Report> reportTree        = new AVLTree<>();
    private final AVLTree<Report> waitingReportTree = new AVLTree<>();

    private final Map<Integer, List<Integer>> userIdMap = new HashMap<>();
    private final Map<String, List<Integer>> plateMap  = new HashMap<>();

    private final MutableLiveData<List<Report>> liveData = new MutableLiveData<>();
    private final Handler                       mainHandler = new Handler(Looper.getMainLooper());

    /* ───────────────────────── ctor ────────────────────────── */
    public JsonReportDao(File file) {
        this.file = file;
        loadFromFile();
        Log.d("JSON_PATH", "path=" + file.getAbsolutePath() + ", exists=" + file.exists());
    }

    /* ───────────────── persistence helpers ────────────────── */
    private synchronized void loadFromFile() {
        List<Report> list = new ArrayList<>();

        if (file.exists()) {
            Gson  gson     = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Type  listType = new TypeToken<List<Report>>() {}.getType();

            try (JsonReader reader = new JsonReader(new FileReader(file))) {
                list = gson.fromJson(reader, listType);
            } catch (IOException e) {
                Log.e("JSON_PATH", "Error loading reports", e);
            }
        }

        if (list == null) list = new ArrayList<>();
        list.forEach(this::insertIntoIndexes);
        liveData.setValue(list);
    }

    private synchronized void saveToFile(List<Report> list) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setPrettyPrinting()
                .create();

        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(list, fw);
        } catch (IOException e) {
            Log.e("JSON_PATH", "Save error", e);
        }

        List<Report> snapshot = new ArrayList<>(list);
        mainHandler.post(() -> liveData.setValue(snapshot));
    }

    /* ───────────────── index maintenance ──────────────────── */
    private void insertIntoIndexes(Report r) {
        reportTree.insert(r);
        if (r.getStatus().equals(Report.WAIT_FOR_REVIEW) ) {
            waitingReportTree.insert(r);
        }

        userIdMap.computeIfAbsent(r.getUserId(),  k -> new ArrayList<>()).add(r.getID());
        plateMap .computeIfAbsent(r.getCarPlate(), k -> new ArrayList<>()).add(r.getID());
    }

    /* ─────────────────────── CRUD ─────────────────────────── */
    @Override
    public synchronized void insertReport(Report report) {
        List<Report> list = liveData.getValue();
        if (list == null) list = new ArrayList<>();

        report.setStatus(Report.WAIT_FOR_REVIEW);
        list.add(report);
        insertIntoIndexes(report);
        saveToFile(list);
    }



    @Override
    public synchronized void updateReport(Report report) {
        List<Report> list = liveData.getValue();
        if (list == null) list = new ArrayList<>();

        if (waitingReportTree.find(report.getID()) == null) {
            // if not found, maybe exception, choose to throw or return
            Log.d("JSON_PATH", "Warning: User with ID " + report.getID() + " not found for update.");
            return;
        }

        // 1. first find matched Report in List
        Report foundedReport = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getID() == report.getID()) {
                foundedReport = list.get(i);
                list.set(i, report); // Replace
                break;
            }
        }

        // 2. Update in AVL Tree
        // waitingReportTree delete this node, reportTree insert this node
        waitingReportTree.delete(foundedReport); // delete the old
        reportTree.delete(foundedReport); // delete the old
        reportTree.insert(report); // insert the new

        // 3 save back to file
        saveToFile(list);
    }

    /* ─────────────────── query helpers ───────────────────── */
    @Override public List<Report> getAllReportsLive() { return liveData.getValue(); }

    @Override public List<Report> getAllWaitingReportsLive() {
        Log.d("JsonReportDao", waitingReportTree.toString());
        return waitingReportTree.inorderList();
    }

    @Override
    public int checkReportExists(int id) {
        List<Report> l = liveData.getValue();
        return (l != null && l.stream().anyMatch(r -> r.getID() == id)) ? 1 : 0;
    }

    @Override
    public String checkReportStatus(int reportId) {
        List<Report> list = liveData.getValue();
        if(list == null) return null;
        for (Report r : list) {
            if(reportId == r.getID())
                return r.getStatus();
        }
        return null;
    }


    @Override
    public synchronized Report findReport(int id, boolean waitingOnly) {
        return waitingOnly ? waitingReportTree.find(id) : reportTree.find(id);
    }

    @Override
    public Report copyReport(Report src) {
        Report copy = new Report(src.getUserId(), src.getUserName(), src.getCarPlate(), src.getLocation(), src.getReportPicUrl(), src.getStatus());
        copy.setID(src.getID());
        return copy;
    }

    /* ─────────────────── reverse look‑ups ─────────────────── */
    public List<Integer> getIdsByUser(int userId) {
        return userIdMap.getOrDefault(userId, List.of());
    }

    public List<Integer> getIdsByPlate(String plate) {
        return plateMap.getOrDefault(plate, List.of());
    }
}