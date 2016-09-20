package com.itsnowball.aicomposer;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MakeMusicActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;

    private SeekBar seekBar2;
    private TextView textView2;

    private EditText titleView;

    private String music_title = "";
    private int beat_stage = 0;
    private int pitch_percent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_music);

        getSupportActionBar().setTitle(R.string.compose);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        textView = (TextView) findViewById(R.id.textView18);

        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        textView2 = (TextView) findViewById(R.id.textView24);

        titleView = (EditText) findViewById(R.id.editText3);

        // Initialize the textview with '0'
        textView.setText((seekBar.getProgress() + 1) + " " + getString(R.string.music_dangye)
                + " " + getString(R.string.music_jung) + " " + (seekBar.getMax() + 1)
                + " " + getString(R.string.music_dangye));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progresValue, boolean fromUser) {
                progress = progresValue;
                textView.setText((progress + 1) + " " + getString(R.string.music_dangye)
                        + " " + getString(R.string.music_jung) + " " + (seekBar.getMax() + 1)
                        + " " + getString(R.string.music_dangye));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                //if you want to do anything at the start of
                // touching the seekbar
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Display the value in textview
                textView.setText((progress + 1) + " " + getString(R.string.music_dangye)
                        + " " + getString(R.string.music_jung) + " " + (seekBar.getMax() + 1)
                        + " " + getString(R.string.music_dangye));

                beat_stage = progress + 1;
            }
        });

        textView2.setText(seekBar.getProgress() + " %");
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progresValue, boolean fromUser) {
                progress = progresValue;
                textView2.setText(progress + " %");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                //if you want to do anything at the start of
                // touching the seekbar
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Display the value in textview
                textView2.setText(progress + " %");
                pitch_percent = progress;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.make_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.make_music:
                showDialog();
                return true;
            default:
                return false;
        }
    }

    private void showDialog() {
        music_title = titleView.getText().toString();
        if(music_title.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.compose_dialogerror_title));
            builder.setCancelable(true);
            builder.setMessage(getString(R.string.compose_dialogerror));
            builder.setPositiveButton(getString(R.string.dialog_ok), null);
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.compose_dialogtitle));
            builder.setCancelable(true);
            builder.setMessage(getString(R.string.compose_dialogcontent) + "\n\n" + getString(R.string.compose_dialog_musictitle) + " " + music_title + "\n" +
                    getString(R.string.compose_dialog_musicbeat) + " " + beat_stage + getString(R.string.music_dangye) + "\n" +
                    getString(R.string.compose_dialog_musicpitch) + " " + pitch_percent + " %");
            builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String makeMusicURL = URLData.defaultURL + URLData.cmpstnDir + URLData.compositionAPI;
                    //new HttpUtils(5, makeMusicURL, "", "", "", "", "", "", "", "", "", "", "", "", "").execute();

                    Toast toast = Toast.makeText(MakeMusicActivity.this, getString(R.string.compose_complete_message), Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);

                    if( v != null) {
                        v.setGravity(Gravity.CENTER);
                        v.setTextSize(13);
                    }
                    toast.show();
                    finish();
                }
            });
            builder.setNegativeButton(R.string.dialog_back, null);
            builder.show();
        }
    }
}
