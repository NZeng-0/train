package com.android.train.ui.home;

public class TabItem {
    private final String title;
    private final int iconResId;

    public TabItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}

