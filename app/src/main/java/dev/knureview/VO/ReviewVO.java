package dev.knureview.VO;

/**
 * Created by DavidHa on 2016. 1. 7..
 */
public class ReviewVO {
    private int rNo;
    private int stdNo;
    private String description;
    private int difc;
    private int asign;
    private int atend;
    private int grade;
    private int achiv;
    private int sbjNo;
    private int profNo;

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

    public int getDifc() {
        return difc;
    }

    public void setDifc(int difc) {
        this.difc = difc;
    }

    public int getAsign() {
        return asign;
    }

    public void setAsign(int asign) {
        this.asign = asign;
    }

    public int getAtend() {
        return atend;
    }

    public void setAtend(int atend) {
        this.atend = atend;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getAchiv() {
        return achiv;
    }

    public void setAchiv(int achiv) {
        this.achiv = achiv;
    }

    public int getSbjNo() {
        return sbjNo;
    }

    public void setSbjNo(int sbjNo) {
        this.sbjNo = sbjNo;
    }

    public int getProfNo() {
        return profNo;
    }

    public void setProfNo(int profNo) {
        this.profNo = profNo;
    }
}
