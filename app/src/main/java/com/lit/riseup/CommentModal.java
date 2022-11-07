package com.lit.riseup;

public class CommentModal {
    String channel_logo, channel_name, comment_date, comment,channel_Url;
    public CommentModal(String channel_logo, String channel_name, String comment_date, String comment, String channel_Url) {
        this.channel_logo = channel_logo;
        this.channel_name = channel_name;
        this.comment_date = comment_date;
        this.comment = comment;
        this.channel_Url = channel_Url;
    }

    public String getChannel_logo() {
        return channel_logo;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public String getComment_date() {
        return comment_date;
    }

    public String getComment() {
        return comment;
    }
    public String getChannel_Url() {
        return channel_Url;
    }

    public void setChannel_Url(String channel_Url) {
        this.channel_Url = channel_Url;
    }
    public void setChannel_logo(String channel_logo) {
        this.channel_logo = channel_logo;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
