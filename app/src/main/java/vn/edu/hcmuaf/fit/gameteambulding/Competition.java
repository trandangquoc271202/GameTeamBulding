package vn.edu.hcmuaf.fit.gameteambulding;

public class Competition {
    private String candidate1;
    private String candidate2;
    private String content;
    private String creator;
    private String criteriaList;

    public void setCandidate1(String candidate1) {
        this.candidate1 = candidate1;
    }

    public void setCandidate2(String candidate2) {
        this.candidate2 = candidate2;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCriteriaList(String criteriaList) {
        this.criteriaList = criteriaList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCandidate1() {
        return candidate1;
    }

    public String getCandidate2() {
        return candidate2;
    }

    public String getContent() {
        return content;
    }

    public String getCreator() {
        return creator;
    }

    public String getCriteriaList() {
        return criteriaList;
    }

    public String getEmail() {
        return email;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getTitle() {
        return title;
    }

    public Competition(String cadidate1, String candidate2, String content, String creator, String criteriaList, String email, String timeStart, String timeEnd, String title) {
        this.candidate1 = cadidate1;
        this.candidate2 = candidate2;
        this.content = content;
        this.creator = creator;
        this.criteriaList = criteriaList;
        this.email = email;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.title = title;
    }

    private String email;
    private String timeStart;
    private String timeEnd;
    private String title;
}
