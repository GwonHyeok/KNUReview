package dev.knureview.VO;

/**
 * Created by DavidHa on 2015. 11. 25..
 */
public class StudentVO {

    private int stdNo;
    private String belong;
    private String major;
    private int reviewCnt;
    private int reviewAuth;
    private int talkCnt;
    private int talkWarning;
    private boolean isExist;

    public int getStdNo() {
        return stdNo;
    }

    public void setStdNo(int stdNo) {
        this.stdNo = stdNo;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
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

    public boolean isExist() {
        return isExist;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }
}
