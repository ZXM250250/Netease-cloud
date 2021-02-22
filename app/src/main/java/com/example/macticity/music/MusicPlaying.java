package com.example.macticity.music;

public class MusicPlaying {


    private String musicname;   //歌曲名字
    private String picUrl;  //歌曲url
    private String anthor;  //作者
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMusicname(String musicname) {
        this.musicname = musicname;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setAnthor(String anthor) {
        this.anthor = anthor;
    }

    public String getMusicname() {
        return musicname;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getAnthor() {
        return anthor;
    }
}
