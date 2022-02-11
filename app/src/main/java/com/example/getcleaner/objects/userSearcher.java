package com.example.getcleaner.objects;

public class userSearcher {
    private String emailStr,passwordStr,addressStr,firstNameStr,lastNameStr,phoneNumberStr;
    public userSearcher(){

    }
    public userSearcher(String emailStr, String passwordStr, String addressStr,String firstNameStr, String lastNameStr, String phoneNumberStr) {
        this.emailStr = emailStr;
        this.passwordStr = passwordStr;
        this.addressStr = addressStr;
        this.firstNameStr = firstNameStr;
        this.lastNameStr = lastNameStr;
        this.phoneNumberStr = phoneNumberStr;
    }


    public String getEmailStr() {
        return emailStr;
    }

    public void setEmailStr(String emailStr) {
        this.emailStr = emailStr;
    }

    public String getPasswordStr() {
        return passwordStr;
    }

    public void setPasswordStr(String passwordStr) {
        this.passwordStr = passwordStr;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }
    public String getFirstNameStr() {
        return firstNameStr;
    }

    public void setFirstNameStr(String firstNameStr) {
        this.firstNameStr = firstNameStr;
    }

    public String getLastNameStr() {
        return lastNameStr;
    }

    public void setLastNameStr(String lastNameStr) {
        this.lastNameStr = lastNameStr;
    }

    public String getPhoneNumberStr() {
        return phoneNumberStr;
    }

    public void setPhoneNumberStr(String phoneNumberStr) {
        this.phoneNumberStr = phoneNumberStr;
    }
}
