package com.android.train.model;

import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("realName")
    private String realName;

    @SerializedName("idType")
    private Integer idType;

    @SerializedName("idCard")
    private String idCard;

    @SerializedName("phone")
    private String phone;

    @SerializedName("mail")
    private String mail;

    // 构造方法
    public UserRequest(String username, String password, String realName, Integer idType, String idCard, String phone, String mail) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.idType = idType;
        this.idCard = idCard;
        this.phone = phone;
        this.mail = mail;
    }

    // Getter 和 Setter 方法
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public Integer getIdType() { return idType; }
    public void setIdType(Integer idType) { this.idType = idType; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}
