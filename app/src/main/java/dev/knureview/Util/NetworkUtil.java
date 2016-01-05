package dev.knureview.Util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.knureview.VO.AlarmVO;
import dev.knureview.VO.CommentVO;
import dev.knureview.VO.Cookie;
import dev.knureview.VO.LectureVO;
import dev.knureview.VO.StudentVO;
import dev.knureview.VO.SubjectVO;
import dev.knureview.VO.TalkVO;

/**
 * Created by DavidHa on 2015. 11. 21..
 */
public class NetworkUtil {
    private String url;
    private String query;
    private Cookie cookie;

    //학교 서버에서 쿠키값 가져오기
    public Cookie loginSchoolServer(String id, String pw) {
        url = "https://m.kangnam.ac.kr/knusmart/c/c001.do?";
        query = "user_id" + "=" + id + "&" + "user_pwd" + "=" + pw;
        return getCookie(url, query);
    }

    //학교 서버에서 학생 정보 가져오기
    public StudentVO getStudentInfo(Cookie cookie) throws Exception {
        StudentVO vo = new StudentVO();
        url = "https://m.kangnam.ac.kr/knusmart/s/s258.do";
        this.cookie = cookie;
        String data = setCookie(url, null);
        JSONObject stdObject = (JSONObject) new JSONParser().parse(data);
        JSONObject dataObject = (JSONObject) stdObject.get("data");

        //이름
        vo.setName(dataObject.get("mast_name").toString());
        //소속
        vo.setBelong(dataObject.get("mast_unko").toString());
        //전공
        vo.setMajor(dataObject.get("mast_mjor").toString());
        return vo;
    }

    // Member 정보 가져오기
    public StudentVO getExistMemberInfo(String stdNo) throws Exception {
        StudentVO vo = new StudentVO();
        url = "http://kureview.cafe24.com/mobileLookupMember.jsp";
        query = "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        try {
            String result = mainObject.get("member").toString();
            JSONObject memberObject = (JSONObject) mainObject.get("member");
            if (result != null) {
                vo.setStdNo(Integer.parseInt(memberObject.get("stdNo").toString()));
                vo.setName(memberObject.get("name").toString());
                vo.setBelong(memberObject.get("belong").toString());
                vo.setMajor(memberObject.get("major").toString());
                vo.setReviewCnt(Integer.parseInt(memberObject.get("reviewCnt").toString()));
                vo.setReviewAuth(Integer.parseInt(memberObject.get("reviewAuth").toString()));
                vo.setTalkCnt(Integer.parseInt(memberObject.get("talkCnt").toString()));
                vo.setTalkWarning(Integer.parseInt(memberObject.get("talkWarning").toString()));
                vo.setTalkAuth(Integer.parseInt(memberObject.get("talkAuth").toString()));
                vo.setTalkTicket(Integer.parseInt(memberObject.get("talkTicket").toString()));
                vo.setIsExist(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            vo.setIsExist(false);
        }
        return vo;
    }


    public void updateMemberInfo(StudentVO vo) throws Exception {
        url = "http://kureview.cafe24.com/mobileUpdateMember.jsp";
        query = "stdNo" + "=" + vo.getStdNo() + "&" + "name" + "=" + vo.getName() + "&" + "belong" + "=" + vo.getBelong()
                + "&" + "major" + "=" + vo.getMajor();
        sendQuery(url, query);
    }

    public void setMemberInfo(StudentVO vo) throws Exception {
        url = "http://kureview.cafe24.com/mobileInsertMember.jsp";
        query = "stdNo" + "=" + vo.getStdNo()
                + "&" + "name" + "=" + vo.getName()
                + "&" + "belong" + "=" + vo.getBelong()
                + "&" + "major" + "=" + vo.getMajor()
                + "&" + "reviewAuth" + "=" + vo.getReviewAuth()
                + "&" + "talkAuth" + "=" + vo.getTalkAuth()
                + "&" + "talkTicket" + "=" + vo.getTalkTicket()
                + "&" + "regId" + "=" + vo.getRegId()
                + "&" + "key" + "=" + new AES256Util().encrypt(vo.getKey());
        sendQuery(url, query);
    }

    public void updatePushRegId(String stdNo, String regId) throws Exception {
        url = "http://kureview.cafe24.com/mobileUpdatePushRegId.jsp";
        query = "stdNo" + "=" + stdNo + "&" + "regId" + "=" + regId;
        sendQuery(url, query);
    }


    // lecture 에 수강했던 과목 가져오기
    public void setStudentLecture(Cookie cookie, String stdNo) throws Exception {
        //기존에 저장된 lecture 삭제 및 초기화
        url = "http://kureview.cafe24.com/mobileDeleteLecture.jsp";
        query = "stdNo" + "=" + stdNo;
        sendQuery(url, query);

        url = "https://m.kangnam.ac.kr/knusmart/s/s252l.do";
        this.cookie = cookie;
        String data = setCookie(url, null);
        JSONObject schlObject = (JSONObject) new JSONParser().parse(data);
        JSONArray schlArray = (JSONArray) schlObject.get("data");

        //수강했던 학년 & 학기 가져옴
        for (int i = 0; i < schlArray.size(); i++) {
            JSONObject object = (JSONObject) schlArray.get(i);
            String schlYear = object.get("schl_year").toString();
            String schlSmst = object.get("schl_smst").toString();

            //1, 2학기 수강 데이터만 가져옴
            if (Integer.parseInt(schlSmst) < 3) {
                url = "https://m.kangnam.ac.kr/knusmart/s/s252.do?";
                query = "schl_year" + "=" + schlYear + "&" + "schl_smst" + "=" + schlSmst;
                data = setCookie(url, query);
                JSONObject lectObject = (JSONObject) new JSONParser().parse(data);
                JSONArray lectArray = (JSONArray) lectObject.get("data2");

                for (int j = 0; j < lectArray.size(); j++) {
                    JSONObject innerObject = (JSONObject) lectArray.get(j);
                    //수강했던 과목 Lecture Table Insert
                    String subjName = innerObject.get("subj_knam").toString().replace('&', '0');

                    url = "http://kureview.cafe24.com/mobileInsertLecture.jsp";
                    query = "stdNo" + "=" + stdNo + "&" + "year" + "=" + schlYear
                            + "&" + "term" + "=" + schlSmst
                            + "&" + "subjName" + "=" + subjName;
                    sendQuery(url, query);
                }
            }
        }
    }


    public ArrayList<LectureVO> getStudentLecture(String stdNo) throws Exception {
        ArrayList<LectureVO> lectureArray = new ArrayList<LectureVO>();
        url = "http://kureview.cafe24.com/mobileGetLecture.jsp";
        query = "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        //json parsing

        return lectureArray;
    }

    //수강리뷰
    public ArrayList<SubjectVO> getSubjectList(String dName) throws Exception {
        ArrayList<SubjectVO> sbjList = new ArrayList<SubjectVO>();
        url = "http://kureview.cafe24.com/mobileGetSubject.jsp";
        query = "dName" + "=" + dName;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONArray sbjArray = (JSONArray) mainObject.get("subject");
        for (int i = 0; i < sbjArray.size(); i++) {
            JSONObject object = (JSONObject) sbjArray.get(i);
            SubjectVO vo = new SubjectVO();
            vo.setsNo(Integer.parseInt(object.get("sNo").toString()));
            vo.setGrade(Integer.parseInt(object.get("grade").toString()));
            vo.setTerm(Integer.parseInt(object.get("term").toString()));
            vo.setsName(object.get("sName").toString());
            vo.setdNo(Integer.parseInt(object.get("dNo").toString()));
            vo.setCredit(Integer.parseInt(object.get("credit").toString()));
            vo.setTime(Integer.parseInt(object.get("time").toString()));
        }
        return sbjList;
    }


    //소곤소곤

    public ArrayList<TalkVO> getAllTalkList() throws Exception {
        ArrayList<TalkVO> talkList = new ArrayList<TalkVO>();
        url = "http://kureview.cafe24.com/mobileTalkList.jsp";
        String data = getJSON(url, null);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONArray talkArray = (JSONArray) mainObject.get("talk");
        for (int i = 0; i < talkArray.size(); i++) {
            JSONObject object = (JSONObject) talkArray.get(i);
            TalkVO vo = new TalkVO();
            vo.settNo(Integer.parseInt(object.get("tNo").toString()));
            vo.setPictureURL(object.get("pictureURL").toString());
            vo.setStdNo(Integer.parseInt(object.get("stdNo").toString()));
            vo.setDescription(object.get("description").toString());
            vo.setWriteTime(object.get("writeTime").toString());
            vo.setLikeCnt(Integer.parseInt(object.get("likeCnt").toString()));
            vo.setCommentCnt(Integer.parseInt(object.get("commentCnt").toString()));
            talkList.add(vo);
        }
        return talkList;
    }

    public ArrayList<TalkVO> getMyTalkList(String stdNo) throws Exception {
        ArrayList<TalkVO> talkList = new ArrayList<TalkVO>();
        url = "http://kureview.cafe24.com/mobileMyTalkList.jsp";
        query = "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONArray talkArray = (JSONArray) mainObject.get("myTalk");
        for (int i = 0; i < talkArray.size(); i++) {
            JSONObject object = (JSONObject) talkArray.get(i);
            TalkVO vo = new TalkVO();
            vo.settNo(Integer.parseInt(object.get("tNo").toString()));
            vo.setPictureURL(object.get("pictureURL").toString());
            vo.setStdNo(Integer.parseInt(object.get("stdNo").toString()));
            vo.setDescription(object.get("description").toString());
            vo.setWriteTime(object.get("writeTime").toString());
            vo.setLikeCnt(Integer.parseInt(object.get("likeCnt").toString()));
            vo.setCommentCnt(Integer.parseInt(object.get("commentCnt").toString()));
            talkList.add(vo);
        }
        return talkList;
    }

    public TalkVO getAlarmTalk(int tNo, String stdNo) throws Exception {

        url = "http://kureview.cafe24.com/mobileGetAlarmTalk.jsp";
        query = "tNo" + "=" + tNo + "&" + "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONObject object = (JSONObject)mainObject.get("alarmTalk");
        TalkVO vo = new TalkVO();
        vo.settNo(Integer.parseInt(object.get("tNo").toString()));
        vo.setPictureURL(object.get("pictureURL").toString());
        vo.setStdNo(Integer.parseInt(object.get("stdNo").toString()));
        vo.setDescription(object.get("description").toString());
        vo.setWriteTime(object.get("writeTime").toString());
        vo.setLikeCnt(Integer.parseInt(object.get("likeCnt").toString()));
        vo.setCommentCnt(Integer.parseInt(object.get("commentCnt").toString()));
        vo.setIsLike(Integer.parseInt(object.get("isLike").toString()));
        return vo;
    }

    public CommentVO getAlarmComment(int cNo, String stdNo) throws Exception {

        url = "http://kureview.cafe24.com/mobileGetAlarmComment.jsp";
        query = "cNo" + "=" + cNo + "&" + "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONObject object = (JSONObject)mainObject.get("alarmComment");
        CommentVO vo = new CommentVO();
        vo.setCno(Integer.parseInt(object.get("cNo").toString()));
        vo.settNo(Integer.parseInt(object.get("tNo").toString()));
        vo.setPictureURL(object.get("pictureURL").toString());
        vo.setStdNo(Integer.parseInt(object.get("stdNo").toString()));
        vo.setDescription(object.get("description").toString());
        vo.setWriteTime(object.get("writeTime").toString());
        vo.setLikeCnt(Integer.parseInt(object.get("likeCnt").toString()));
        vo.setIsLike(Integer.parseInt(object.get("isLike").toString()));
        return vo;
    }

    public ArrayList<CommentVO> getAllCommentList(int tNo) throws Exception {
        ArrayList<CommentVO> talkList = new ArrayList<CommentVO>();
        url = "http://kureview.cafe24.com/mobileCommentList.jsp";
        query = "tNo" + "=" + tNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONArray cmtArray = (JSONArray) mainObject.get("comment");
        if (cmtArray.size() == 0) {
            return null;
        }
        for (int i = 0; i < cmtArray.size(); i++) {
            JSONObject object = (JSONObject) cmtArray.get(i);
            CommentVO vo = new CommentVO();
            vo.setCno(Integer.parseInt(object.get("cNo").toString()));
            vo.settNo(Integer.parseInt(object.get("tNo").toString()));
            vo.setPictureURL(object.get("pictureURL").toString());
            vo.setStdNo(Integer.parseInt(object.get("stdNo").toString()));
            vo.setDescription(object.get("description").toString());
            vo.setWriteTime(object.get("writeTime").toString());
            vo.setLikeCnt(Integer.parseInt(object.get("likeCnt").toString()));
            talkList.add(vo);
        }
        return talkList;
    }

    public boolean insertTalk(String stdNo, String pictureURL, String description) throws Exception {
        url = "http://kureview.cafe24.com/mobileInsertTalk.jsp";
        query = "stdNo" + "=" + stdNo + "&" + "pictureURL" + "=" + pictureURL
                + "&" + "description" + "=" + description;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        if (mainObject.get("result").toString().equals("success")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean insertComment(String stdNo, String pictureURL, String description, int tNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileInsertComment.jsp";
        query = "stdNo" + "=" + stdNo + "&" + "pictureURL" + "=" + pictureURL
                + "&" + "description" + "=" + description + "&" + "tNo" + "=" + tNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        if (mainObject.get("result").toString().equals("success")) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteTalk(int tNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileDeleteTalk.jsp";
        query = "tNo" + "=" + tNo;
        sendQuery(url, query);
    }

    public void deleteComment(int cNo, int tNo, String stdNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileDeleteComment.jsp";
        query = "cNo" + "=" + cNo
                + "&" + "tNo" + "=" + tNo + "&" + "stdNo" + "=" + stdNo;
        sendQuery(url, query);
    }

    public HashMap<Integer, String> findMyLikeTalk(String stdNo) throws Exception {
        HashMap<Integer, String> likeHashMap = new HashMap<>();
        url = "http://kureview.cafe24.com/mobileFindMyLikeTalk.jsp";
        query = "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONArray likeArray = (JSONArray) mainObject.get("myLikeTalk");

        if (likeArray.size() > 0) {
            for (int i = 0; i < likeArray.size(); i++) {
                likeHashMap.put(Integer.parseInt(likeArray.get(i).toString()), "like");
            }
            return likeHashMap;
        } else {
            return null;
        }
    }

    public HashMap<Integer, String> findMyLikeComment(String stdNo) throws Exception {
        HashMap<Integer, String> likeHashMap = new HashMap<>();
        url = "http://kureview.cafe24.com/mobileFindMyLikeComment.jsp";
        query = "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONArray likeArray = (JSONArray) mainObject.get("myLikeComment");

        if (likeArray.size() > 0) {
            for (int i = 0; i < likeArray.size(); i++) {
                likeHashMap.put(Integer.parseInt(likeArray.get(i).toString()), "like");
            }
            return likeHashMap;
        } else {
            return null;
        }
    }

    public void increaseTalkLike(int tNo, String stdNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileIncreaseLike.jsp";
        query = "tNo" + "=" + tNo + "&" + "stdNo" + "=" + stdNo + "&" + "isComment" + "=" + 0;
        sendQuery(url, query);
    }

    public void increaseCommentLike(int cNo, String stdNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileIncreaseLike.jsp";
        query = "cNo" + "=" + cNo + "&" + "stdNo" + "=" + stdNo + "&" + "isComment" + "=" + 1;
        sendQuery(url, query);
    }

    public void decreaseTalkLike(int tNo, String stdNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileDecreaseLike.jsp";
        query = "tNo" + "=" + tNo + "&" + "stdNo" + "=" + stdNo + "&" + "isComment" + "=" + 0;
        sendQuery(url, query);
    }

    public void decreaseCommentLike(int cNo, String stdNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileDecreaseLike.jsp";
        query = "cNo" + "=" + cNo + "&" + "stdNo" + "=" + stdNo + "&" + "isComment" + "=" + 1;
        sendQuery(url, query);
    }

    //profile
    public void sendMemberTicket(String sendStdNo, String receiveStdNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileUpdateTicketCnt.jsp";
        StudentVO vo = new StudentVO();
        query = "sendStdNo" + "=" + sendStdNo + "&" + "receiveStdNo" + "=" + receiveStdNo +
                "&" + "key" + "=" + new AES256Util().encrypt(vo.getKey());
        sendQuery(url, query);
    }

    public void updateMemberTicketAuth(String stdNo) throws Exception {
        url = "http://kureview.cafe24.com/mobileUpdateTicketAuth.jsp";
        query = "stdNo" + "=" + stdNo;
        sendQuery(url, query);
    }

    //alarm
    public ArrayList<AlarmVO> getAlarmList(String stdNo) throws Exception {
        ArrayList<AlarmVO> alarmList = new ArrayList<AlarmVO>();
        url = "http://kureview.cafe24.com/mobileAlarmList.jsp";
        query = "stdNo" + "=" + stdNo;
        String data = getJSON(url, query);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        JSONArray talkArray = (JSONArray) mainObject.get("alarm");
        for (int i = 0; i < talkArray.size(); i++) {
            JSONObject object = (JSONObject) talkArray.get(i);
            AlarmVO vo = new AlarmVO();
            vo.setaNo(Integer.parseInt(object.get("aNo").toString()));
            vo.setR_stdNo(Integer.parseInt(object.get("r_stdNo").toString()));
            vo.setS_stdNo(Integer.parseInt(object.get("s_stdNo").toString()));
            vo.setWriteTime(object.get("writeTime").toString());
            vo.settNo(Integer.parseInt(object.get("tNo").toString()));
            vo.setcNo(Integer.parseInt(object.get("cNo").toString()));
            vo.setPictureURL(object.get("pictureURL").toString());
            vo.setDescription(object.get("description").toString());
            vo.setIsLike(Integer.parseInt(object.get("isLike").toString()));
            alarmList.add(vo);
        }
        return alarmList;
    }


    //version
    public String[] getLatestVersion() throws Exception {
        String[] versionInfo = new String[2];
        url = "http://kureview.cafe24.com/mobileVersion.jsp";
        String data = getJSON(url, null);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        versionInfo[0] = mainObject.get("version").toString();
        versionInfo[1] = mainObject.get("update").toString();
        return versionInfo;
    }

    //picture count
    public int getPictureCnt() throws Exception {
        int pictureCnt = 0;
        url = "http://kureview.cafe24.com/mobilePictureCnt.jsp";
        String data = getJSON(url, null);
        JSONObject mainObject = (JSONObject) new JSONParser().parse(data);
        pictureCnt = Integer.parseInt(mainObject.get("pictureCnt").toString());
        return pictureCnt;
    }

    // NetWork Util Method
    private void sendQuery(String url, String query) throws Exception {

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(query);
        wr.flush();
        conn.connect();
        conn.getInputStream();
    }

    private String getJSON(String url, String query) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.connect();

            if (query != null) {
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(query);
                wr.flush();
                conn.connect();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String setCookie(String url, String query) {
        if (query != null) {
            url += query;
        }
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            request.addRequestProperty("Cookie", cookie.getJsession() + ";" + cookie.getName() + ";" +
                    cookie.getMjco() + ";" + cookie.getAuto() + ";" + cookie.getPass() + ";" +
                    cookie.getName_e() + ";" + cookie.getGubn() + ";" + cookie.getIdno() + ";");
            request.setUseCaches(false);
            request.setDoOutput(true);
            request.setDoInput(true);
            HttpURLConnection.setFollowRedirects(true);
            request.setRequestMethod("GET");
            request.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();

            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Cookie getCookie(String url, String query) {
        Cookie vo = new Cookie();
        // url += query;
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            request.setUseCaches(false);
            request.setDoOutput(true);
            request.setDoInput(true);
            HttpURLConnection.setFollowRedirects(true);
            request.setInstanceFollowRedirects(true);
            request.setRequestMethod("GET");
            request.connect();

            OutputStreamWriter wr = new OutputStreamWriter(request.getOutputStream());
            wr.write(query);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();

            String data = builder.toString();
            JSONObject loginJsonObject = (JSONObject) new JSONParser().parse(data);
            vo.setLoginResult(loginJsonObject.get("result").toString());

            List<String> cookies = request.getHeaderFields().get("Set-Cookie");
            vo.setIdno(cookies.get(0));
            vo.setGubn(cookies.get(1));
            vo.setName(cookies.get(2));
            vo.setPass(cookies.get(3));
            vo.setAuto(cookies.get(4));
            vo.setMjco(cookies.get(5));
            vo.setName_e(cookies.get(6));
            vo.setJsession(cookies.get(7));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }
}
