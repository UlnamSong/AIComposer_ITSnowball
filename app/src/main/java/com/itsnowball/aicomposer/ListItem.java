package com.itsnowball.aicomposer;

import android.graphics.drawable.Drawable;

/**
 * Created by Luny on 2016. 8. 3..
 */
public class ListItem {
    private String theme;
    private String nickname;
    private String musicPath;
    private int favoriteCnt;
    private String content;
    private String boardNo;

    private boolean isChecked = false;
    // Set Methods
    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFavoriteCnt(int favoriteCnt) {
        this.favoriteCnt = favoriteCnt;
    }

    public void setChecked(boolean parameter) {
        isChecked = parameter;
    }

    // Get Methods
    public String getBoardNo() {
        return boardNo;
    }

    public String getTheme() {
        return theme;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public String getContent() {
        return content;
    }

    public int getFavoriteCnt() {
        return favoriteCnt;
    }

    public boolean getChecked() {
        return isChecked;
    }

}
