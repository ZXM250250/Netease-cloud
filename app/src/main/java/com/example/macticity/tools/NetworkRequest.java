package com.example.macticity.tools;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;



public class NetworkRequest extends Activity {
 //   private String TAG = "测试二";

    private OnNetworkRequestlistener onNetworkRequestlistener;







    public void setOnNetworkRequestlistener(OnNetworkRequestlistener onNetworkRequestlistener) {
        this.onNetworkRequestlistener = onNetworkRequestlistener;
    }

   public interface OnNetworkRequestlistener{     //定义一个内部接口，用于网络请求的接口回调
        void HandleMessage(String response);
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //Log.i(TAG,"消息机制被回调了吗"+msg.obj);
            onNetworkRequestlistener.HandleMessage((String) msg.obj);  //接口回调传送返回的数据
        }
    };

    public void sendGetNetRequest(String U) {
        new Thread(
                () -> {
                    try {
                        String responseData;
                        URL url = new URL(U);
                     //   Log.i(TAG,"音乐的url"+url);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        responseData = StreamToString(inputStream);
                        Message message = new Message();
                        message.obj = responseData;
                        handler.sendMessage(message);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();

    }



    private String StreamToString(InputStream inputStream) {           // 将流转为字符串的操作
        StringBuilder sb = new StringBuilder();

        String online;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((online = reader.readLine()) != null) {
                sb.append(online).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }



    public void sendPostNetRequest(String U, HashMap<String, String> params) {

        new Thread(
                () -> {
                    try {
                        String responseData = null;
                        URL url = new URL(U);
                      //  Log.i(TAG, "是空吗？" + U+responseData);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        StringBuilder dataTOwrite = new StringBuilder();
                        for (String key : params.keySet()) {
                            dataTOwrite.append(key).append("=").append(params.get(key)).append("&");
                        }
                       // Log.i(TAG, "返回之前的数据responseData " + "空了吗"+"请求的数据"+dataTOwrite);
                       // Log.i(TAG, "返回之前的数据" + responseData + "空了吗");
                        connection.connect();
                        OutputStream outputStream = connection.getOutputStream();
                       // Log.i(TAG,   "输入流的请求的数据"+dataTOwrite.substring(0, dataTOwrite.length() - 1));
                        outputStream.write(dataTOwrite.substring(0, dataTOwrite.length() - 1).getBytes());
                        Log.i("测试二","网络请求完整url"+url+dataTOwrite.substring(0, dataTOwrite.length() - 1));
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        responseData = StreamToString(inputStream);
                      //  Log.i(TAG, "返回的网络请求之后的数据" + responseData + "空了吗");
                        Message message = new Message();
                        message.obj = responseData;
                        outputStream.flush();
                        inputStream =null;
                        outputStream.close();
                        connection.disconnect();
                        handler.sendMessage(message);
                       // Log.i(TAG, "返回的数据" + responseData + "空了吗");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        ).start();

    }
}