package com.example.parkingreport.ui.user.fragment.Myreport;

public class ReportItem {
    private int id;
    private String title;
    private String time;

    public ReportItem(int id, String title, String time) {  // 这里是3个参数的构造器
        this.id = id;
        this.title = title;
        this.time = time;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

