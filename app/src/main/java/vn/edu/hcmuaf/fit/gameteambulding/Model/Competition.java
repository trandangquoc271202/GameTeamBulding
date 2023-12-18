package vn.edu.hcmuaf.fit.gameteambulding.Model;

import androidx.lifecycle.ViewModel;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Competition extends ViewModel implements Serializable {

    private CompetitionUser user1;
    private CompetitionUser user2;
    private String code;
    private String documentId;
    private String compettionUser1;
    private String compettionUser2;
    private String content;
    private String creator;

    @Override
    public String toString() {
        return "Competition{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", code='" + code + '\'' +
                ", documentId='" + documentId + '\'' +
                ", compettionUser1='" + compettionUser1 + '\'' +
                ", compettionUser2='" + compettionUser2 + '\'' +
                ", content='" + content + '\'' +
                ", creator='" + creator + '\'' +
                ", endAt='" + endAt + '\'' +
                ", startAt='" + startAt + '\'' +
                ", title='" + title + '\'' +
                ", totalVote='" + totalVote + '\'' +
                '}';
    }

    private String endAt;
    private String startAt;
    private String title;
    private String totalVote;

    public Competition(String code, String compettionUser1, String compettionUser2, String content, String creator, String endAt, String startAt, String title, String totalVote) {
        this.code = code;
        this.compettionUser1 = compettionUser1;
        this.compettionUser2 = compettionUser2;
        this.content = content;
        this.creator = creator;
        this.endAt = endAt;
        this.startAt = startAt;
        this.title = title;
        this.totalVote = totalVote;
    }

    public CompetitionUser getUser1() {
        return user1;
    }

    public void setUser1(CompetitionUser user1) {
        this.user1 = user1;
    }

    public CompetitionUser getUser2() {
        return user2;
    }

    public void setUser2(CompetitionUser user2) {
        this.user2 = user2;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompettionUser1() {
        return compettionUser1;
    }

    public void setCompettionUser1(String compettionUser1) {
        this.compettionUser1 = compettionUser1;
    }

    public String getCompettionUser2() {
        return compettionUser2;
    }

    public void setCompettionUser2(String compettionUser2) {
        this.compettionUser2 = compettionUser2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(String totalVote) {
        this.totalVote = totalVote;
    }


    public Competition() {
    }
}
