package com.itsnowball.aicomposer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends Activity {
    private Toast toast;

    private TextView joinText;
    private TextView normalloginButton;

    private EditText id_input;
    private EditText pw_input;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private long backKeyPressedTime = 0;
    public static Activity mainActivity;

    public ProfileTracker profileTracker;
    public String user_id;

    String dialog_msg = "";
    String dialog_title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;

        joinText = (TextView) findViewById(R.id.textView3);
        normalloginButton = (Button) findViewById(R.id.normalLoginBtn);

        id_input = (EditText) findViewById(R.id.et_id);
        pw_input = (EditText) findViewById(R.id.et_pw);

        normalloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SociallistActivity.class);

                String url = URLData.defaultURL + URLData.userDir + URLData.loginAPI;
                String id = id_input.getText().toString();
                String pw = pw_input.getText().toString();

                if(id.equals("") || pw.equals("")) {
                    Toast.makeText(MainActivity.this, "아이디 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Log.d("TAG : ", "HTTPUTILS LAUNCH");
                        String result = new HttpUtils(1, url, id, pw, "", "", "", "", "", "", "", "", "", "", "").execute().get();
                        if (result == null) {
                            //Timeout
                            Toast.makeText(MainActivity.this, getString(R.string.server_connection_error), Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject job = new JSONObject(result);
                            String resultCode = job.getString("resultCode");

                            if (resultCode.equals("2300")) {
                                String resultData = job.getString("resultData");
                                JSONObject job2 = new JSONObject(resultData);

                                UserData.userID = job2.getString("userId");
                                UserData.userEmail = job2.getString("userEmail");
                                UserData.userNickname = job2.getString("userNickName");

                                startActivity(intent);
                                finish();
                            } else {
                                switch(resultCode) {
                                    case "3040":
                                        displayDialog(getString(R.string.login_error_3040_title), getString(R.string.login_error_3040));
                                        break;
                                    case "3041":
                                        displayDialog(getString(R.string.login_error_3041_title), getString(R.string.login_error_3041));
                                        break;
                                    case "3042":
                                        displayDialog(getString(R.string.login_error_3042_title), getString(R.string.login_error_3042));
                                        break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        joinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Facebook Login SDK
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.facebookJoinBtn);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String access_token = loginResult.getAccessToken().getToken();
                Profile profile = Profile.getCurrentProfile();
                Log.e("TAG", profile + "");

                if(profile == null) {
                    Toast.makeText(MainActivity.this, "Profile is Null", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                } else {
                    user_id = profile.getId();
                    String user_id_store = user_id + "_facebook";

                    Log.e("TAG", "user_id : " + Profile.getCurrentProfile().getId());

                    // LoginURL = Default + login API
                    String loginURL = URLData.defaultURL + URLData.userDir + URLData.loginAPI;

                    // Login Success Check
                    try {
                        Log.d("FacebookLogin : ", "user_id : " + user_id + ", password : " + "");

                        // Get Result Value
                        try {
                            String result = new HttpUtils(1, loginURL, user_id_store, user_id, "", "", "", "", "", "", "", "", "", "", "").execute("").get();
                            if (result == null) {
                                Toast.makeText(getApplicationContext(), "TimeOut", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("NO TIMEOUT : ", "TIME OUT은 아닙니다.");
                                JSONObject job = new JSONObject(result);
                                String resultCode = job.getString("resultCode");
                                String resultData = job.getString("resultData");

                                switch (resultCode) {
                                    case "3040":
                                        dialog_msg = getString(R.string.login_error_3040);
                                        dialog_title = getString(R.string.login_error_3040_title);
                                        displayDialog(dialog_title, dialog_msg);
                                        break;
                                    case "3041":
                                        dialog_msg = getString(R.string.login_error_3041);
                                        dialog_title = getString(R.string.login_error_3041_title);
                                        displayDialog(dialog_title, dialog_msg);
                                        break;
                                    case "3042":
                                        //dialog_msg = getString(R.string.login_error_3042);
                                        //dialog_title = getString(R.string.login_error_3042_title);
                                        Toast.makeText(MainActivity.this, getString(R.string.welcome_social), Toast.LENGTH_SHORT).show();
                                        goJoinScreen(user_id);
                                        break;
                                    case "2300":
                                        JSONObject job2 = new JSONObject(resultData);
                                        String nickname = job2.getString("userNickName");
                                        String email = job2.getString("userEmail");
                                        UserData.userID = job2.getString("userId");
                                        UserData.userEmail = job2.getString("userEmail");
                                        UserData.userNickname = job2.getString("userNickName");
                                        String received_user_id = job2.getString("userId");
                                        goMainScreen(access_token, received_user_id, nickname, email);
                                        finish();
                                        break;
                                }
                            }
                            //Toast.makeText(getApplicationContext(), "Result : " + result, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Canceled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Facebook Login Error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDialog(String title, String contents) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setMessage(contents);
        builder.setPositiveButton(R.string.dialog_ok, null);
        builder.show();
    }

    private void goJoinScreen(String facebook_id) {
        // 서버에 해당 토큰이 존재하는지 체크한 후 존재하면 메인으로 존재하지 않으면 회원 가입 화면으로 이동시킨다
        // 이 부분은 추후 추가 구현될 예정이다

        Intent intent = new Intent(this, SignUpSocialActivity.class);
        intent.putExtra("facebook_id", facebook_id);
        startActivity(intent);
    }

    private void goMainScreen(String token, String user_id, String user_nick, String email) {
        // 서버에 해당 토큰이 존재하는지 체크한 후 존재하면 메인으로 존재하지 않으면 회원 가입 화면으로 이동시킨다
        // 이 부분은 추후 추가 구현될 예정이다

        Intent intent = new Intent(this, SociallistActivity.class);
        intent.putExtra("accessToken", token);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_email", email);
        intent.putExtra("user_nickname", user_nick);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
                toast.cancel();
            }
    }

    public void showGuide() {
        toast = Toast.makeText(MainActivity.this,
                R.string.toast_exit_msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) {
            v.setGravity(Gravity.CENTER);
            v.setTextSize(13);
        }
        toast.show();
    }
}
