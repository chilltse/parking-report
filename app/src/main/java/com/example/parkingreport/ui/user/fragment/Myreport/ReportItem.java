package com.example.parkingreport.ui.user.fragment.Myreport;

public class ReportItem {
    private String plate;  // 车牌号
    private String state;  // 状态（原 title）
    private String time;   // 报告时间

    // 构造方法
    public ReportItem(String plate, String state, String time) {
        this.plate = plate;
        this.state = state;
        this.time = time;
    }

    // Getter 和 Setter
    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

