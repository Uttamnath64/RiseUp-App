package com.lit.riseup;

public class ListHomeCardData {
    private String Url;
    private String Image;
    private String Title;
    private String Description;
    private String ChannelLogo;
    private String ChannelNameAndViewAndTime;

    public ListHomeCardData(String Url, String image, String title, String description, String channelLogo, String ChannelNameAndViewAndTime) {
        this.Url = Url;
        this.Image = image;
        this.Title = title;
        this.Description = description;
        this.ChannelLogo = channelLogo;
        this.ChannelNameAndViewAndTime = ChannelNameAndViewAndTime;
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getChannelLogo() {
        return ChannelLogo;
    }

    public void setChannelLogo(String channelLogo) {
        ChannelLogo = channelLogo;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        Url = Url;
    }


    public String getChannelNameAndViewAndTime() {
        return ChannelNameAndViewAndTime;
    }

    public void setChannelNameAndViewAndTime(String ChannelNameAndViewAndTime) {
        ChannelNameAndViewAndTime = ChannelNameAndViewAndTime;
    }
}
