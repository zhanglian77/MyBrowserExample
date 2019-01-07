package com.example.zhanglian4.mybrowserexample;

public class Websites {

    private String website;
    private String url;

    public Websites(String website, String url){
        this.website = website;
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
