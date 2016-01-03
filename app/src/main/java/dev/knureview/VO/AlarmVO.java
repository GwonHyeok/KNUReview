package dev.knureview.VO;

/**
 * Created by DavidHa on 2016. 1. 3..
 */
public class AlarmVO {
    private int aNo;
    private int r_stdNo;
    private int s_stdNo;
    private String writeTime;
    private int tNo;
    private int cNo;
    private String pictureURL;
    private String description;
    private int isLike;

    public int getaNo() {
        return aNo;
    }

    public void setaNo(int aNo) {
        this.aNo = aNo;
    }

    public int getR_stdNo() {
        return r_stdNo;
    }

    public void setR_stdNo(int r_stdNo) {
        this.r_stdNo = r_stdNo;
    }

    public int getS_stdNo() {
        return s_stdNo;
    }

    public void setS_stdNo(int s_stdNo) {
        this.s_stdNo = s_stdNo;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public int gettNo() {
        return tNo;
    }

    public void settNo(int tNo) {
        this.tNo = tNo;
    }

    public int getcNo() {
        return cNo;
    }

    public void setcNo(int cNo) {
        this.cNo = cNo;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
