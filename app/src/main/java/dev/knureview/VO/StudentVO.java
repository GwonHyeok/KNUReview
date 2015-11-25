package dev.knureview.VO;

/**
 * Created by DavidHa on 2015. 11. 25..
 */
public class StudentVO {

    private int id;
    private String dept;
    private String major;
    private int reviewCnt;
    private int reviewAuth;
    private int talkCnt;
    private int talkWarning;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getReviewCnt() {
        return reviewCnt;
    }

    public void setReviewCnt(int reviewCnt) {
        this.reviewCnt = reviewCnt;
    }

    public int getReviewAuth() {
        return reviewAuth;
    }

    public void setReviewAuth(int reviewAuth) {
        this.reviewAuth = reviewAuth;
    }

    public int getTalkCnt() {
        return talkCnt;
    }

    public void setTalkCnt(int talkCnt) {
        this.talkCnt = talkCnt;
    }

    public int getTalkWarning() {
        return talkWarning;
    }

    public void setTalkWarning(int talkWarning) {
        this.talkWarning = talkWarning;
    }
}
