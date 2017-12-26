package com.example.administrator.note.bean;

/**
 * Created by Administrator on 2017/12/14.
 */

public class NoteBean {
    private int id;
    public String time;
    public String wenben;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return wenben;
    }

    public void setContent(String wenben) {
        this.wenben = wenben;
    }
}
