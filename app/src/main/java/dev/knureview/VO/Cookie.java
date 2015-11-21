package dev.knureview.VO;

/**
 * Created by DavidHa on 2015. 11. 21..
 */
public class Cookie {
    private String loginResult;
    private String idno;
    private String gubn;
    private String name;
    private String pass;
    private String auto;
    private String mjco;
    private String name_e;
    private String jsession;

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getGubn() {
        return gubn;
    }

    public void setGubn(String gubn) {
        this.gubn = gubn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getMjco() {
        return mjco;
    }

    public void setMjco(String mjco) {
        this.mjco = mjco;
    }

    public String getName_e() {
        return name_e;
    }

    public void setName_e(String name_e) {
        this.name_e = name_e;
    }

    public String getJsession() {
        return jsession;
    }

    public void setJsession(String jsession) {
        this.jsession = jsession;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "loginResult='" + loginResult + '\'' +
                ", idno='" + idno + '\'' +
                ", gubn='" + gubn + '\'' +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", auto='" + auto + '\'' +
                ", mjco='" + mjco + '\'' +
                ", name_e='" + name_e + '\'' +
                ", jsession='" + jsession + '\'' +
                '}';
    }
}
