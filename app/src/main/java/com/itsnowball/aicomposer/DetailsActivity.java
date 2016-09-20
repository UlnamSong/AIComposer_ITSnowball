package com.itsnowball.aicomposer;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {

    private String email = "";
    private String nickname = "";
    private String contents = "";
    private int favoriteCnt = 0;
    private String musicPath = "";

    private Button downloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setTitle(R.string.details_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        contents = intent.getStringExtra("contents");
        musicPath = intent.getStringExtra("musicpath");
        favoriteCnt = intent.getIntExtra("favoriteCnt", 0);

        downloadBtn = (Button) findViewById(R.id.details_downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle(R.string.dialog_title_download);
                builder.setCancelable(true);
                builder.setMessage(R.string.download_msg);
                builder.setPositiveButton(R.string.dialog_continue, null);
                builder.setNegativeButton(R.string.dialog_cancel, null);
                builder.show();
            }
        });

        ListView listview ;
        DetailListViewAdapter adapter;

        // Adapter 생성
        adapter = new DetailListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        TextView textview = (TextView) findViewById(R.id.textView11);
        textview.setText(contents);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.details_nickname),
                getString(R.string.details_nickname), nickname) ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.details_theme),
                getString(R.string.details_theme), "여행을 떠날 때") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.details_favorite),
                getString(R.string.details_favoriteCnt), String.valueOf(favoriteCnt)) ;

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
