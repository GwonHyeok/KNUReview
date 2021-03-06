package dev.knureview.VO;

/**
 * Created by DavidHa on 2015. 11. 25..
 */
public class StudentVO {

    private int stdNo;
    private String name;
    private String belong;
    private String major;
    private int reviewCnt;
    private int reviewAuth;
    private int talkCnt;
    private int talkWarning;
    private int talkAuth;
    private int talkTicket;
    private String regId;
    private boolean isExist;
    private String key;

    public StudentVO() {
        this.key = "강k남n대u리r뷰v키k";
    }

    public int getStdNo() {
        return stdNo;
    }

    public void setStdNo(int stdNo) {
        this.stdNo = stdNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getTalkAuth() {
        return talkAuth;
    }

    public void setTalkAuth(int talkAuth) {
        this.talkAuth = talkAuth;
    }

    public int getTalkTicket() {
        return talkTicket;
    }

    public void setTalkTicket(int talkTicket) {
        this.talkTicket = talkTicket;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
