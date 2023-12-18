package vn.edu.hcmuaf.fit.gameteambulding.Model;

public class CompetitionReviewUser implements java.io.Serializable {
    private String CompetitionUserId;
    private boolean Male;
    private String Name;
    private String Province;
    private double Score;

    public String getCompetitionUserId() {
        return CompetitionUserId;
    }

    public void setCompetitionUserId(String competitionUserId) {
        CompetitionUserId = competitionUserId;
    }

    public boolean isMale() {
        return Male;
    }

    public void setMale(boolean male) {
        Male = male;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    @Override
    public String toString() {
        return "CompetitionReviewUser{" +
                "CompetitionUserId='" + CompetitionUserId + '\'' +
                ", Male=" + Male +
                ", Name='" + Name + '\'' +
                ", Province='" + Province + '\'' +
                ", Score=" + Score +
                '}';
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }
}
