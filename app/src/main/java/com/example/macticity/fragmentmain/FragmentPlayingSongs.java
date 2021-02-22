package com.example.macticity.fragmentmain;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.acticity.R;
import com.example.acticity.databinding.FragmentPlayingSongsBinding;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.api.GlideRoundTransform;
import com.example.macticity.mactivity.MainActivitySongs;
import com.example.macticity.music.MusicPlaying;
import com.example.macticity.service.MusicService;
import com.example.macticity.tools.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentPlayingSongs extends Fragment implements View.OnClickListener,NetworkRequest.OnNetworkRequestlistener{

    public static FragmentPlayingSongsBinding binding;
    private int time=0;
    private static String songid;
    private static NetworkRequest networkRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_playing_songs,container,false);
            View view = binding.getRoot();
            initView();
            if(MusicService.mp!=null&&MusicService.mp.isPlaying()){
                binding.ivPlaying.setImageResource(R.drawable.module_fragment_pause);
            }
            if(songid!=null){      //重新加载的时候再次显示图片
                networkquest();
            }
        return view;
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                if (MusicService.mp!=null){
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    intent.putExtra("action","back");
                    getActivity().startService(intent);
                }else {
                    Toast.makeText(getActivity(),"暂无歌曲",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_playing:
                if(MusicService.mp!=null&&!MusicService.mp.isPlaying()){
                    FragmentSongs.stastanimation();
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    intent.putExtra("action","play");
                    getActivity().startService(intent);
                   // Log.i("测试二","开始播放音乐");
                    binding.ivPlaying.setImageResource(R.drawable.module_fragment_pause);
                }else {
                    if (MusicService.mp==null){
                        Toast.makeText(getActivity(),"暂无可播放音乐，请选择一个歌单",Toast.LENGTH_SHORT).show();
                    }
                  //  Log.i("测试二","暂停音乐");
                    FragmentSongs.stopanimation();//暂停光盘转动
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    intent.putExtra("action","pause");
                    getActivity().startService(intent);
                    binding.ivPlaying.setImageResource(R.drawable.module_main_playing);
                }

                break;
            case R.id.iv_former:
                if (MusicService.mp!=null){
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    intent.putExtra("action","up");
                    getActivity().startService(intent);
                }else {
                    Toast.makeText(getActivity(),"暂无歌曲",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_songs:

                Intent intent = new Intent(getActivity(), MainActivitySongs.class);

                getActivity().startActivity(intent);
                break;

        }
    }

    private static void networkquest(){
       // Log.i("调试一","网络请求第一步");
            String url = ApiConfig.BASE_URl+ApiConfig.MAIN_MUSIC_DETIALS+"?ids="+songid;
            networkRequest.sendGetNetRequest(url);
            //Log.i("调试一","网络请求"+url);

    }

    private void initView(){
        networkRequest = new NetworkRequest();
        binding.rlTvUp.setSelected(true);
        binding.rlTvDown.setSelected(true);
        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        binding.ivBack.setOnClickListener(this::onClick);
        binding.ivPlaying.setOnClickListener(this::onClick);
        binding.ivFormer.setOnClickListener(this::onClick);
        binding.ivSongs.setOnClickListener(this::onClick);
    }

    public static void setSongid(String songid) {
        FragmentPlayingSongs.songid = songid;
        networkquest();
    }

    @Override
    public void HandleMessage(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject songs =  jsonObject.getJSONArray("songs").getJSONObject(0);
            JSONObject ar = songs.getJSONArray("ar").getJSONObject(0);
            JSONObject al = songs.getJSONObject("al");
            String name = songs.getString("name");
            String author = ar.getString("name");
            String picUrl = al.getString("picUrl");
            binding.rlTvUp.setText(name);
            binding.rlTvDown.setText(author);

            RequestOptions myOptions = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(getActivity(), 20));  // 加载圆角图片
            Glide.with(getActivity()).load(picUrl).apply(myOptions).into(binding.ivRlSong);
            //通知数据发生变化
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}