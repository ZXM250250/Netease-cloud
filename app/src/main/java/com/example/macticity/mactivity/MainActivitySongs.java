package com.example.macticity.mactivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.acticity.R;
import com.example.acticity.databinding.ActivityMainSongsBinding;
import com.example.macticity.adapter.RecycleradapterSongs;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.music.MusicPlaying;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.tools.ToolsLogin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivitySongs extends AppCompatActivity implements NetworkRequest.OnNetworkRequestlistener{
    private  static List<String> songsid = new ArrayList<>();
    private List<MusicPlaying> musicPlayings = new ArrayList<>();
    private static NetworkRequest networkRequest;
    private ActivityMainSongsBinding binding;
    private ToolsLogin toolsLogin;
    private RecycleradapterSongs recycleradapterSongs;
  //  private String TAG ="调试一";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_songs);
        initView();
    }

    private void initView() {
       // Log.i(TAG,"initView()"+songsid.size());
        networkRequest = new NetworkRequest();
        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        toolsLogin = new ToolsLogin();
        toolsLogin.FullScreen(this);
        networkquest();
    }



    private void DataAnalysis(String response){
        //Log.i(TAG,"返回的数据"+response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray songs =  jsonObject.getJSONArray("songs");
            for(int i=0;i<songs.length();i++){
                MusicPlaying musicPlaying = new MusicPlaying();
                JSONObject song = songs.getJSONObject(i);
                JSONObject ar = song.getJSONArray("ar").getJSONObject(0);
                JSONObject al = song.getJSONObject("al");
                String id = song.getString("id");
                String name = song.getString("name");
                String author = ar.getString("name");
                String picUrl = al.getString("picUrl");
                musicPlaying.setId(id);
                musicPlaying.setAnthor(author);
                musicPlaying.setMusicname(name);
                musicPlaying.setPicUrl(picUrl);
                musicPlayings.add(musicPlaying);
            }

            //通知数据发生变化
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void setSongsid(List<String> songsid) {
        MainActivitySongs.songsid = songsid;
    }

    private static void networkquest(){
        String url = ApiConfig.BASE_URl+ApiConfig.MAIN_MUSIC_DETIALS+"?ids=";
        //Log.i("调试一","网络请求第一步");
        for (int i=0;i<songsid.size();i++){        //  要一次请求完所有歌曲数据   单个id请求回调时间差异不同 造成数据混乱
            url = url+songsid.get(i);
            if(i!=songsid.size()-1){
                url = url+",";
            }
            networkRequest.sendGetNetRequest(url);
            //Log.i("调试一","网络请求"+url);
        }
    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysis(response);
       // Log.i(TAG,"songsid的大小"+songsid.size()+"musicPlayings"+musicPlayings.size());
        recycleradapterSongs=new RecycleradapterSongs(musicPlayings,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvSongs.setLayoutManager(linearLayoutManager);
        binding.rvSongs.setAdapter(recycleradapterSongs);
    }
}