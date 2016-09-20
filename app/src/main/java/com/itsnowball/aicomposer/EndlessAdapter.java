package com.itsnowball.aicomposer;

/**
 * Created by Luny on 2016. 7. 25..
 */

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EndlessAdapter extends ArrayAdapter<ListItem> {

    private List<ListItem> itemList;
    private Context ctx;
    private int layoutId;

    public View result;
    public boolean isChecked = false;

    public EndlessAdapter(Context ctx, List<ListItem> itemList, int layoutId) {
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
    public ListItem getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        result = convertView;

        if (result == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inflater.inflate(layoutId, parent, false);
        }

        // We should use class holder pattern
        TextView tv = (TextView) result.findViewById(R.id.tv_contents);
        tv.setText(itemList.get(position).getContent());

        TextView tv_theme = (TextView) result.findViewById(R.id.tv_id);
        tv_theme.setText(itemList.get(position).getTheme());

        TextView tv_nickname = (TextView) result.findViewById(R.id.tv_nickname);
        tv_nickname.setText(itemList.get(position).getNickname());

        final Button favoriteBtn = (Button) result.findViewById(R.id.favoriteBtn);

        // Initialize Buttons
        if(itemList.get(position).getChecked()) {
            favoriteBtn.setBackgroundResource(R.drawable.button_white);
            favoriteBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.favorite_icon, 0, 0, 0);
            //favoriteBtn.setLeft(R.drawable.favorite_icon);
            favoriteBtn.setTextColor(Color.argb(255, 204, 71, 162));
        } else {
            favoriteBtn.setBackgroundResource(R.drawable.button_white);
            favoriteBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.favorite_button_unclicked, 0, 0, 0);
            //favoriteBtn.setLeft(R.drawable.favorite_button);
            favoriteBtn.setTextColor(Color.argb(255, 189, 217, 216));
        }

        favoriteBtn.setText(getContext().getResources().getString(R.string.sociallist_favorite) + " " + itemList.get(position).getFavoriteCnt());

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChecked = itemList.get(position).getChecked();
                String boardNo = itemList.get(position).getBoardNo();
                String boardLikeStatusURL = URLData.defaultURL + URLData.boardDir + URLData.boardLikeStatusAPI;

                Log.d("BOARD NO", boardNo);
                Log.d("USER ID", UserData.userID);
                new HttpUtils(31, boardLikeStatusURL, UserData.userID, boardNo, "", "", "", "", "", "", "", "", "", "", "").execute();

                if(isChecked) {
                    // Temporary Code
                    int currentCount = itemList.get(position).getFavoriteCnt();
                    itemList.get(position).setFavoriteCnt(currentCount - 1);
                    currentCount = itemList.get(position).getFavoriteCnt();
                    //Toast.makeText(getContext(), "isChecked : " + isChecked + ", currentCount : " + (currentCount), Toast.LENGTH_SHORT).show();

                    // 해당 아이템만 데이터를 갱신한다
                    favoriteBtn.setText(getContext().getResources().getString(R.string.sociallist_favorite) + " " + currentCount);

                    // 갱신된 데이터를 적용한다
                    favoriteBtn.setBackgroundResource(R.drawable.button_white);
                    favoriteBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.favorite_button_unclicked, 0, 0, 0);
                    favoriteBtn.setTextColor(Color.argb(255, 189, 217, 216));
                    itemList.get(position).setChecked(false);
                } else {
                    // Temporary Code
                    int currentCount = itemList.get(position).getFavoriteCnt();
                    itemList.get(position).setFavoriteCnt(currentCount + 1);
                    currentCount = itemList.get(position).getFavoriteCnt();
                    //Toast.makeText(getContext(), "isChecked : " + isChecked + ", currentCount : " + (currentCount), Toast.LENGTH_SHORT).show();

                    // 해당 아이템만 데이터를 갱신한다
                    //refreshFavoriteCount =

                    // 갱신된 데이터를 적용한다
                    favoriteBtn.setText(getContext().getResources().getString(R.string.sociallist_favorite) + " " + currentCount);

                    favoriteBtn.setBackgroundResource(R.drawable.button_white);
                    favoriteBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.favorite_icon, 0, 0, 0);
                    favoriteBtn.setTextColor(Color.argb(255, 204, 71, 162));
                    itemList.get(position).setChecked(true);
                }
            }
        });

        Button downloadBtn = (Button) result.findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_title_download);
                builder.setCancelable(true);
                builder.setMessage(R.string.download_msg);
                builder.setPositiveButton(R.string.dialog_continue, null);
                builder.setNegativeButton(R.string.dialog_cancel, null);
                builder.show();
            }
        });

        return result;

    }
}

