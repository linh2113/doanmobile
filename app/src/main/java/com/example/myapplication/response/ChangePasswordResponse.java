package com.example.myapplication.response;

public class ChangePasswordResponse {
    private boolean status;
    private InvalidDetail invalidDetail;
    private String gmailAddress;
    private String msg;

    // Getters and setters

    public static class InvalidDetail {
        private String invaidType;
        private String invalidMsg;

        public InvalidDetail() {
        }

        public InvalidDetail(String invaidType, String invalidMsg) {
            this.invaidType = invaidType;
            this.invalidMsg = invalidMsg;
        }

        public String getInvaidType() {
            return invaidType;
        }

        public void setInvaidType(String invaidType) {
            this.invaidType = invaidType;
        }

        public String getInvalidMsg() {
            return invalidMsg;
        }

        public void setInvalidMsg(String invalidMsg) {
            this.invalidMsg = invalidMsg;
        }
    }

    public ChangePasswordResponse() {
    }

    public ChangePasswordResponse(boolean status, InvalidDetail invalidDetail, String gmailAddress, String msg) {
        this.status = status;
        this.invalidDetail = invalidDetail;
        this.gmailAddress = gmailAddress;
        this.msg = msg;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public InvalidDetail getInvalidDetail() {
        return invalidDetail;
    }

    public void setInvalidDetail(InvalidDetail invalidDetail) {
        this.invalidDetail = invalidDetail;
    }

    public String getGmailAddress() {
        return gmailAddress;
    }

    public void setGmailAddress(String gmailAddress) {
        this.gmailAddress = gmailAddress;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
