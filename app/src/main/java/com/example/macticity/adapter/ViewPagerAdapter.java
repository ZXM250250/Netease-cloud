package com.example.macticity.adapter;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.macticity.fragmentmain.BlankFragment;
import com.example.macticity.music.Song;
import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {            //拿到了所有的歌曲信息

    private List<Song> songs = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private Context context;
    //private String TAG = "调试";

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior,List<Song> songs, Context context) {
        super(fm, behavior);
        this.songs = songs;
        this.context = context;
        //Log.i(TAG,"适配器构造器"+songs.size());
        initView();
    }

    private void initView(){    // 给每一个碎片添加内容
        for (int i= 0;i<songs.size();i++){
            BlankFragment fragment = new BlankFragment(songs.get(i));
            fragmentList.add(fragment);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }



    @Override
    public int getCount() {
        return fragmentList.size();
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return songs.get(position).getNickname();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

}
