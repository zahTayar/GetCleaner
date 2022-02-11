package com.example.getcleaner.objects;

public class userCleaner {
    private String emailStr,passwordStr,addressStr,firstNameStr,lastNameStr,phoneNumberStr,perHour;
    private boolean readyForSpecial,mobility;
    private boolean favorite=false;

    public userCleaner(){

    }
    public userCleaner(String emailStr, String passwordStr, String addressStr,String firstNameStr, String lastNameStr,
                       String phoneNumberStr,String perHour,boolean readyForSpecial, boolean mobility) {
        this.emailStr = emailStr;
        this.passwordStr = passwordStr;
        this.addressStr = addressStr;
        this.firstNameStr = firstNameStr;
        this.lastNameStr = lastNameStr;
        this.phoneNumberStr = phoneNumberStr;
        this.perHour=perHour;
        this.readyForSpecial=readyForSpecial;
        this.mobility = mobility;
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

    public String getPerHour() {
        return perHour;
    }

    public void setPerHour(String perHour) {
        this.perHour = perHour;
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


    public boolean isReadyForSpecial() {
        return readyForSpecial;
    }

    public void setReadyForSpecial(boolean readyForSpecial) {
        this.readyForSpecial = readyForSpecial;
    }

    public boolean isMobility() {
        return mobility;
    }

    public void setMobility(boolean mobility) {
        this.mobility = mobility;
    }
    public String getRelaventData()
    {
        String mobility="";
        String special="";
        if(isMobility())
            mobility="I am mobile";
        else
            mobility="I am not mobile";
        if(isReadyForSpecial())
            special="I am doing special missions";
        else
            special="I am not doing special missions";

        return "Cost per hour: "+getPerHour()+"\n" +
                "Phone number: "+getPhoneNumberStr()+"\n"+
                mobility+"\n"+
                special;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
