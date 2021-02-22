package com.example.macticity.view;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.macticity.api.ApiConfig;
import com.example.macticity.music.Lyrics;
import com.example.macticity.tools.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LyricView extends View implements NetworkRequest.OnNetworkRequestlistener {

    //private String TAG = "测试三";
    private Paint paint;
    //private int lastx;
    private int lasty;
    private int memberoffsety=0;
    private int offsety;
    private Paint mTextPaint;
    private LocalBroadcastManager localBroadcastManager;
    private static Lyrics lyrics = new Lyrics();  //忘了使用单例模式   这个改为静态属性也可表示唯一共性
    private static Context context;
    private IntentFilter intentFilter;
    private timechange timechange;
    private static int currentlyric=0;
    private int timeaaa=0;
    private int Postion2=0;
    //private Scroller scroller;
    private static int NUMBER=-1;
    public void setContext(Context context) {
        this.context = context;
    }


    public void setLyrics(Lyrics lyrics) {
        this.lyrics = lyrics;
        invalidate();
       // Log.i(TAG," setLyrics"+lyrics.lyric_line.size());

    }



    public LyricView(Context context) {
        super(context);
        initpaint();

    }


    public LyricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initpaint();

    }


    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initpaint();

    }




    public void initpaint(){     //平民画笔
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        //  高亮画笔
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(60);


    }


    private void rigsterbrodcaster(){
        //Log.i(TAG,"注册广播接收器之前context是否为空"+context);
        if(context!=null&&timeaaa==0){  //保证广播接收器只会被注册一次
            timeaaa++;
           // Log.i(TAG,"注册广播接收器");
            intentFilter = new IntentFilter();
            intentFilter.addAction("add.action");
            timechange = new timechange();
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
            localBroadcastManager.registerReceiver(timechange,intentFilter);
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // Log.i(TAG,"画歌词");
        Drawlyrics(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y= (int) event.getY();
        //boolean result = true;
      //  Log.i(TAG,"子view有点击事件吗");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lasty =y;
                break;
            case MotionEvent.ACTION_MOVE:
                  offsety= y - lasty;
//                 FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
//                 layoutParams.topMargin=getTop()+offsety;
//                 setLayoutParams(layoutParams);
               // layout(getLeft(),getTop()+offsety,getRight(),getBottom()+offsety);
                //ObjectAnimator.ofFloat(this,"translationY",getTop(),getTop()+offsety).setDuration(100).start();
                    if(Math.abs(offsety)>=50&&memberoffsety!=offsety){
                        scrollBy(0,-offsety/10);
                    }
                    memberoffsety =offsety;





                break;
            case MotionEvent.ACTION_UP:
//                if(Math.abs(y-lasty)<50){
//                    result = false;
//                }

                break;

        }

        return true;
    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysis(response);
    }

//  private Handler handler = new Handler(){
//      @Override
//      public void handleMessage(@NonNull Message msg) {
//          super.handleMessage(msg);
//          currentlyric = (int) msg.obj;
//          invalidate();
//      }
//  };

    public void networkquest(HashMap map){
        NetworkRequest networkRequest = new NetworkRequest();
        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        String url = ApiConfig.BASE_URl+ApiConfig.LYRICS+"?id="+map.get("id");
        networkRequest.sendGetNetRequest(url);
    }
    class timechange extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra("time");
                //Log.i(TAG,"歌词界面接收到广播");
            for(int i=1;i<lyrics.line_time.size();i++){
                if(lyrics.line_time.get(i).compareTo(time)>0){
                   currentlyric = i-1;
                   invalidate();
                    break;
                }
            }
//            new Thread(
//                    ()->{
//                        String time = intent.getStringExtra("time");
//                        Log.i("测试","歌词界面接收到广播");
//                        for(int i=1;i<lyrics.line_time.size();i++){
//                            if(lyrics.line_time.get(i).compareTo(time)>1){
//                                currentlyric = i-1;
//                                postInvalidate();
//                            }
//                        }
//
//                    }
//            ).start();
        }
    }
    private void DataAnalysis(String response){

        try {
            lyrics = new Lyrics();
            JSONObject jsonObject = new JSONObject(response);
            String lrc = jsonObject.getString("lrc");
            String[] lyric = lrc.split("\\\\n");
            for (int i = 1;i<lyric.length;i++){
                String[] timp =  lyric[i].split("]");
              //  Log.i(TAG,"歌词"+lyric[i]);
                if (timp.length>1){
                    //if (timp[1].length()>=40){
                        //division(timp[1]);
                   // }else {
                        lyrics.lyric_line.add(timp[1]);
                   // }
                    timp[0]= timp[0].replace("[","");
                    timp[0]=timp[0].substring(0,5);
                    lyrics.line_time.add(timp[0]);
                }
                             //每一行的歌词
                             //每一行的时间
                Log.i("测试","每一句歌词"+timp[0]);
            }
           setLyrics(lyrics);

//            LyricView lyricView = getActivity().findViewById(R.id.frm_lyrics);
//            lyricView.setContext(getContext());
//            lyricView.setLyrics(lyrics);





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private int drwaLirc(String data,Canvas canvas){    // 分割高亮显示长句歌词
        NUMBER++;
        Log.i("流弊","高亮多行多行"+NUMBER);
        if(data.length()>=30){
            canvas.drawText(data.substring(0,30),getWidth()/2,getHeight()/2+NUMBER*100,mTextPaint);
            return drwaLirc(data.substring(30,data.length()),canvas);
        }else {
            canvas.drawText(data.substring(0,data.length()),getWidth()/2,getHeight()/2+NUMBER*100,mTextPaint);
            return NUMBER;
        }
    }

    //    private int drwaComLircup(String data,Canvas canvas,int i){
//        NUMBER++;
//        String[] temp=null;
//        while (data.length()>30){
//            temp[NUMBER] = data.substring(0,30);
//            data=data.substring(30,data.length());
//            NUMBER++;
//        }
//        for(int j=0;j<temp.length;j++){
//
//        }
//    }
    private int drwaComLirc(String data,Canvas canvas,int i){   // 分割平民显示长句歌词
        NUMBER++;
        Log.i("流弊","普通多行"+NUMBER);
        if(data.length()>=30){
            canvas.drawText(data.substring(0,30),getWidth()/2,(getHeight()/2)+(i-currentlyric)*100,paint);
            i++;
            return drwaComLirc(data.substring(30,data.length()),canvas,i);
        }else {
            canvas.drawText(data.substring(0,data.length()),getWidth()/2,(getHeight()/2)+(i-currentlyric)*100,paint);
            return NUMBER;

        }


    }

    private void Drawlyrics(Canvas canvas){   //注册太多次广播接收器会造成卡顿且闪退 此处就是Ondraw方法
        rigsterbrodcaster();
        int postinup = 0;
        int postindowu=0;
        int postion=0;
        int uplirc =0;;
        // Log.i(TAG,"广播接收器已经注册"+"歌词的大小"+lyrics.lyric_line.size());
        for (int i=0;i<lyrics.lyric_line.size();i++){
            if(i==currentlyric){    //高亮显示歌词画法
                if(lyrics.lyric_line.get(i).length()>30){
                    postion=drwaLirc(lyrics.lyric_line.get(i),canvas);
                    Log.i("流弊","高亮postion位置"+postion);
                    NUMBER=-1;
                }else {
                    canvas.drawText(lyrics.lyric_line.get(i),getWidth()/2,getHeight()/2,mTextPaint);

                }
            }else {           //平民歌词画法
                if(i-currentlyric<0){    //相对位置以上采用倒叙画法
                    uplirc = Math.abs(i-currentlyric);
                    if(Math.abs(i-postinup)>0){
                        postinup= drwaComLircup(lyrics.lyric_line.get(uplirc),canvas,i+postinup)+postinup;
                        NUMBER=-1;
                    }

//                    if(lyrics.lyric_line.get(i).length()>30){
//                        drwaComLirc(lyrics.lyric_line.get(i),canvas,i-postinup);
//                        NUMBER=-1;
//                    }else {
//                        canvas.drawText(lyrics.lyric_line.get(i),getWidth()/2,(getHeight()/2)+(i-currentlyric)*100,paint);
//                    }
                }else{
                    if(lyrics.lyric_line.get(i).length()>30){
                        postindowu= drwaComLirc(lyrics.lyric_line.get(i),canvas,i+postindowu+postion)+postindowu;
                        NUMBER=-1;
                    }else {
                        canvas.drawText(lyrics.lyric_line.get(i),getWidth()/2,(getHeight()/2)+(i-currentlyric+postindowu+postion)*100,paint);
                    }
                }


            }
            Log.i("流弊","一个循环加一次"+i);
            // Log.i(TAG,lyrics.lyric_line.get(i)+"有无歌词");
        }

    }

    private int drwaComLircup(String data, Canvas canvas, int i) {
        List<String> temp = new ArrayList<>();
        if(data.length()>30) {
            Log.i("最后的黄昏","上部普通多行测试");
            while (data.length() > 30) {
                temp.add(data.substring(0, 30));
                data = data.substring(30, data.length());
                NUMBER++;
            }
            temp.add(data.substring(0,data.length()));
            NUMBER++;
            for (int j = temp.size() - 1; j >= 0; j--) {
                canvas.drawText(temp.get(j), getWidth() / 2, (getHeight() / 2) + (-i ) * 100, paint);
                i++;
            }
            temp.clear();
        }else {
            canvas.drawText(data, getWidth() / 2, (getHeight() / 2) + (-i ) * 100, paint);
            NUMBER++;
        }
        return NUMBER;
    }
}
