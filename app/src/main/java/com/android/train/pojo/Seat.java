package com.android.train.pojo;

public class Seat {
    /** ID */
    private String id;

    /** 列车ID */
    private Long trainId;

    /** 车厢号 */
    private String carriageNumber;

    /** 座位号 */
    private String seatNumber;

    /** 座位类型 */
    private Long seatType;

    /** 起始站 */
    private String startStation;

    /** 终点站 */
    private String endStation;

    /** 车票价格 */
    private Long price;

    /** 座位状态 */
    private Long seatStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public String getCarriageNumber() {
        return carriageNumber;
    }

    public void setCarriageNumber(String carriageNumber) {
        this.carriageNumber = carriageNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Long getSeatType() {
        return seatType;
    }

    public void setSeatType(Long seatType) {
        this.seatType = seatType;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(Long seatStatus) {
        this.seatStatus = seatStatus;
    }
}
