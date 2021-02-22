package com.example.macticity.fragmentmain;
import android.content.Context;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.acticity.R;
import com.example.acticity.databinding.FragmentFrmRecommendationSongsBinding;
import com.example.macticity.adapter.ViewPagerAdapter;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.mactivity.MainActivity;
import com.example.macticity.music.Song;
import com.example.macticity.tools.NetworkRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Frmrecommendationsongs extends Fragment implements NetworkRequest.OnNetworkRequestlistener {
    private NetworkRequest networkRequest;
   // private String TAG = "调试二";
    private FragmentFrmRecommendationSongsBinding binding;
    private HashMap<String,String> map = new HashMap<>();
    private List<Song> songs = new ArrayList<>();
    public static MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_frm_recommendation_songs,container,false);
        View view = binding.getRoot();
        networkRequest = new NetworkRequest();
        map.put("limit","50");
        map.put("orde","new");

        String url = ApiConfig.BASE_URl+ApiConfig.MAIN_SONGS;
        networkRequest.sendPostNetRequest(url,map);
        networkRequest.setOnNetworkRequestlistener(this::DataAnalysissongs);
        return view;
    }

    private void DataAnalysissongs(String response){
        try {
            //Log.i(TAG,"分析数据了吗DataAnalysissongs"+response);
            JSONObject jsonObject1 = new JSONObject(response);
            JSONArray playlists = jsonObject1.getJSONArray("playlists");
            for (int i = 0;i<playlists.length();i++){
            //    Log.i(TAG,"执行了吗"+i);
                Song song = new Song();
                JSONObject play = playlists.getJSONObject(i);
                song.setSongid(play.getString("id"));
                song.setCoverImgUrl(play.getString("coverImgUrl"));
                JSONObject creator = play.getJSONObject("creator");
                song.setNickname(creator.getString("nickname"));
                songs.add(song);
            //    Log.i(TAG,song.getNickname()+song.getSongid());
            }
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initView(){
        binding.vpRecomedndations.setAdapter(new ViewPagerAdapter(mainActivity.getSupportFragmentManager(),ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,songs,getActivity()));
        binding.tbRecommendations.setupWithViewPager(binding.vpRecomedndations);
    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysissongs(response);
      //  Log.i(TAG,"方法被回调了吗"+response);

    }
}