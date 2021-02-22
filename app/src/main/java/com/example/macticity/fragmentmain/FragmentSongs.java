package com.example.macticity.fragmentmain;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.acticity.R;
import com.example.acticity.databinding.FragmentSongsBinding;
import com.example.macticity.mactivity.MainActivityPlayMusic;
import com.example.macticity.service.MusicService;

public  class FragmentSongs extends Fragment  {

    private static ObjectAnimator animator;
    private static FragmentSongsBinding binding;
    private int time=0;
    private static ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_songs,container,false);
        View view = binding.getRoot();
        imageView = container.findViewById(R.id.iv_disk);
            if(MusicService.mp!=null&&MusicService.mp.isPlaying()){
               // Log.i("哈哈","动画执行");
                stastanimation();
            }

        return view;
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void stastanimation() {    //播放的接口回调方法   活动执行碎片里面的动画

        if(animator!=null&&animator.isPaused()){
            //Log.i("哈哈","animator.resume();");
            animator.resume();
//            if(animator.isPaused()){
//                Log.i("哈哈","!animator.isRunning()");
//                animator.start();
//            }
        }else if(binding!=null) {
            animator = ObjectAnimator.ofFloat(binding.ivDisk ,"rotation", 0, 360.0F); // 初始化状态
            animator.setDuration(10000); // 转动时长，10秒
            animator.setInterpolator(new LinearInterpolator()); // 时间函数，有很多类型
            animator.setRepeatCount(-1); // 一直旋转
            animator.start();
        }

        }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void stopanimation(){
        if(animator!=null){
            animator.pause();
        }
    }

    @Override
    public void onDestroy() {
        animator=null;    //界面销毁动画赋予空值  下次启动则可管控
        super.onDestroy();
    }

}