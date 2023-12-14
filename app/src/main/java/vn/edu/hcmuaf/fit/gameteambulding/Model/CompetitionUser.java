package vn.edu.hcmuaf.fit.gameteambulding.Model;

public class CompetitionUser {
    private String criteria;
    private String evaluate;
    private String result;
    private double totalScore;
    private int totalVote;
    private String userId;

    private UserInfo userInfo;

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    // Constructor
    public CompetitionUser(String criteria, String evaluate, String result,
                           double totalScore, int totalVote, String userId) {
        this.criteria = criteria;
        this.evaluate = evaluate;
        this.result = result;
        this.totalScore = totalScore;
        this.totalVote = totalVote;
        this.userId = userId;
    }


    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(int totalVote) {
        this.totalVote = totalVote;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter methods (generated automatically or manually)
    public CompetitionUser() {
    }


    @Override
    public String toString() {
        return "CompetitionUser{" +
                "criteria='" + criteria + '\'' +
                ", evaluate='" + evaluate + '\'' +
                ", result='" + result + '\'' +
                ", totalScore=" + totalScore +
                ", totalVote=" + totalVote +
                ", userId='" + userId + '\'' +
                '}';
    }
}