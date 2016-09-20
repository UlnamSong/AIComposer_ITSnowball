package com.itsnowball.aicomposer;

import android.graphics.drawable.Drawable;

/**
 * Created by Luny on 2016. 8. 20..
 */
public class MyListItem {
    private String musicPath;
    private String musicTitle;
    private String musicTheme;

    // Set Methods
    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public void setMusicTheme(String musicTheme) {
        this.musicTheme = musicTheme;
    }

    // Get Methods
    public String getMusicPath() {
        return musicPath;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public String getMusicTheme() {
        return musicTheme;
    }
}
