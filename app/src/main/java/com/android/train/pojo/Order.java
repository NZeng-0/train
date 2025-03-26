package com.android.train.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Order {
     /** ID */
    private String id;

    /** 订单号 */
    private String orderSn;

    /** 用户ID */
    private Long userId;

    /** 列车ID */
    private Long trainId;

    /** 列车车次 */
    private String trainNumber;

    /** 乘车日期 */
    private String ridingDate;

    /** 出发站点 */
    private String departure;

    /** 到达站点 */
    private String arrival;

    /** 出发时间 */
    @SerializedName("departureTime")
    private String departureTime;

    /** 到达时间 */
    @SerializedName("arrivalTime")
    private String arrivalTime;

    /** 座位类型 */
    private Long seatType;

    /** 车厢号 */
    private String carriageNumber;

    /** 座位号 */
    private String seatNumber;

    /** 真实姓名 */
    private String realName;

    /** 车票类型 */
    private Long ticketType;

    /** 手机号 */
    private String phone;

    /** 订单金额 */
    private Long amount;

    /** 订单状态 */
    private Long status;

    /** 支付方式 */
    private Long payType;

    /** 支付时间 */
    private Date payTime;

    public Order(
            Long userId,
            Long trainId,
            String trainNumber,
            String ridingDate,
            String departure,
            String arrival,
            String departureTime,
            String arrivalTime,
            Long seatType,
            String carriageNumber,
            String seatNumber,
            String realName,
            Long ticketType,
            String phone,
            Long amount,
            Long payType,
            Long status)
    {
        this.userId = userId;
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.ridingDate = ridingDate;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatType = seatType;
        this.carriageNumber = carriageNumber;
        this.seatNumber = seatNumber;
        this.realName = realName;
        this.ticketType = ticketType;
        this.phone = phone;
        this.amount = amount;
        this.payType = payType;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getRidingDate() {
        return ridingDate;
    }

    public void setRidingDate(String ridingDate) {
        this.ridingDate = ridingDate;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
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

    public Long getSeatType() {
        return seatType;
    }

    public void setSeatType(Long seatType) {
        this.seatType = seatType;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getTicketType() {
        return ticketType;
    }

    public void setTicketType(Long ticketType) {
        this.ticketType = ticketType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getPayType() {
        return payType;
    }

    public void setPayType(Long payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
