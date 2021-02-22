package com.example.macticity.fragmentmain;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.acticity.R;
import com.example.acticity.databinding.FragmentBlankBinding;
import com.example.macticity.api.GlideRoundTransform;
import com.example.macticity.music.Song;
import com.example.macticity.service.MusicService;

import java.util.ArrayList;
import java.util.List;


public class BlankFragment extends Fragment {
    private FragmentBlankBinding blankBinding;
    private Song song;
    private LocalBroadcastManager localBroadcastManager;
    public BlankFragment(Song song) {
        this.song = song;
    }

    public BlankFragment(){    //这个构造方法并不知道是何处调用   我自己并没有用  但是  报错信息说缺少这个构造方法

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        blankBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_blank,container,false);
        View view = blankBinding.getRoot();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        if(song!=null){    //屏蔽空参构造造成空指针错误
            initView();
        }

        return view;
    }

    private void initView() {
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .transform(new GlideRoundTransform(getActivity(), 20));  // 加载圆角图片
        Glide.with(getActivity()).load(song.getCoverImgUrl()).apply(myOptions).into(blankBinding.ivBlankMain);
        blankBinding.tvBlankMain.setText(song.getNickname());
        blankBinding.ivBlankMain.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getActivity(),MusicService.class);
                    getActivity().startService(intent);
                    Log.i("测试流","服务被啦起来了"+song.getSongid());
                    Intent intent1 = new Intent("action.ID");
                    intent1.putExtra("ID",song.getSongid());
                    localBroadcastManager.sendBroadcast(intent1);
                }
        );
    }

}