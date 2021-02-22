package com.example.macticity.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import java.util.ArrayList;



public class WaveRelativeLayout extends RelativeLayout {     //水波纹的viewgroup

    private ArrayList<Wave> wList; //水波纹集合
    private int X;    //水波纹的初始化的点坐标
    private int Y;

    public WaveRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        wList = new ArrayList<Wave>();
        X= 540;
        Y= 550;
        addWave();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < wList.size(); i++) {     //真实的绘制每一个水波纹
            Wave wave = wList.get(i);
            canvas.drawCircle(wave.pointX, wave.pointY, wave.radius, wave.paint);
        }
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            //刷新数据
            flushData();
            //刷新页面
            invalidate();
            //循环动画
                handler.sendEmptyMessageDelayed(0, 5);
            addWave();

        }



        ;
    };


    //刷新水波纹
    private void flushData() {
        for (int i = 0; i < wList.size(); i++) {

            Wave w = wList.get(i);

            //如果透明度为 0 从集合中删除
            int alpha = w.paint.getAlpha();
            if(alpha == 0){
                wList.remove(i);	//删除i 以后，i的值应该再减1 否则会漏掉一个对象，不过，在此处影响不大，效果上看不出来。
                continue;
            }

            alpha-=2;
            if(alpha<2){
                alpha =0;
            }
            //降低透明度
            w.paint.setAlpha(alpha);
            //扩大半径
            w.radius = w.radius+3;
            //设置半径厚度
            w.paint.setStrokeWidth(10);
        }


    }




    /*
    定义一个水波纹
     */
    private class Wave {
        //圆心
        int pointX;
        int pointY;
        //画笔
        Paint paint;
        //半径
        int radius;
    }

    /*
    添加新的波浪
     */
    private void addWave(){
        if(wList.size() == 0) {
            /*
             * 第一次启动动画
             */
            handler.sendEmptyMessage(0);
        }
        Wave w = new Wave();
        w.pointX = X;
        w.pointY=Y;
        Paint pa=new Paint();
        pa.setColor(colors[(int)(Math.random()*4)]);
        pa.setAntiAlias(true);
        pa.setStyle(Paint.Style.STROKE);
        w.paint = pa;
        wList.add(w);
    }

    //颜色数组
    private int [] colors = new int[]{Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN};


    }

