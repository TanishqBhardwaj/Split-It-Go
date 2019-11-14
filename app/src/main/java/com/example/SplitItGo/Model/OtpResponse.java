package com.example.SplitItGo.Model;

public class OtpResponse {

    private String message;
    private String detail;
    private int user_id;

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public int getUser_id() {
        return user_id;
    }

    public OtpResponse(String message) {
        this.message = message;
    }

    public OtpResponse(String detail, int user_id) {
        this.detail = detail;
        this.user_id = user_id;
    }
}
