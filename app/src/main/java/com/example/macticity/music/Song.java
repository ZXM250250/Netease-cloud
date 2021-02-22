package com.example.macticity.music;

import java.util.ArrayList;
import java.util.List;

public class Song {      //一个歌单类

    private String songid;    //歌单的id
    private String nickname;   //歌单的名字
    private String coverImgUrl;  //歌单的封面

    public String getSongid() {
        return songid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }



    public void setSongid(String songid) {
        this.songid = songid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }
}
