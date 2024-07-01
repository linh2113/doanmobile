package com.example.myapplication.model;

public class RegisterResponse {
    boolean status;
    InvalidDetail invalidDetail;
    String gmailAddress;
    String token;
    int foxcoinAmount;

    public boolean isStatus() {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getFoxcoinAmount() {
        return foxcoinAmount;
    }

    public void setFoxcoinAmount(int foxcoinAmount) {
        this.foxcoinAmount = foxcoinAmount;
    }

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
}
