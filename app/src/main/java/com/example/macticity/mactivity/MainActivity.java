package com.example.macticity.mactivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.acticity.R;
import com.example.acticity.databinding.ActivityMain2Binding;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.fragmentmain.FragmentOwnSongs;
import com.example.macticity.fragmentmain.FragmentPlayingSongs;
import com.example.macticity.fragmentmain.Frmrecommendationsongs;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.tools.ToolsLogin;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NetworkRequest.OnNetworkRequestlistener {
    private static ActivityMain2Binding binding;
    private ToolsLogin toolsLogin;
    public static String id;
    private static String avatarUrl;
    private static String nickname;
    private View view;
//  private MediaPlayer mp;
    private String TAG ="测试";
    //mp.setDataSource("http://m7.music.126.net/20210130194058/88adfb8fd2d63d79427ee4dd62f883d0/ymusic/0fd6/4f65/43ed/a8772889f38dfcb91c04da915b301617.mp3");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2);
        Frmrecommendationsongs.mainActivity=this;
        initView();
        //playingmusic();
    }
    private void initView(){    //加载三个碎片
        id=getIntent().getStringExtra("id");
        nickname = getIntent().getStringExtra("nickname");
        avatarUrl = getIntent().getStringExtra("avatarUrl");
        toolsLogin = new ToolsLogin();
        toolsLogin.FullScreen(this);
        view=binding.ngvBack.inflateHeaderView(R.layout.nav_header);
        view.findViewById(R.id.tv_drop_out).setOnClickListener(this::onClick);
        Fragment fragment1 = new FragmentOwnSongs();
        Fragment fragment = new Frmrecommendationsongs();
        Fragment fragment2 = new FragmentPlayingSongs();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frm_recommendation_songs,fragment);
        transaction.replace(R.id.frm_own_songs,fragment1);
        transaction.replace(R.id.frm_onplaying,fragment2);
        transaction.commit();
        binding.frmOnplaying.setOnClickListener(this::onClick);
        binding.ivDetails.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frm_onplaying:
                Intent intent = new Intent(this,MainActivityPlayMusic.class);
                startActivity(intent);
                break;
            case R.id.iv_details:
                Glide.with(this).load(avatarUrl).into((ImageView) view.findViewById(R.id.iv_header));
                TextView textView = view.findViewById(R.id.tv_nickname);
                textView.setText(nickname);
                binding.dropdownMenu.open();
                break;
            case R.id.tv_drop_out:
                        String url = ApiConfig.BASE_URl+ApiConfig.DROP_OUT;
                NetworkRequest networkRequest = new NetworkRequest();
                networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
                networkRequest.sendGetNetRequest(url);
                break;



        }
    }

    @Override
    public void HandleMessage(String response) {
        Toast.makeText(this,"退出成功",Toast.LENGTH_SHORT).show();
        finish();
    }
//
//        private void playingmusic(){
//            new Thread(
//                    () -> {
//                     mp = new MediaPlayer();
//                        try {
//                            mp.setDataSource("http://m7.music.126.net/20210130194058/88adfb8fd2d63d79427ee4dd62f883d0/ymusic/0fd6/4f65/43ed/a8772889f38dfcb91c04da915b301617.mp3");
//                            mp.prepare();
//                            Log.i(TAG,"sd"+mp.isPlaying());
//                            mp.start();
//                            Log.i(TAG,"开始后"+mp.isPlaying());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//            ).start();
//        }

//
//    private interface OnUrlchangelistener{
//        void changeUrl(List<String> urls);
//    }



}