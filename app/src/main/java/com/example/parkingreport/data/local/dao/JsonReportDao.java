package com.example.parkingreport.data.local.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingreport.data.local.entities.Report;

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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonReportDao implements ReportDao{

    private final File file;
    private final AVLTree<Report> reportTree = new AVLTree<>();
    private final AVLTree<Report> waitingReportTree = new AVLTree<>();
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
        if (list == null) list = new ArrayList<>();
        final List<Report> initial = list;


        // 创建AVL树,仅仅添加alive的User
        // 直接从list来，id保证正确
        for (Report report : list) {
            reportTree.insert(report);
            if(report.getStatus() == Report.WAIT_FOR_REVIEW){
                waitingReportTree.insert(report);
            }
        }
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


            // 新增的report默认都是待批状态
            report.setStatus(Report.WAIT_FOR_REVIEW);
            list.add(report);

            reportTree.insert(report);
            waitingReportTree.insert(report);
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

//    @Override
//    public synchronized void deleteReport(int reportId) {
//        synchronized (this){
//            List<Report> list = liveData.getValue();
//            Report reportInstance = new Report();
//            if (list != null){
//                // find the instance in livedata, or remove won't work.
//                for (Report r : list) {
//                    if(reportId == r.getID())
//                        reportInstance = r;
//                }
//                list.remove(reportInstance);
//            }
//            saveToFile(list);
//        }
//    }

    @Override
    public synchronized void handleReport(int reportId, String status) {
        synchronized (this){
            List<Report> list = liveData.getValue();
            if (list != null){
                // find the instance in livedata, or remove won't work.
                for (Report r : list) {
                    if(reportId == r.getID()) {
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
            if(reportId == r.getID())
                return 1;
        }
        return 0;
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

    //TODO 待测试

    /**
     *  find report info
     * @param ID
     */
    @Override
    public synchronized Report findReport(int ID, boolean isWaitStatus) {
        synchronized (this) {
            return isWaitStatus ? waitingReportTree.find(ID) : reportTree.find(ID);
        }
    }

    @Override
    public Report copyReport(Report report){
        Report newReport =  new Report(
                report.getUserId(),
                report.getCarPlate(),
                report.getLocation(),
                report.getStatus());
        newReport.setID(report.getID());
        return newReport;
    }

    /**
     * Update Report info atomically.
     * 使用方法：创建一个ID一致的Report,然后直接调用这个函数。
     * @param report the updated report object
     */
    @Override
    public synchronized void updateReport(Report report) {
        synchronized (this) {
            List<Report> list = liveData.getValue();
            if (list == null) list = new ArrayList<>();

            if (waitingReportTree.find(report.getID()) == null) {
                // 可选：如果没找到，可能是异常，可以选择抛异常或直接return
                Log.d("JSON_PATH", "Warning: User with ID " + report.getID() + " not found for update.");
                return;
            }

            // 1️⃣ 先在 List 里找到并更新对应的Report
            Report foundedReport = null;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getID() == report.getID()) {
                    foundedReport = list.get(i);
                    list.set(i, report); // 替换
                    break;
                }
            }

            // 2️⃣ 在 AVL Tree 里也更新
            // waitingReportTree删除该节点。reportTree变更该节点
            waitingReportTree.delete(foundedReport); // 删除旧的
            reportTree.delete(foundedReport); // 删除旧的
            reportTree.insert(report); // 插入新的

            // 3️⃣ 保存回文件
            saveToFile(list);
        }
    }
}
