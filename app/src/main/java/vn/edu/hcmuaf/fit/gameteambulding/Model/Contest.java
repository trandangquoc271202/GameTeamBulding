package vn.edu.hcmuaf.fit.gameteambulding.Model;

import java.util.ArrayList;
import java.util.List;

public class Contest {
    private String id;
    private String title;
    private String timeStart;
    private String timeEnd;
    public List<String> listCriteria;
    public List<String> listEmail;
    public String content;

    public Contest() {
        this.listCriteria = new ArrayList<String>();
        this.listEmail = new ArrayList<String>();
    }

    public Contest(String id, String title, String timeStart, String timeEnd, String content) {
        this.id = id;
        this.title = title;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.listCriteria = new ArrayList<String>();
        this.listEmail = new ArrayList<String>();
        this.content = content;
    }

    public Contest(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public List<String> getListCriteria() {
        return listCriteria;
    }

    public void setListCriteria(List<String> listCriteria) {
        this.listCriteria = listCriteria;
    }

    public List<String> getListEmail() {
        return listEmail;
    }

    public void setListEmail(List<String> listEmail) {
        this.listEmail = listEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
