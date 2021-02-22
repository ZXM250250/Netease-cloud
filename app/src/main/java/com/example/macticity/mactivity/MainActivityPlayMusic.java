package com.example.macticity.mactivity;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.View;


import android.widget.SeekBar;
import android.widget.Toast;


import com.example.acticity.R;

import com.example.acticity.databinding.ActivityMainPlayMusicBinding;
import com.example.macticity.adapter.RecycleradapterSongs;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.fragmentmain.FragmentLyrics;
import com.example.macticity.fragmentmain.FragmentPlayingSongs;
import com.example.macticity.fragmentmain.FragmentSongs;
import com.example.macticity.service.MusicService;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.tools.ToolsLogin;
public class MainActivityPlayMusic extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,NetworkRequest.OnNetworkRequestlistener {
    private ActivityMainPlayMusicBinding binding;
   // private String TAG = "调试";
    private ObjectAnimator animator;
    private ToolsLogin toolsLogin;
    private IntentFilter intentFilter;
    private dataChangeReceive dataChangeReceive;
    private boolean isPausing = false, isPlaying = false; // 音乐暂停状态，音乐第一次播放之后变为true
//    private float starty;
//    private float endy;
//    private float lasty;
    private int fragmenttime =0;   //用来记录碎片的切换
    private Fragment fragment;
    private static int random=0;
    private static int buttontime=0;//是否循环播放
    private Fragment fragment1;
    private LocalBroadcastManager localBroadcastManager;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolsLogin = new ToolsLogin();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_play_music);
        toolsLogin.FullScreen(this);
        initview();
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initview(){
         fragment1 = new FragmentSongs();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frm_music_songs,fragment1);
        transaction.commit();
        binding.ivBacktomain.setOnClickListener(this::onClick);
        binding.sbProgress.setOnSeekBarChangeListener(this);
        binding.ivPlayingBack.setOnClickListener(this::onClick);
        binding.ivPlayingFreshen.setOnClickListener(this::onClick);
        binding.ivPlayingPlay.setOnClickListener(this::onClick);
        binding.ivPlayingRandom.setOnClickListener(this::onClick);
        binding.ivPlayingUp.setOnClickListener(this::onClick);
        binding.frmMusicSongs.setOnClickListener(this::onClick);
        binding.ivAdore.setOnClickListener(this::onClick);
        binding.ivPlayAdd.setOnClickListener(this::onClick);
        binding.ivSongs.setOnClickListener(this::onClick);
        if(MusicService.mp!=null&&MusicService.mp.isPlaying()){
            binding.ivPlayingPlay.setImageResource(R.drawable.module_main_playing_pause);
           FragmentSongs.stastanimation();
        }

        //注册一个广播接收者
        intentFilter = new IntentFilter();
        intentFilter.addAction("add.action");
        dataChangeReceive = new dataChangeReceive();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(dataChangeReceive,intentFilter);
        //FrameLayouttouch();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            Intent intent = new Intent(this,MusicService.class);
            intent.putExtra("action","onchange");
            intent.putExtra("progress",progress);
            startService(intent);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Intent intent = new Intent(this,MusicService.class);
                    intent.putExtra("action","ontouch");
                    startService(intent);
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Intent intent = new Intent(this,MusicService.class);
                intent.putExtra("action","stoptouch");
                startService(intent);

    }

    @Override
    public void HandleMessage(String response) {
        Toast.makeText(this,"已添加到喜欢的歌曲",Toast.LENGTH_SHORT).show();
    }

    class dataChangeReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.i(TAG,"播报成功");
            String time = intent.getStringExtra("time");
            int duration = intent.getIntExtra("duration",0);
            int current = intent.getIntExtra("currentMs",0);


            if (duration!=0){              //在最开始时设置歌曲的总长度和最终时间
                binding.sbProgress.setMax(duration);
                int sec = duration / 1000;
                int min = sec / 60;
                sec -= min * 60;
                String musicTime = String.format("%02d:%02d", min, sec);
                binding.tvProgressTotal.setText(musicTime);
            }
            binding.sbProgress.setProgress(current);
            binding.tvProgressCurrent.setText(time);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_playing_random:
                if (MusicService.mp!=null){
                    Intent intent = new Intent(this, MusicService.class);
                    if (random%2==0){
                        intent.putExtra("action","beginrandom");
                        Toast.makeText(this,"报告大人：已开启随机播放模式",Toast.LENGTH_SHORT).show();
                    }else {
                        intent.putExtra("action","overrandom");
                        Toast.makeText(this,"报告大人：已关闭随机播放模式",Toast.LENGTH_SHORT).show();
                    }
                    random++;
                    startService(intent);
                }else {
                    Toast.makeText(this,"报告大人：暂无可随机歌曲",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_playing_back:    //播放上一首
                if (MusicService.mp!=null){
                    Intent intent = new Intent(this, MusicService.class);
                    intent.putExtra("action","back");
                    startService(intent);
                }else {
                    Toast.makeText(this,"报告大人：暂无可播放歌曲",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_playing_play:
                if(MusicService.mp!=null&&!MusicService.mp.isPlaying()){
                    FragmentSongs.stastanimation();
                    Intent intent = new Intent(this, MusicService.class);
                    intent.putExtra("action","play");
                    startService(intent);
                   // Log.i("测试二","开始播放音乐");
                    binding.ivPlayingPlay.setImageResource(R.drawable.module_main_playing_pause);
                }else {
                    if (MusicService.mp==null){
                        Toast.makeText(this,"暂无可播放音乐，请选择一个歌单",Toast.LENGTH_SHORT).show();
                    }
                   // Log.i("测试二","暂停音乐");
                    FragmentSongs.stopanimation();//暂停光盘转动
                    Intent intent = new Intent(this, MusicService.class);
                    intent.putExtra("action","pause");
                    startService(intent);
                    binding.ivPlayingPlay.setImageResource(R.drawable.module_main_play_songs);
                }
                break;
            case R.id.iv_playing_up:    //播放下一首
                if (MusicService.mp!=null){
                    Intent intent = new Intent(this, MusicService.class);
                    intent.putExtra("action","up");
                    startService(intent);
                }else {
                    Toast.makeText(this,"暂无歌曲",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_playing_freshen:
                if (MusicService.mp!=null){
                    Intent intent = new Intent(this, MusicService.class);
                    if (buttontime%2==0){
                        intent.putExtra("action","circulation");
                        Toast.makeText(this,"大人已开启循环播放模式",Toast.LENGTH_SHORT).show();
                    }else {
                        intent.putExtra("action","overcirculation");
                        Toast.makeText(this,"大人已关闭循环播放模式",Toast.LENGTH_SHORT).show();
                    }
                    buttontime++;
                    startService(intent);
                }else {
                    Toast.makeText(this,"报告大人暂无钟爱歌曲循环",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.frm_music_songs:
              //  Log.i(TAG,"父碎片的点击事件触发了吗");
                if(fragmenttime%2==0){
                    fragment = new FragmentLyrics();
                }else {
                    fragment = new FragmentSongs();

                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frm_music_songs,fragment);
                transaction.commit();
                fragment = null;
                fragmenttime++;
                break;
            case R.id.iv_backtomain:
                finish();
                break;
            case R.id.iv_adore:   //喜欢此音乐
                AdoreMusic();
                break;
            case R.id.iv_play_add:
              Toast.makeText(this,"懒鬼正在睡懒觉，快去叫他写代码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_songs:
                Intent intent = new Intent(this, MainActivitySongs.class);
                startActivity(intent);
                break;
        }

    }
        private void AdoreMusic(){
        if(RecycleradapterSongs.currentplaysong!=null){
            String url = ApiConfig.BASE_URl+ApiConfig.MUSIC_LIKE+"?id="+RecycleradapterSongs.currentplaysong;
            NetworkRequest networkRequest = new NetworkRequest();
            networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
            networkRequest.sendGetNetRequest(url);

        }else {
            Toast.makeText(this,"看！空气！",Toast.LENGTH_SHORT).show();
        }

        }
// private void FrameLayouttouch(){
//
//        binding.frmMusicSongs.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.i(TAG,"ontouchevent执行了吗");
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        starty = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        endy = event.getY();
//                        float distancemove = endy - starty;
//                        if(distancemove>=5){
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        lasty = event.getY();
//                        float distanceup = lasty - starty;
//                        if(distanceup<=5){
//                            Log.i(TAG,"碎片的点击事件触发了吗");
//                            Fragment fragment = new FragmentLyrics();
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction transaction = fragmentManager.beginTransaction();
//                            transaction.replace(R.id.frm_music_songs,fragment);
//                            transaction.commit();
//                            return true;
//                        }
//
//                        break;
//                }
//                return false;
//            }
//        });
// }


















}