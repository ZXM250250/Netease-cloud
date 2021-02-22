package com.example.macticity.api;

public class ApiConfig {

    public static final String BASE_URl = "http://sandyz.ink:3000";
    public static final String MOBILE_LOGIN ="/login/cellphone";
    public static final String MOBILE_TEST = "/cellphone/existence/check";  //检测手机号是否已经注册
    public static final String MOBILE_CTCODE= "/captcha/sent";
    public static final String MOBILE_VERIFY_CODE = "/captcha/verify";
    public static final String MAIN_SONGS = "/top/playlist";     //推荐歌单的接口
    public static final String MAIN_OWN_SONGS = "/user/playlist"; // 用户自己的歌单
    public static final String LYRICS = "/lyric";  //歌词接口
    public static final String SONGS = "/playlist/detail";   //歌单详情
    public static final String MAIN_URL = "/song/url";
    public static final String CHECK_MUSIC="/check/music";//检查音乐是否可用
    public static final String MAIN_MUSIC_DETIALS = "/song/detail";//获取歌曲详情
    public static final String MUSIC_LIKE ="/like";  //喜欢歌曲的接口
    public static final String MAIN_REGISTER = "/register/cellphone";
    public static final String DROP_OUT="/logout";
}
