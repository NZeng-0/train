package com.android.train.pojo;

public class Ticket {
    /** ID */
    private String id;

    /** 用户名 */
    private String username;

    /** 列车ID */
    private Long trainId;

    /** 车厢号 */
    private String carriageNumber;

    /** 座位号 */
    private String seatNumber;

    /** 乘车人ID */
    private Long passengerId;

    /** 车票状态 */
    private Long ticketStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Long ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}
