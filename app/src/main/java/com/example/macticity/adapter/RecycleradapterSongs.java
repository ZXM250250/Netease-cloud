package com.example.macticity.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.acticity.R;
import com.example.macticity.api.GlideRoundTransform;
import com.example.macticity.music.MusicPlaying;
import com.example.macticity.tools.NetworkRequest;
import java.util.ArrayList;
import java.util.List;

public class RecycleradapterSongs extends RecyclerView.Adapter<RecycleradapterSongs.ViewHolder> {
    private Context context;
    private static int numberonplayingid;
    private List<MusicPlaying> musicPlayings = new ArrayList<>();
    private static NetworkRequest networkRequest;
    //private String TAG ="调试一";
    private  int i=0;
    public static String currentplaysong;
    private static chechsonglistener chechsonglistener;

     class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewName;
        TextView textViewaAuthor;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Log.i("无敌","执行ViewHolder"+i);
            i++;
            linearLayout = itemView.findViewById(R.id.ll_playingsongs);
            imageView= itemView.findViewById(R.id.iv_music_icion);
            textViewaAuthor = itemView.findViewById(R.id.tv_author);
            textViewName = itemView.findViewById(R.id.tv_name);
            itemView.findViewById(R.id.tv_name).setSelected(true);   //TextView跑马灯效果
            itemView.findViewById(R.id.tv_author).setSelected(true);
        }
    }

    public RecycleradapterSongs( List musicPlayings,Context context) {
      this.musicPlayings = musicPlayings;
     // Log.i(TAG,"musicPlayings的大小"+musicPlayings.size());
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.i(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_songs_new,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setIsRecyclable(false);   //防止数据复用发生混乱   再没有设置此属性的时候第75行出现了视图数据复用的情况 造成了数据混乱无法实现只有一行的高亮显示 出现很多重复
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Log.i(TAG,"onBindViewHolder");
                holder.textViewName.setText(musicPlayings.get(position).getMusicname());
                holder.textViewaAuthor.setText(musicPlayings.get(position).getAnthor());
                if(position==numberonplayingid){
                 //   Log.i("卧槽","怎么进来了"+position);
                    currentplaysong = musicPlayings.get(position).getId();
                    holder.linearLayout.setBackgroundResource(R.drawable.main_onplaying_music);
                }else {
                    holder.linearLayout.setBackgroundResource(R.drawable.main_onplaying_default);
                }
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .transform(new GlideRoundTransform(context, 15));  // 加载圆角图片
        Glide.with(context).load(musicPlayings.get(position).getPicUrl()).apply(myOptions).into(holder.imageView);
        holder.linearLayout.setOnClickListener(
                v -> {
                    chechsonglistener.check(position);   //选中歌曲的下标
                        notifyDataSetChanged();
                }
        );

    }



    @Override
    public int getItemCount() {
        return musicPlayings.size();
    }

    public static void setNumberonplayingid(int numberonplayingid) {
        RecycleradapterSongs.numberonplayingid = numberonplayingid;

    }
    public interface chechsonglistener{
         void check(int postion);
    }

    public static void setChechsonglistener(RecycleradapterSongs.chechsonglistener chechsonglistener) {
        RecycleradapterSongs.chechsonglistener = chechsonglistener;
    }
}
