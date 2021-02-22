package com.example.macticity.view;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MyFrameLayout extends FrameLayout {
    private int lastx;
    private int lasty;
    private int originalx;
    private int originaly;
    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




//        @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        float stastx = event.getX();
//        float stasty = event.getY();
//
//        boolean result = false;
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//
//               break;
//            case MotionEvent.ACTION_MOVE:
//
//               break;
//            case MotionEvent.ACTION_UP:
//                if(Math.abs(stastx-event.getX())<=5||Math.abs(stasty-getY())<=5){
//                    Log.i("测试","执行了");
//                    result = true;
//               }
//                break;
//        }
//
//       return result;
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int x= (int) ev.getX();
//        int y= (int) ev.getY();
//        boolean result = true;   //默认拦截
//
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastx =x;
//                lasty =y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                int offsety = y - lasty;
//
//                if (Math.abs(offsety)>5){    //滑动距离够大就不拦截
//
//
//
//
//                    result = false;
//                    getChildAt(0).dispatchTouchEvent(ev);
//                    Log.i("测试","滑动距离大不拦截");
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//
//        }
//        return result;
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // Log.i("嘿嘿","lcmkancsanvjnjncjkmacascascascasm碎片的点击事件有执行吗");
        performClick();   //直接去响应setOnClickListener
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("嘿嘿","事件分发的执行");
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                originalx = (int) ev.getX();
                originaly = (int) ev.getY();
               // Log.i("嘿嘿","事件分发的执行");
                return super.dispatchTouchEvent(ev);

            case MotionEvent.ACTION_MOVE:
                if(Math.abs(originalx-ev.getX())>=50||Math.abs(originaly-ev.getY())>=50){
                    //Log.i("嘿嘿","距离大分发下去");
                    return super.dispatchTouchEvent(ev);
                }
                else {
                   // Log.i("嘿嘿","距离小");
                    return true;
                }

            case MotionEvent.ACTION_UP:
                if(Math.abs(originalx-ev.getX())<=5&&Math.abs(originaly-ev.getY())<=5){
                   onTouchEvent(ev);
                  //  Log.i("嘿嘿","抬起自己解决事件");
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
