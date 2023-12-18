package vn.edu.hcmuaf.fit.gameteambulding.Model;

import java.util.ArrayList;
import java.util.List;

public class CompetitionUser implements java.io.Serializable {
    private String criteria;
    private String evaluate;
    private String result;
    private Long totalScore;
    private Long totalVote;
    private String userId;
    private List<CompetitionReviewUser> competitionReviewUsers;
    private UserInfo userInfo;

    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void add(CompetitionReviewUser competitionReviewUser) {
        if (this.competitionReviewUsers == null)
            this.competitionReviewUsers = new ArrayList<>();
        this.competitionReviewUsers.add(competitionReviewUser);
    }

    public String totalScore() {
        double totalScore = 0L;
        for (CompetitionReviewUser competitionReviewUser : competitionReviewUsers
        ) {
            if (competitionReviewUser.getScore() != 0)
                totalScore += competitionReviewUser.getScore();

        }
        return String.valueOf(totalScore);
    }

    public String totalVote() {
        return String.valueOf(competitionReviewUsers.size());
    }

    public CompetitionUser(String criteria, String evaluate, String result, Long totalScore, Long totalVote, String userId, UserInfo userInfo) {
        this.criteria = criteria;
        this.evaluate = evaluate;
        this.result = result;
        this.totalScore = totalScore;
        this.totalVote = totalVote;
        this.userId = userId;
        this.competitionReviewUsers = new ArrayList<>();
        this.userInfo = userInfo;
    }

    public CompetitionUser() {
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
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

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public Long getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(Long totalVote) {
        this.totalVote = totalVote;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CompetitionReviewUser> getCompetitionReviewUsers() {
        return competitionReviewUsers;
    }

    public void setCompetitionReviewUsers(List<CompetitionReviewUser> competitionReviewUsers) {
        this.competitionReviewUsers = competitionReviewUsers;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}