package com.itsnowball.aicomposer;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luny on 2016. 8. 20..
 */
public class MylistListViewAdapter extends ArrayAdapter<MyListItem> {
    private ArrayList<MyListItem> listViewItemList = new ArrayList<MyListItem>() ;
    private List<MyListItem> itemList;
    private Context ctx;
    private int layoutId;

    // ListViewAdapter의 생성자
    public MylistListViewAdapter(Context ctx, List<MyListItem> itemList, int layoutId) {
        super(ctx, layoutId, itemList);
        this.itemList = itemList;
        this.ctx = ctx;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public MyListItem getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).hashCode();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final Context result = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null || result == null) {
            // If Context is null, inflate layout to application system.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mylist_item_card, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.tv_musictitle);
        TextView themeTextView = (TextView) convertView.findViewById(R.id.tv_musictheme);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(itemList.get(position).getMusicTitle());
        themeTextView.setText(itemList.get(position).getMusicTheme());

        Button downloadBtn = (Button) convertView.findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_title_download);
                builder.setCancelable(true);
                builder.setMessage(R.string.download_msg);

                // Prositive Button : Download Start
                builder.setPositiveButton(R.string.dialog_continue, null);
                builder.setNegativeButton(R.string.dialog_cancel, null);
                builder.show();
            }
        });

        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String path) {
        MyListItem item = new MyListItem();

        item.setMusicPath(path);
        item.setMusicTitle(title);

        listViewItemList.add(item);
    }
}
