package com.lit.riseup;

public class SearchModal {
    String search_url, search_image, search_title, search_description, search_channel_image, channel_name;
    public SearchModal(String search_url, String search_image, String search_title, String search_description, String search_channel_image, String channel_name) {
        this.search_url = search_url;
        this.search_image = search_image;
        this.search_title = search_title;
        this.search_description = search_description;
        this.search_channel_image = search_channel_image;
        this.channel_name = channel_name;
    }

    public String getSearch_url() {
        return search_url;
    }

    public void setSearch_url(String search_url) {
        this.search_url = search_url;
    }

    public String getSearch_image() {
        return search_image;
    }

    public void setSearch_image(String search_image) {
        this.search_image = search_image;
    }

    public String getSearch_title() {
        return search_title;
    }

    public void setSearch_title(String search_title) {
        this.search_title = search_title;
    }

    public String getSearch_description() {
        return search_description;
    }

    public void setSearch_description(String search_description) {
        this.search_description = search_description;
    }

    public String getSearch_channel_image() {
        return search_channel_image;
    }

    public void setSearch_channel_image(String search_channel_image) {
        this.search_channel_image = search_channel_image;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }
}
