package com.android.train.model;


public class TrainModel {
    private String departureTime;
    private String trainNumber;
    private String arrivalTime;
    private String departureStation;
    private String arrivalStation;
    private String duration;
    private String secondClassStatus;
    private String firstClassStatus;
    private String businessClass;
    private boolean isSpecialOffer;
    private String price;

    public TrainModel(String departureTime, String trainNumber, String arrivalTime,
                      String departureStation, String arrivalStation, String duration,
                      String secondClassStatus, String firstClassStatus, String businessClass
    ) {
        this.departureTime = departureTime;
        this.trainNumber = trainNumber;
        this.arrivalTime = arrivalTime;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.duration = duration;
        this.secondClassStatus = secondClassStatus;
        this.firstClassStatus = firstClassStatus;
        this.businessClass = businessClass;
    }

    // Getters and setters
    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSecondClassStatus() {
        return secondClassStatus;
    }

    public void setSecondClassStatus(String secondClassStatus) {
        this.secondClassStatus = secondClassStatus;
    }

    public String getFirstClassStatus() {
        return firstClassStatus;
    }

    public void setFirstClassStatus(String firstClassStatus) {
        this.firstClassStatus = firstClassStatus;
    }

    public boolean isSpecialOffer() {
        return isSpecialOffer;
    }

    public void setSpecialOffer(boolean specialOffer) {
        isSpecialOffer = specialOffer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(String businessClass) {
        this.businessClass = businessClass;
    }
}
