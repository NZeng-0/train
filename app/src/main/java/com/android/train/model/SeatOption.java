package com.android.train.model;

public class SeatOption {
    private String seatType;
    private int price;
    private boolean available;
    private boolean isSelected;

    public SeatOption(String seatType, int price, boolean available) {
        this.seatType = seatType;
        this.price = price;
        this.available = available;
        this.isSelected = false;
    }

    public String getSeatType() { return seatType; }
    public int getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}

