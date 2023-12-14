package vn.edu.hcmuaf.fit.gameteambulding;

import java.util.HashMap;
import java.util.Map;

public class Entry {
    private String ID;
    private String Entry_link;
    private String candidateID;
    private String competitionID;
    private String uploadTime;
    // review ID is added later when s/o do evaluate this entry of a competition
    private String reviewID;
    private Map<String, Object> map;
    private boolean win;

    public boolean isWin() {
        return win;
    }

    public Entry(String ID, String entry_link, String candidateID, String competitionID, String uploadTime, String reviewID, boolean win) {
        this.ID = ID;
        Entry_link = entry_link;
        this.candidateID = candidateID;
        this.competitionID = competitionID;
        this.uploadTime = uploadTime;
        this.reviewID = reviewID;
        this.win = win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public Entry(String ID, String entry_link, String candidateID, String competitionID, String uploadTime) {
        this.ID = ID;
        Entry_link = entry_link;
        this.candidateID = candidateID;
        this.competitionID = competitionID;
        this.uploadTime = uploadTime;
        map= new HashMap<String,Object>();
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    public void setCompetitionID(String competitionID) {
        this.competitionID = competitionID;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public String getCompetitionID() {
        return competitionID;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getReviewID() {
        return reviewID;
    }

    public Entry(String entry_link) {
        Entry_link = entry_link;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEntry_link(String entry_link) {
        Entry_link = entry_link;
    }

    public String getID() {
        return ID;
    }

    public String getEntry_link() {
        return Entry_link;
    }

    public Map<String, Object> getMap() {
        map.put("ENTRY_ID",ID);
        map.put("CANDIDATE_ID",candidateID);
        map.put("ENTRY_LINK",Entry_link);
        map.put("COMPETITION_ID",competitionID);
        map.put("UPLOAD_TIME",uploadTime);
        map.put("REVIEW","");
        map.put("WIN",false);
        return map;
    }
}
