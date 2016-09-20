package com.itsnowball.aicomposer;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements MylistEndlessListView.EndlessListener {

    private final static int ITEM_PER_REQUEST = 10;
    private SwipeRefreshLayout mSwipeRefresh;
    public MylistEndlessListView lv;
    public MylistListViewAdapter adp;

    int mult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        getSupportActionBar().setTitle(getString(R.string.mylist_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Toast.makeText(getContext(), "Refresh Timeline Fragment", Toast.LENGTH_SHORT).show();
        lv = (MylistEndlessListView)findViewById(R.id.mel);

        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "Refresh Executed", Toast.LENGTH_SHORT).show();
                System.out.println("Refresh Data");

                // Refresh 되는 데이터는 서버에서 받아서 처리해주어야 한다.
                // 현재는 값을 초기화하는 용도로만 구현되어 있고 추후 다시 구현해주어야 한다.
                lv.refreshData(createItems(mult));
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));

        adp = new MylistListViewAdapter(this, createItems(mult), R.layout.mylist_item_card);
        lv.setAdapter(adp);
        lv.setListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                List<MyListItem> temp = new ArrayList<MyListItem>();
                temp.add((MyListItem)lv.getAdapter().getItem(position));
                MyListItem temp2 = temp.get(0);

                String details_musicpath = temp2.getMusicPath();
                String details_musictitle = temp2.getMusicTitle();

                Toast.makeText(getApplicationContext(),
                        "path : " + details_musicpath + ", title : " + details_musictitle,
                        Toast.LENGTH_SHORT).show();
            }
        });
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

    // ListView 기능 테스트를 위한 FakeNetLoader Class
    private class FakeNetLoader extends AsyncTask<MyListItem, Void, List<MyListItem>> {

        @Override
        protected List<MyListItem> doInBackground(MyListItem... params) {
            return createItems(mult);
        }

        @Override
        protected void onPostExecute(List<MyListItem> result) {
            super.onPostExecute(result);
            lv.addNewData(result);
        }
    }

    private List<MyListItem> createItems(int mult) {
        List<MyListItem> result = new ArrayList<MyListItem>();
        MyListItem temp;

        String mylistURL = URLData.defaultURL + URLData.cmpstnDir + URLData.compositionListAPI;
        try {
            String response = new HttpUtils(6, mylistURL, UserData.userID, "", "", "", "", "", "", "", "", "", "", "", "").execute().get();
            JSONObject job = new JSONObject(response);
            String error_code = job.getString("resultCode");
            if(error_code.equals("3246")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyListActivity.this);
                builder.setTitle(getString(R.string.post_nomusic_title));
                builder.setCancelable(true);
                builder.setMessage(getString(R.string.post_nomusic));
                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();

            } else if (error_code.equals("3240")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyListActivity.this);
                builder.setTitle(getString(R.string.login_error_3040_title));
                builder.setCancelable(true);
                builder.setMessage(getString(R.string.login_error_3040));
                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            } else {
                String listarray = job.getString("resultData");
                JSONArray jar = new JSONArray(listarray);
                for (int i = 0; i < jar.length(); ++i) {
                    temp = new MyListItem();

                    JSONObject item = jar.getJSONObject(i);
                    temp.setMusicTitle(item.getString("compositionTitle"));
                    temp.setMusicPath(item.getString("compositionMusicUrl"));

                    result.add(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void loadData() {
        System.out.println("Load data");
        mult += 1;
        // We load more data here
        FakeNetLoader fl = new FakeNetLoader();
        fl.execute(new MyListItem[]{});

    }
}
