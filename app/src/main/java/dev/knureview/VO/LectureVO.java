package dev.knureview.VO;

/**
 * Created by DavidHa on 2015. 12. 17..
 */
public class LectureVO {
    private int stdNo;
    private int year;
    private int term;
    private String sbjName;
    private int isReview;

    public int getStdNo() {
        return stdNo;
    }

    public void setStdNo(int stdNo) {
        this.stdNo = stdNo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getSbjName() {
        return sbjName;
    }

    public void setSbjName(String sbjName) {
        this.sbjName = sbjName;
    }

    public int getIsReview() {
        return isReview;
    }

    public void setIsReview(int isReview) {
        this.isReview = isReview;
    }
}
