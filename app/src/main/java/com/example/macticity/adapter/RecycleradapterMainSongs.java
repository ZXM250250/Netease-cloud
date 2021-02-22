package com.example.macticity.adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.acticity.R;
import com.example.macticity.api.GlideRoundTransform;
import com.example.macticity.music.Mysongs;
import com.example.macticity.service.MusicService;

import java.util.List;

public class RecycleradapterMainSongs extends RecyclerView.Adapter<RecycleradapterMainSongs.ViewHolder>{

   private List<Mysongs> mysongsList;
    private Context context;
    //private String TAG = "调试";
    private LocalBroadcastManager localBroadcastManager;
    static class ViewHolder extends RecyclerView.ViewHolder{
         ImageView imageView;
         TextView textViewup;
         TextView textViewdown;
         ImageView imageViewplay;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
            imageView = itemView.findViewById(R.id.iv_rl_song);
            textViewup = itemView.findViewById(R.id.rl_tv_up);
            textViewdown = itemView.findViewById(R.id.rl_tv_down);
            imageViewplay  = itemView.findViewById(R.id.play);
       }
   }

    public RecycleradapterMainSongs(List<Mysongs> mysongsList,Context context) {
        this.mysongsList = mysongsList;
        this.context = context;
       // Log.i(TAG,"适配器数据没有问题"+mysongsList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.module_main_own_songs,parent,false);
        //Log.i(TAG,"视图返回去了吗");
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                Mysongs mysongs = mysongsList.get(position);
                holder.textViewdown.setSelected(true);
                holder.textViewup.setSelected(true);
                holder.textViewup.setText(mysongs.getName());
                holder.textViewdown.setText(mysongs.getSignature());
                RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .transform(new GlideRoundTransform(context, 15));  // 加载圆角图片
        Glide.with(context).load(mysongs.getCoverImgUrl()).apply(myOptions).into(holder.imageView);
            holder.imageViewplay.setOnClickListener(    //点击事件的监听
                    view->{
                        Intent intent1 = new Intent(context, MusicService.class);
                        context.startService(intent1);
                        Intent intent = new Intent("action.ID");
                        intent.putExtra("ID",mysongs.getId());
                        Log.i("测试二","音乐的id"+mysongs.getId());
                        localBroadcastManager.sendBroadcast(intent);
                    }
            );
       // Log.i(TAG,"视图绑定了吗");
    }



    @Override
    public int getItemCount() {
        return mysongsList.size();
    }


}
