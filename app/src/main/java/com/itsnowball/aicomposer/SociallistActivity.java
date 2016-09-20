package com.itsnowball.aicomposer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class SociallistActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EndlessListView.EndlessListener {

    private final static int ITEM_PER_REQUEST = 10;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    private TextView nickname_tv;
    private TextView email_tv;
    private Toast toast;
    private long backKeyPressedTime = 0;

    private SwipeRefreshLayout mSwipeRefresh;
    public EndlessListView lv;
    public EndlessAdapter adp;
    public static Activity sociallistActivity;

    int mult = 0;

    private String userNickname = "";
    private String userEmail = "";
    private String accessToken = "";
    private String userID = "";
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sociallist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.sociallist_title);

        sociallistActivity = SociallistActivity.this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_sociallist);
        navigationView.setNavigationItemSelectedListener(this);

        // Receive Access Token from Facebook SDK.
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        userID = UserData.userID;
        userNickname = UserData.userNickname;
        userEmail = UserData.userEmail;

        nickname_tv = (TextView) headerView.findViewById(R.id.nickname_nav);
        email_tv = (TextView) headerView.findViewById(R.id.email_nav);

        nickname_tv.setText(userNickname);
        email_tv.setText(userEmail);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        // Make Music FAB
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(SociallistActivity.this, "음악 만들기 버튼 눌림", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SociallistActivity.this, MakeMusicActivity.class);
                startActivity(intent);
                materialDesignFAM.close(true);
            }
        });

        // Teach FAB
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(SociallistActivity.this, getString(R.string.ready_service), Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(SociallistActivity.this, TeachActivity.class);
                //startActivity(intent);
                materialDesignFAM.close(true);
            }
        });

        // Write Post FAB
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(SociallistActivity.this, "게시물 작성 버튼 눌림", Toast.LENGTH_SHORT).show();
                materialDesignFAM.close(true);

                if(materialDesignFAM.isAnimated()) {
                    Intent intent = new Intent(SociallistActivity.this, PostActivity.class);
                    intent.putExtra("user_id", userID);
                    startActivity(intent);
                }
            }
        });

        mult = 0;
        //Toast.makeText(getContext(), "Refresh Timeline Fragment", Toast.LENGTH_SHORT).show();
        lv = (EndlessListView)findViewById(R.id.el);

        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getApplicationContext(), "Refresh Executed", Toast.LENGTH_SHORT).show();
                System.out.println("Refresh Data");

                List<ListItem> resultList = createItems();
                Log.d("TAG", "Refresh CreateItem Finish");

                lv.refreshData(resultList);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        adp = new EndlessAdapter(this, createItems(), R.layout.list_item_card);
        lv.setAdapter(adp);
        lv.setListener(this);

        List<ListItem> resultList2 = createItems();
        lv.refreshData(resultList2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(SociallistActivity.this, DetailsActivity.class);

                List<ListItem> temp = new ArrayList<ListItem>();
                temp.add((ListItem)lv.getAdapter().getItem(position));
                ListItem temp2 = temp.get(0);

                String details_theme = temp2.getTheme();
                String details_nickname = temp2.getNickname();
                String details_contents = temp2.getContent();
                int details_favoriteCnt = temp2.getFavoriteCnt();
                String details_musicpath = temp2.getMusicPath();

                intent.putExtra("theme", details_theme);
                intent.putExtra("nickname", details_nickname);
                intent.putExtra("contents", details_contents);
                intent.putExtra("musicpath", details_musicpath);
                intent.putExtra("favoriteCnt", details_favoriteCnt);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                LoginManager.getInstance().logOut();
                finish();
                toast.cancel();
            }
        }
    }

    @Override
    public void onDestroy() {
        LoginManager.getInstance().logOut();
        super.onDestroy();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.info_nav) {
            Toast.makeText(this, "현재 서비스 준비중입니다.", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.mylist_nav) {
            Intent intent = new Intent(SociallistActivity.this, MyListActivity.class);
            startActivity(intent);
        } else if (id == R.id.sociallist_nav) {
            // Do Nothing
            // Because : Same Activity
        } else if (id == R.id.settings_nav) {
            Intent intent = new Intent(SociallistActivity.this, SettingActivity.class);
            intent.putExtra("email", UserData.userEmail);
            intent.putExtra("nickname", UserData.userNickname);
            startActivity(intent);

        } else if (id == R.id.inquiry_nav) {
            Toast.makeText(this, "현재 서비스 준비중입니다.", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.reckakao_nav) {
            Intent intent = new Intent(SociallistActivity.this, KakaoTalkSendActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout_nav) {
            LoginManager.getInstance().logOut();
            Toast.makeText(this, R.string.logout_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SociallistActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showGuide() {
        toast = Toast.makeText(SociallistActivity.this,
                R.string.toast_exit_msg, Toast.LENGTH_SHORT);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) {
            v.setGravity(Gravity.CENTER);
            v.setTextSize(13);
        }
        toast.show();
    }

    // Data Load Class
    private class NetLoader extends AsyncTask<ListItem, Void, List<ListItem>> {
        @Override
        protected List<ListItem> doInBackground(ListItem... params) {
            //try {
            //    Thread.sleep(2000);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
            return createItems();
        }

        @Override
        protected void onPostExecute(List<ListItem> result) {
            super.onPostExecute(result);
            lv.addNewData(result);
        }
    }

    private List<ListItem> createItems() {
        List<ListItem> result = new ArrayList<ListItem>();
        ListItem temp;
        String sociallistURL = URLData.defaultURL + URLData.boardDir + URLData.mainDataListAPI;

        Log.d("TAG", "Before Requesting to Server");
        Log.i("TAG", HttpURLConnection.HTTP_OK + "");

        // Request to Server
        try{
            String response = new HttpUtils(3, sociallistURL, UserData.userID, "", "", "", "", "", "", "", "", "", "", "", "").execute("").get();

            Log.d("TAG", "After Requesting to Server");
            if(response == null) {
                //Time Out
            } else {
                //Toast.makeText(SociallistActivity.this, response, Toast.LENGTH_LONG).show();

                // Apply Data to List
                JSONObject job = new JSONObject(response);
                String resultArray = job.getString("resultData");

                JSONArray jar = new JSONArray(resultArray);
                for (int i = 0; i < jar.length(); ++i) {
                    JSONObject item = jar.getJSONObject(i);
                    temp = new ListItem();
                    temp.setContent(item.getString("boardContent"));
                    String themeIndex = item.getString("boardTheme");

                    String[] arrString = getResources().getStringArray(R.array.music_theme);
                    ArrayList<String> arraylist_theme = new ArrayList<String>();

                    for(String str : arrString) {
                        arraylist_theme.add(str);
                    }

                    int theme_index = Integer.parseInt(themeIndex);
                    temp.setTheme(arraylist_theme.get(theme_index - 1));
                    temp.setNickname(item.getString("userNickName"));
                    temp.setMusicPath(item.getString("boardMusicUrl"));
                    temp.setBoardNo(item.getString("boardNo"));
                    temp.setFavoriteCnt(Integer.parseInt(item.getString("likeCnt")));

                    String likeChecked = item.getString("likeYn");
                    if (likeChecked.equals("Y")) {
                        temp.setChecked(true);
                    } else {
                        temp.setChecked(false);
                    }

                    // Add Data to List
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
        NetLoader fl = new NetLoader();
        fl.execute(new ListItem[]{});
    }
}
