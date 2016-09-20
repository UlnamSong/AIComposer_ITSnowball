package com.itsnowball.aicomposer;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

public class SignUpActivity extends AppCompatActivity {

    private TextView agreement_textView;
    private TextView personal_textView;

    private ImageButton email_ib;
    private ImageButton nickname_ib;
    private ImageButton password_ib;
    private ImageButton passwordConfirm_ib;

    public EditText email_et;
    public EditText nickname_et;
    public EditText password_et;
    public EditText passwordConfirm_et;

    public CheckBox agree_check;

    public String email;
    public String password;
    public String password_confirm;
    public String nickname;

    private Button signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        agreement_textView = (TextView) findViewById(R.id.agreement_tv);
        personal_textView = (TextView) findViewById(R.id.personal_tv);

        nickname_ib = (ImageButton) findViewById(R.id.nickname_ib);
        email_ib = (ImageButton) findViewById(R.id.email_ib);
        password_ib = (ImageButton) findViewById(R.id.password_ib);
        passwordConfirm_ib = (ImageButton) findViewById(R.id.passwordConfirm_ib);

        signup_button = (Button) findViewById(R.id.button);
        agree_check = (CheckBox) findViewById(R.id.checkBox);

        email_et = (EditText) findViewById(R.id.email_et);
        nickname_et = (EditText) findViewById(R.id.nickname_et);
        password_et = (EditText) findViewById(R.id.password_et);
        passwordConfirm_et = (EditText) findViewById(R.id.passwordConfirm_et);

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

        password_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password_et.setText("");
            }
        });

        passwordConfirm_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordConfirm_et.setText("");
            }
        });

        agreement_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });

        personal_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_et.getText().toString();
                password = password_et.getText().toString();
                password_confirm = passwordConfirm_et.getText().toString();
                nickname = nickname_et.getText().toString();

                String url = URLData.defaultURL + URLData.userDir + URLData.joinAPI;

                if(email.equals("") || password.equals("") || password_confirm.equals("") || nickname.equals("")) {
                    // Input Field is blank.
                    displayDialog(getString(R.string.signup_input_error_title), getString(R.string.signup_input_error));
                } else if(!password.equals(password_confirm)) {
                    // Password not equal.
                    displayDialog(getString(R.string.signup_password_equal_error_title), getString(R.string.signup_password_equal_error));
                } else if(!agree_check.isChecked()) {
                    // CheckBox is not Checked.
                    displayDialog(getString(R.string.signup_check_title), getString(R.string.signup_check));
                } else {
                    // Success, Request to Server
                    try {
                        String result = new HttpUtils(2, url, email, password, nickname, "", "", "", "", "", "", "", "", "", "").execute().get();

                        if (result == null) {
                            //Timeout
                            Toast.makeText(SignUpActivity.this, getString(R.string.server_connection_error), Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject job = new JSONObject(result);
                            String resultCode = job.getString("resultCode");
                            String resultMessage = job.getString("resultMsg");

                            switch(resultCode) {
                                case "2301":
                                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case "3040":
                                    displayDialog(getString(R.string.login_error_3040_title), getString(R.string.login_error_3040));
                                    break;
                                case "3044":
                                    displayDialog(getString(R.string.signup_nick_error_title), getString(R.string.signup_nick_error));
                                    break;
                                case "3043":
                                    displayDialog(getString(R.string.signup_id_error_title), getString(R.string.signup_id_error));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
                finish();
                return true;
            default:
                return false;
        }
    }
}
