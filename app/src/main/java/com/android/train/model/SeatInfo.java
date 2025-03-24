package com.android.train.model;

public class SeatInfo {
    public final String seatNumber;
    public final int normalBackground;
    public final int selectedBackground;

    public SeatInfo(String seatNumber, int normalBackground, int selectedBackground) {
        this.seatNumber = seatNumber;
        this.normalBackground = normalBackground;
        this.selectedBackground = selectedBackground;
    }
}
