package com.itsnowball.aicomposer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PostActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> adspin;
    ArrayList<String> arraylist;
    ArrayList<String> musicURL;
    ArrayList<String> arraylist_theme;

    int theme_index = 0;
    int music_index = 0;

    boolean no_music = false;

    public EditText content_et;

    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().setTitle(R.string.write_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");

        arraylist = new ArrayList<String>();
        musicURL = new ArrayList<String>();

        content_et = (EditText) findViewById(R.id.editText4);

        String post_composeURL = URLData.defaultURL + URLData.cmpstnDir + URLData.compositionListAPI;

        // Composition List 불러오기
        try {
            String response = new HttpUtils(6, post_composeURL, userID, "", "", "", "", "", "", "", "", "", "", "", "").execute().get();
            Log.i("POST RESPONSE : ", response);
            JSONObject job = new JSONObject(response);
            String listarray = job.getString("resultData");
            String error_code = job.getString("resultCode");
            if(!error_code.equals("3246")) {
                no_music = false;
                JSONArray jar = new JSONArray(listarray);
                for (int i = 0; i < jar.length(); ++i) {
                    JSONObject item = jar.getJSONObject(i);
                    arraylist.add(item.getString("compositionTitle"));
                    musicURL.add(item.getString("compositionMusicUrl"));
                }
            } else {
                no_music = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                builder.setTitle(getString(R.string.post_nomusic_title));
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.post_nomusic));
                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String[] arrString = getResources().getStringArray(R.array.music_theme);
        arraylist_theme = new ArrayList<String>();
        for(String str : arrString) {
            arraylist_theme.add(str);
        }

        // Spinner 1
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraylist);
        //스피너 속성
        Spinner sp = (Spinner) this.findViewById(R.id.spinner);
        sp.setPrompt(getString(R.string.post_musicselect)); // 스피너 제목
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(PostActivity.this, arraylist.get(i), Toast.LENGTH_LONG).show();
                music_index = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                music_index = 0;
            }
        });


        // Spinner 2
        ArrayAdapter<String> adapter_theme = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraylist_theme);
        //스피너 속성
        Spinner sp_theme = (Spinner) this.findViewById(R.id.spinner2);
        sp_theme.setPrompt(getString(R.string.post_themeselect)); // 스피너 제목
        sp_theme.setAdapter(adapter_theme);
        sp_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(PostActivity.this, arraylist_theme.get(i), Toast.LENGTH_LONG).show();
                theme_index = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                theme_index = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // doing something
                finish();
                return true;
            case R.id.post_write:
                String url = URLData.defaultURL + URLData.boardDir + URLData.boardWriteAPI;
                String contents = content_et.getText().toString();
                theme_index += 1;
                String theme = theme_index + "";
                try {
                    String response = new HttpUtils(4, url, UserData.userID, "", UserData.userNickname, "", "", contents, musicURL.get(music_index), "", "", theme, "", "", "").execute("").get();
                    //Log.d("POST_SEND", UserData.userID + ", " + UserData.userNickname + ", " + contents + ", " + musicURL.get(music_index) + ", " + theme);
                    //Log.d("RESPONSE", response);

                    JSONObject job = new JSONObject(response);
                    String resultCode = job.getString("resultCode");

                    switch(resultCode) {
                        case "2304":
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                            builder.setTitle(getString(R.string.post_success_title));
                            builder.setCancelable(false);
                            builder.setMessage(getString(R.string.post_success));
                            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                            builder.show();
                            break;
                        case "3147":
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PostActivity.this);
                            builder1.setTitle(getString(R.string.post_fail_3147_title));
                            builder1.setCancelable(false);
                            builder1.setMessage(getString(R.string.post_fail_3147));
                            builder1.setPositiveButton(R.string.dialog_ok, null);
                            builder1.show();
                            break;
                        case "3140":
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(PostActivity.this);
                            builder2.setTitle(getString(R.string.post_fail_3140_title));
                            builder2.setCancelable(false);
                            builder2.setMessage(getString(R.string.post_fail_3140));
                            builder2.setPositiveButton(R.string.dialog_ok, null);
                            builder2.show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return false;
        }
    }
}
