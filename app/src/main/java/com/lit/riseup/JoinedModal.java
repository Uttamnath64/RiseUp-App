package com.lit.riseup;

public class JoinedModal {

    String  channel_url,
            channel_name,
            channel_joiner,
            channel_image,
            article_url,
            article_image;


    public JoinedModal(String channel_url, String channel_name, String channel_joiner, String channel_image, String article_url, String article_image) {
        this.channel_url = channel_url;
        this.channel_name = channel_name;
        this.channel_joiner = channel_joiner;
        this.channel_image = channel_image;
        this.article_url = article_url;
        this.article_image = article_image;
    }

    public String getChannel_url() {
        return channel_url;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public String getChannel_joiner() {
        return channel_joiner;
    }

    public String getChannel_image() {
        return channel_image;
    }

    public String getArticle_url() {
        return article_url;
    }

    public String getArticle_image() {
        return article_image;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public void setChannel_joiner(String channel_joiner) {
        this.channel_joiner = channel_joiner;
    }

    public void setChannel_image(String channel_image) {
        this.channel_image = channel_image;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public void setArticle_image(String article_image) {
        this.article_image = article_image;
    }
}
