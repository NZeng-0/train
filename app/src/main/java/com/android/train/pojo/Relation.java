package com.android.train.pojo;

import java.util.Date;

public class Relation {

    /**
     * ID
     */
    private String id;

    /**
     * 列车车次
     */
    private String trainNumber;

    /**
     * 列车类型 0：高铁 1：动车 2：普通车
     */
    private Long trainType;

    /**
     * 列车标签 0：复兴号 1：智能动车组 2：静音车厢 3：支持选铺
     */
    private String trainTag;

    /**
     * 列车品牌 0：GC-高铁/城际 1：D-动车 2：Z-直达 3：T-特快 4：K-快速 5：其他 6：复兴号 7：智能动车组
     */
    private String trainBrand;

    /**
     * 起始站
     */
    private String startStation;

    /**
     * 终点站
     */
    private String endStation;

    /**
     * 起始城市
     */
    private String startRegion;

    /**
     * 终点城市
     */
    private String endRegion;

    /**
     * 销售时间
     */
    private Date saleTime;

    /**
     * 销售状态 0：可售 1：不可售 2：未知
     */
    private Long saleStatus;

    /**
     * 出发时间
     */
    private String departureTime;

    /**
     * 到达时间
     */
    private String arrivalTime;

    /** 商务座余票 */
    private Integer businessSeat;

    /** 一等座余票 */
    private Integer firstSeat;

    /** 二等座余票 */
    private Integer secondSeat;

    private Integer businessPrice;
    private Integer firstPrice;
    private Integer secondPrice;

    public Relation(
            String id,
            String trainNumber,
            Long trainType,
            String trainTag,
            String trainBrand,
            String startStation,
            String endStation,
            String startRegion,
            String endRegion,
            Date saleTime,
            Long saleStatus,
            String departureTime,
            String arrivalTime,
            Integer businessSeat,
            Integer firstSeat,
            Integer secondSeat,
            Integer businessPrice,
            Integer firstPrice,
            Integer secondPrice
    ) {
        this.id = id;
        this.trainNumber = trainNumber;
        this.trainType = trainType;
        this.trainTag = trainTag;
        this.trainBrand = trainBrand;
        this.startStation = startStation;
        this.endStation = endStation;
        this.startRegion = startRegion;
        this.endRegion = endRegion;
        this.saleTime = saleTime;
        this.saleStatus = saleStatus;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.businessSeat = businessSeat;
        this.firstSeat = firstSeat;
        this.secondSeat = secondSeat;
        this.businessPrice = businessPrice;
        this.firstPrice = firstPrice;
        this.secondPrice = secondPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Long getTrainType() {
        return trainType;
    }

    public void setTrainType(Long trainType) {
        this.trainType = trainType;
    }

    public String getTrainTag() {
        return trainTag;
    }

    public void setTrainTag(String trainTag) {
        this.trainTag = trainTag;
    }

    public String getTrainBrand() {
        return trainBrand;
    }

    public void setTrainBrand(String trainBrand) {
        this.trainBrand = trainBrand;
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

    public String getStartRegion() {
        return startRegion;
    }

    public void setStartRegion(String startRegion) {
        this.startRegion = startRegion;
    }

    public String getEndRegion() {
        return endRegion;
    }

    public void setEndRegion(String endRegion) {
        this.endRegion = endRegion;
    }

    public Date getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Date saleTime) {
        this.saleTime = saleTime;
    }

    public Long getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Long saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getBusinessSeat() {
        return businessSeat;
    }

    public void setBusinessSeat(Integer businessSeat) {
        this.businessSeat = businessSeat;
    }

    public Integer getFirstSeat() {
        return firstSeat;
    }

    public void setFirstSeat(Integer firstSeat) {
        this.firstSeat = firstSeat;
    }

    public Integer getSecondSeat() {
        return secondSeat;
    }

    public void setSecondSeat(Integer secondSeat) {
        this.secondSeat = secondSeat;
    }
    public Integer getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(Integer businessPrice) {
        this.businessPrice = businessPrice;
    }

    public Integer getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Integer firstPrice) {
        this.firstPrice = firstPrice;
    }

    public Integer getSecondPrice() {
        return secondPrice;
    }

    public void setSecondPrice(Integer secondPrice) {
        this.secondPrice = secondPrice;
    }
}
