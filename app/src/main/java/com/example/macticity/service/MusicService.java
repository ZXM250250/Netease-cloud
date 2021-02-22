package com.example.macticity.service;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.acticity.R;
import com.example.macticity.adapter.RecycleradapterMainSongs;
import com.example.macticity.adapter.RecycleradapterSongs;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.fragmentmain.FragmentLyrics;
import com.example.macticity.fragmentmain.FragmentPlayingSongs;
import com.example.macticity.mactivity.MainActivity;
import com.example.macticity.mactivity.MainActivitySongs;
import com.example.macticity.music.Lyrics;
import com.example.macticity.music.Songs;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.view.LyricView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
public class MusicService extends Service implements NetworkRequest.OnNetworkRequestlistener,MediaPlayer.OnCompletionListener,RecycleradapterSongs.chechsonglistener {
    public  static MediaPlayer mp;
   // private String TAG = "测试三";
    private static int x=0;
    private Intent intent = new Intent("add.action");
    private static List<Songs> mysongs = new ArrayList<>();       //可播放音乐资源的集合
    private IntentFilter intentFilter;
    private Servicebroadcast servicebroadcast;
    private static NetworkRequest networkRequest;
    private int number=0;
    private int ch=0;
    private boolean isIllegalStateException=false;
    private int currentMs;
    private static String url;
    private static boolean isPlaying = false;
    private LocalBroadcastManager localBroadcastManager;
    private static boolean iscirculation=false;       //用户是否设置循环播放  默认不循环
    private  int numbersongsid;
    private  List<String> songsid = new ArrayList<>();
    private static boolean israndom = false;                    //用户设置是否随机播放  默认按顺序播放
    private Handler handle = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mp.start();
          //  Log.i(TAG,"开始播放音乐");
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                        updateTimer();

                }
            };
            new Timer().scheduleAtFixedRate(timerTask, 0, 500);
        }
    };






    private void prepareandplaySongs(String url){
       // Log.i(TAG,"创建播放器");
        MusicService.url=url;
        if(isIllegalStateException){
            mp=null;
            mp = MediaPlayer.create(this, Uri.parse(url));
            mp.seekTo(currentMs);
            isIllegalStateException=false;
        }else {
            if (mp!=null){
                mp.stop();
                mp.reset();
                mp=null;
                isPlaying=false;
            }
            mp = MediaPlayer.create(this, Uri.parse(url));
        }

            isPlaying=true;
           FragmentPlayingSongs.binding.ivPlaying.setImageResource(R.drawable.module_fragment_pause);
           if(mp!=null) {
            mp.setOnCompletionListener(this::onCompletion);
        }

//        try {
//            //mp.setDataSource(url);
//            Log.i(TAG,"设置资源");
//       } catch (IOException e) {
//           e.printStackTrace();
//        }
        new Thread(
                ()->{
                 //  Log.i(TAG,"进入线程");
                   // try {
                      //  Log.i(TAG,"准备之前");
                        //mp.prepare();
                        int duration = mp.getDuration();
                        intent.putExtra("duration",duration);
                      //  Log.i(TAG,"准备好了");
                        Message message = new Message();
                        handle.sendMessage(message);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
        ).start();

    }

//        private  void checkSong(int postion){
//                String id = mysongs.get(postion).getId();
//            String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL+"?id="+id;
//            networkRequest.sendGetNetRequest(url);
//        }



    private void updateTimer()  {
                try {
                    if (mp != null&&url!=null) {
                        if (isPlaying) {     //有闪退的风险
                            currentMs = mp.getCurrentPosition();
                            int sec = currentMs / 1000;
                            int min = sec / 60;
                            sec -= min * 60;
                            String time = String.format("%02d:%02d", min, sec);
                            intent.putExtra("currentMs", currentMs);
                            intent.putExtra("time", time);
                          //  Log.i("测试时候", "执行评率" + ch++);
                            localBroadcastManager.sendBroadcast(intent);
                            if (numbersongsid != songsid.size()) {     //保证只有歌曲数目发生改变的时候才会去刷新
                                MainActivitySongs.setSongsid(songsid);
                                numbersongsid = songsid.size();
                            }
                            //Log.i(TAG, currentMs + "时间");
                            //Log.i(TAG, "时间更新得到执行");
                        }


                    }
                }catch (IllegalStateException e){     //就这样误打误撞解决了 java.lang.IllegalStateException at android.media.MediaPlayer._prepare(Native Method) 这个错误
                    e.printStackTrace();
                    isIllegalStateException=true;    //表示发生了isIllegalStateException错误
                    prepareandplaySongs(url);

                }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Log.i("测试流","服务被启动了");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (true) {
                    updateTimer();
                }
            }
        };
        new Timer().scheduleAtFixedRate(timerTask, 0, 1000);
        String action=null;
        if(intent!=null){
            action = intent.getStringExtra("action");
        }

        if(action!=null&&action.equals("play")){    //播放的逻辑
            //Log.i("测试二","歌单的大小"+mysongs.size());
                         start();
//                if(mysongs.size()>=1) {
//                    Log.i("测试二","歌单的大小"+mysongs.size());
//                    Songs song = mysongs.get(0);
//                    Log.i("测试二","歌单的大小"+song.getId());
//
//                    String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL+"?id="+song.getId();
//                    networkRequest.sendGetNetRequest(url);
//                    }else {
//                    Toast.makeText(this,"当前没有要播放的音乐",Toast.LENGTH_SHORT).show();
//                }
                    //prepareandplaySongs("http://m7.music.126.net/20210216230646/45b26e55c94150d8dcda5cbdc67128b3/ymusic/f30e/c024/212f/59f62b09c2633b8aa1e3bca88701a876.mp3");
        }else if(action!=null&&action.equals("pause")){
                pause();
        }else if(action!=null&&action.equals("stop")){
               stop();
        }else if(action!=null&&action.equals("onchange")&&mp!=null){

                int progress = intent.getIntExtra("progress",0);
                mp.seekTo(progress);

        }else if(action!=null&&action.equals("ontouch")&&mp!=null){
                mp.pause();
        }else if(action!=null&&action.equals("stoptouch")&&mp!=null){
                mp.start();

        }else if(action!=null&&action.equals("up")&&mp!=null){    //播放下一首
            if(number<mysongs.size()-1){
                number++;
            }else{
                Toast.makeText(this,"大人已经没有可播放歌曲啦",Toast.LENGTH_SHORT).show();
            }
            Songs song = mysongs.get(number);
        //    Log.i("测试二","回调第"+number+"音乐"+song.getId());
            HashMap<String,String> map = new HashMap<>();
            map.put("id",song.getId());
            LyricView lyricView = new LyricView(this);
            lyricView.setContext(this);
            lyricView.networkquest(map);
            RecycleradapterSongs.setNumberonplayingid(number);
            FragmentPlayingSongs.setSongid(song.getId());
            String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL+"?id="+song.getId();
            networkRequest.sendGetNetRequest(url);


        }else if(action!=null&&action.equals("back")&&mp!=null){
            if(number>0){
                number--;
            }else {
                Toast.makeText(this,"报告大人：小抑暂未发现可播放曲目",Toast.LENGTH_SHORT).show();
            }
            Songs song = mysongs.get(number);
           // Log.i("测试二","回调第"+number+"音乐"+song.getId());
            HashMap<String,String> map = new HashMap<>();
            map.put("id",song.getId());
            LyricView lyricView = new LyricView(this);
            lyricView.setContext(this);
            lyricView.networkquest(map);
            RecycleradapterSongs.setNumberonplayingid(number);
            FragmentPlayingSongs.setSongid(song.getId());
            String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL+"?id="+song.getId();
            networkRequest.sendGetNetRequest(url);
        }else if(action!=null&&action.equals("circulation")&&mp!=null){
           iscirculation=true;
        }else if(action!=null&&action.equals("overcirculation")&&mp!=null){
            iscirculation=false;
        }else if(action!=null&&action.equals("beginrandom")&&mp!=null){
          //随机播放歌曲的逻辑
            israndom=true;
        }else if(action!=null&&action.equals("overrandom")&&mp!=null){
            //结束随机播放歌曲的逻辑
                israndom=false;
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        RecycleradapterSongs.setChechsonglistener(this::check);
        networkRequest = new NetworkRequest();
        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        //注册一个广播接收者来收取点击url信息
        localBroadcastManager =LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.ID");
        servicebroadcast = new Servicebroadcast();
        localBroadcastManager.registerReceiver(servicebroadcast,intentFilter);

//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  //音乐播放完成调用此接口
//            @Override
//            public void onCompletion(MediaPlayer mp) {    //每一播放完一首歌曲立马换
//                    Log.i("测试二","真假回调音乐播放"+mysongs.size());
//                    Songs song = mysongs.get(1);
//                    HashMap<String,String> map = new HashMap<>();
//                    map.put("id",song.getId());
//                    String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL;
//                    networkRequest.sendPostNetRequest(url,map);
//            }
//
//        });

    }

    private void pause(){
        if(mp!=null&&mp.isPlaying()){
            mp.pause();
            isPlaying=false;
        }

    }

    private void start(){
        if(mp!=null&&!mp.isPlaying()){
            mp.start();
            isPlaying=true;
        }
    }


    private void stop(){
        mp.release();
        mp.stop();
        isPlaying=false;
    }


    @Override
    public void HandleMessage(String response) {    //网络请求返回的数据源
        //Log.i("测试二","回传数据"+response);
        DataAnalysis(response);
        DataAnalysisUlr(response);

    }


    @Override
    public void onCompletion(MediaPlayer mp) {
       // Log.i("测试二","回调音乐播放"+mysongs.size());
        if(number<mysongs.size()-1&&!iscirculation){
            number++;
        }
        if(israndom){
            number= (int) (Math.random()*mysongs.size());
        }
        Songs song = mysongs.get(number);
        //Log.i("测试二","回调第"+number+"音乐"+song.getId());
        HashMap<String,String> map = new HashMap<>();
        map.put("id",song.getId());
        LyricView lyricView = new LyricView(this);
        lyricView.setContext(this);
        lyricView.networkquest(map);
        RecycleradapterSongs.setNumberonplayingid(number);
        FragmentPlayingSongs.setSongid(song.getId());
        String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL+"?id="+song.getId();
        networkRequest.sendGetNetRequest(url);


    }

    @Override
    public void check(int postion) {
        String id = mysongs.get(postion).getId();
        String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL+"?id="+id;
        networkRequest.sendGetNetRequest(url);
        FragmentPlayingSongs.setSongid(id);
        RecycleradapterSongs.setNumberonplayingid(postion);
        number=postion;
        HashMap<String,String> map = new HashMap<>();
        map.put("id",id);
        LyricView lyricView = new LyricView(this);
        lyricView.setContext(this);
        lyricView.networkquest(map);
        RecycleradapterSongs.setNumberonplayingid(number);
    }
    class Servicebroadcast extends BroadcastReceiver{    //广播接收者
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("测试二","服务的点击事件生效");
                String id = intent.getStringExtra("ID");    //得到点击事件的歌单id
                Log.i("测试流","服务得到的id"+id);
                String url = ApiConfig.BASE_URl+ApiConfig.SONGS+"?id="+id;
                networkRequest.sendGetNetRequest(url);
//                NetworkRequest networkRequest = new NetworkRequest();
//                networkRequest.sendPostNetRequest(url,map);
//                networkRequest.setOnNetworkRequestlistener(
//                        response -> {
//                            //DataAnalysisUlr(response);
//                        }
//                );
            }
        }


        private void DataAnalysis(String data){

            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject playlist = jsonObject.getJSONObject("playlist");
                JSONArray tracks = playlist.getJSONArray("tracks");
                for(int i=0;i<tracks.length();i++){
                    Songs song = new Songs();
                    JSONObject track = tracks.getJSONObject(i);
                    song.setId(track.getString("id"));
                    song.setName(track.getString("name"));
                   // Log.i("测试二","音乐的id："+song.getId());
                    mysongs.add(song);
                  //  Log.i("测试五","音乐的id"+song.getId());
                    songsid.add(song.getId());
                }
                String url = ApiConfig.BASE_URl+ApiConfig.MAIN_URL+"?id="+mysongs.get(0).getId();
                networkRequest.sendGetNetRequest(url);
                HashMap<String,String> map = new HashMap<>();
                map.put("id",mysongs.get(0).getId());
                LyricView lyricView = new LyricView(this);
                lyricView.setContext(this);
                lyricView.networkquest(map);
                RecycleradapterSongs.setNumberonplayingid(0);
                FragmentPlayingSongs.setSongid(mysongs.get(0).getId());
//                FragmentLyrics lyrics = new FragmentLyrics();
//                HashMap<String,String> map = new HashMap<>();
//                map.put("id",mysongs.get(0).getId());
//                lyrics.setMap(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    @Override
    public void onDestroy() {
        if (mp!=null){
            mp.release();
        }
        localBroadcastManager.unregisterReceiver(servicebroadcast);
        super.onDestroy();
    }
    private void DataAnalysisUlr(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
            String url = jsonObject1.getString("url");
            //Log.i("测试二",url);
           // Log.i("测试二","音乐播放器"+mp+"url"+url);
            url=url;
           prepareandplaySongs(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





}