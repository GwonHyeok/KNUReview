package dev.knureview.Util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import dev.knureview.VO.Cookie;

/**
 * Created by DavidHa on 2015. 11. 21..
 */
public class NetworkUtil {
    private String url;
    private String query;

    public Cookie loginSchoolServer(String id, String pw) {
        url = "https://m.kangnam.ac.kr/knusmart/c/c001.do?";
        query = "user_id" + "=" + id + "&" + "user_pwd" + "=" + pw;
        return getCookie(url, query);
    }

    private Cookie getCookie(String url, String query) {
        Cookie vo = new Cookie();
        url += query;
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
