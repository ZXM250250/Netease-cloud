package com.example.macticity.music;

public class Mysongs {     //这是一个歌单类




    private String coverImgUrl;
    private String name;
    private String id;
    private String signature;




    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSignature() {
        return signature;
    }
}
