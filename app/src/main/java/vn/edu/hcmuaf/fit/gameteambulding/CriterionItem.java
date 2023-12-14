package vn.edu.hcmuaf.fit.gameteambulding;

public class CriterionItem {
    private String criterion;
    private int score;

    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public CriterionItem(String criterion) {
        this.criterion = criterion;
    }
}
