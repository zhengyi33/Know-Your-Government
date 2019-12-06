package com.yizheng.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

    private String title, name, address, party, phone, url, email, photoUrl, facebook, twitter, youtube, google;

    public Official(String title, String name, String party) {
        this.title = title;
        this.name = name;
        this.party = party;
    }

    public Official(){}

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phone;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getGoogle() {
        return google;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public void setGoogle(String google) {
        this.google = google;
    }
}
