package dev.knureview.VO;

/**
 * Created by DavidHa on 2016. 1. 7..
 */
public class ReviewVO {
    private int rNo;
    private int stdNo;
    private String description;
    private int scNo;
    private int sb_profNo;

    public int getrNo() {
        return rNo;
    }

    public void setrNo(int rNo) {
        this.rNo = rNo;
    }

    public int getStdNo() {
        return stdNo;
    }

    public void setStdNo(int stdNo) {
        this.stdNo = stdNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScNo() {
        return scNo;
    }

    public void setScNo(int scNo) {
        this.scNo = scNo;
    }

    public int getSb_profNo() {
        return sb_profNo;
    }

    public void setSb_profNo(int sb_profNo) {
        this.sb_profNo = sb_profNo;
    }
}
