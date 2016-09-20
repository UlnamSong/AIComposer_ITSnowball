package com.itsnowball.aicomposer;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Luny on 2016. 8. 25..
 */
public class HttpUtils extends AsyncTask<String, Void, String> {
    // mode : 0 ( Default )
    // mode : 1 ( Login )
    // mode : 2 ( Join )
    // mode : 21 ( Social Join )
    // mode : 3 ( Main )
    // mode : 31 ( Main - Like Click )
    // mode : 4 ( Post )
    // mode : 5 ( Compose )
    // mode : 6 ( MyList )
    // mode : 7 ( User Info )
    int mode = 0;
    String url = "", id = "", pw = "", nick = "", email = "";
    String startIndex = "", contents = "", music_addr = "", music_name = "", motive_name = "", theme = "";
    String genre = "", speed = "", pitch = "";

    boolean likeToggle;
    String board_no = "";

    String response;

    HttpUtils(boolean like, String id, String board_no) {
        this.likeToggle = like;
        this.id = id;
        this.board_no = board_no;
    }

    HttpUtils(int mode, String url, String id, String pw, String nick, String email,
              String startIndex, String contents, String music_addr, String music_name, String motive_name, String theme,
              String genre, String speed, String pitch) {
        this.id = id;
        this.pw = pw;
        this.nick = nick;
        this.email = email;
        this.startIndex = startIndex;
        this.contents = contents;
        this.music_addr = music_addr;
        this.music_name = music_name;
        this.motive_name = motive_name;
        this.theme = theme;
        this.genre = genre;
        this.speed = speed;
        this.pitch = pitch;
        this.url = url;
        this.mode = mode;
    }

    @Override
    public String doInBackground(String... params) {
        OutputStream          os   = null;
        InputStream           is   = null;
        ByteArrayOutputStream baos = null;

        Log.i("TAG", HttpURLConnection.HTTP_OK + "");

        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject data = new JSONObject();

            switch(mode) {
                case 1:
                    data.put("user_id", id);
                    data.put("user_pass", pw);
                    break;
                case 2:
                    data.put("user_id", id);
                    data.put("user_pass", pw);
                    data.put("user_nick_name", nick);
                    break;
                case 21:
                    data.put("user_id", id);
                    data.put("user_pass", pw);
                    data.put("user_nick_name", nick);
                    data.put("user_email", email);
                    break;
                case 3:
                    data.put("user_id", id);
                    break;
                case 31:
                    data.put("user_id", id);
                    data.put("board_no", pw);
                    break;
                case 4:
                    data.put("user_id", id);
                    data.put("user_nick_name", nick);
                    data.put("board_music_url", music_addr);
                    data.put("board_content", contents);
                    data.put("board_theme", theme);
                    break;
                case 6:
                    data.put("user_id", id);
                    break;
                case 7:
                    data.put("user_id", id);
                    break;
            }

            os = conn.getOutputStream();
            os.write( data.toString().getBytes() );
            os.close();

            Log.i("TAG", "Before Get Code");

            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                response = new String(byteData);
                Log.i("TAG", "DATA response = " + response);
            } else {
                Log.i("TAG", "DATA response = ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}