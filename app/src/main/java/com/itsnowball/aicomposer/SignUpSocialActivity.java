package com.itsnowball.aicomposer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.login.LoginManager;

import org.json.JSONObject;

/**
 * Created by Luny on 2016. 8. 8..
 */
public class SignUpSocialActivity extends AppCompatActivity {
    private TextView agreement_textView;
    private TextView personal_textView;

    private ImageButton email_ib;
    private ImageButton nickname_ib;

    public CheckBox agree_check;

    public EditText email_et;
    public EditText nickname_et;

    public String facebook_id = "";
    public String real_facebook_id = "";

    private Button SignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_social);

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id") + "_facebook";
        real_facebook_id = intent.getStringExtra("facebook_id");

        agreement_textView = (TextView) findViewById(R.id.agreement_tv);
        personal_textView = (TextView) findViewById(R.id.personal_tv);

        nickname_ib = (ImageButton) findViewById(R.id.nickname_ib);
        email_ib = (ImageButton) findViewById(R.id.email_ib);

        email_et = (EditText) findViewById(R.id.email_et);
        nickname_et = (EditText) findViewById(R.id.nickname_et);

        email_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_et.setText("");
            }
        });

        nickname_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname_et.setText("");
            }
        });

        agree_check = (CheckBox) findViewById(R.id.checkBox);
        SignupButton = (Button) findViewById(R.id.button);

        agreement_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpSocialActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });

        personal_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpSocialActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!agree_check.isChecked()) {
                    displayDialog(getString(R.string.social_signup_check_title), getString(R.string.social_signup_check));
                } else if(email_et.getText().toString().equals("") || nickname_et.getText().toString().equals("")) {
                    displayDialog(getString(R.string.social_signup_input_error_title), getString(R.string.social_signup_input_error));
                } else {
                    String url = URLData.defaultURL + URLData.userDir + URLData.socialJoinAPI;

                    String id = email_et.getText().toString();
                    String nick = nickname_et.getText().toString();

                    try {
                        Log.d("TAG : ", "HTTPUTILS LAUNCH");
                        Log.e("TAG : ", facebook_id + ", " + real_facebook_id + ", " + nick + ", " + id);
                        String result = new HttpUtils(21, url, facebook_id, real_facebook_id, nick, id, "", "", "", "", "", "", "", "", "").execute().get();
                        if (result == null) {
                            //Timeout
                            Toast.makeText(SignUpSocialActivity.this, getString(R.string.server_connection_error), Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject job = new JSONObject(result);
                            String resultCode = job.getString("resultCode");
                            String resultMessage = job.getString("resultMsg");

                            switch(resultCode) {
                                case "2301":
                                    Toast.makeText(SignUpSocialActivity.this, getString(R.string.social_signup_success), Toast.LENGTH_SHORT).show();
                                    LoginManager.getInstance().logOut();
                                    finish();
                                    break;
                                case "3040":
                                    displayDialog(getString(R.string.login_error_3040_title), getString(R.string.login_error_3040));
                                    break;
                                case "3044":
                                    displayDialog(getString(R.string.signup_nick_error_title), getString(R.string.signup_nick_error));
                                    break;
                            }

                            // Test Code
                            //Toast.makeText(SignUpSocialActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        getSupportActionBar().setTitle(R.string.signup_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void displayDialog(String title, String contents) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpSocialActivity.this);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setMessage(contents);
        builder.setPositiveButton(R.string.dialog_ok, null);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // doing something
                LoginManager.getInstance().logOut();
                finish();

                return true;
            default:
                return false;
        }
    }
}
