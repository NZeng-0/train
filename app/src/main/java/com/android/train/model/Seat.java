package com.android.train.model;

public class Seat {
    private final String seatType; // 座位类型（A、B、C、D、F）
    private final int bg;
    private final boolean isAvailable; // 是否可选
    private boolean isSelected; // 是否被选中

    public Seat(String seatType, boolean isAvailable, int bg) {
        this.seatType = seatType;
        this.isAvailable = isAvailable;
        this.isSelected = false;
        this.bg = bg;
    }

    public String getSeatType() { return seatType; }
    public boolean isAvailable() { return isAvailable; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
    public int getBg(){
        return this.bg;
    }
}
