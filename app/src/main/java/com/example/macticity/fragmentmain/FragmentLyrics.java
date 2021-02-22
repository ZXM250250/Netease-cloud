package com.example.macticity.fragmentmain;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.acticity.R;
import com.example.acticity.databinding.FragmentLyricsBinding;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.music.Lyrics;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.view.LyricView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentLyrics extends Fragment {

    //private NetworkRequest networkRequest;
    private FragmentLyricsBinding binding;
    //private static Lyrics lyrics;
   // private HashMap<String,String> map = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //networkRequest = new NetworkRequest();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lyrics,container,false);
        View view = binding.getRoot();
      //  getLyrics();
        //Log.i("测试三","oncreatview方法执行");
        //networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        return view;
    }


//
//    public void getLyrics(){
//        String url = ApiConfig.BASE_URl+ApiConfig.LYRICS;
////       HashMap<String,String> map = new HashMap<>();
////        map.put("id","33894312");
//        Log.i("测试三","得到map");
//        NetworkRequest networkRequest= new NetworkRequest();;
//        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
//        networkRequest.sendPostNetRequest(url,map);
//    }
//
//    @Override
//    public void HandleMessage(String response) {
//        Log.i("测试三","网络请求成功");
//       DataAnalysis(response);
//
//    }
//    private void DataAnalysis(String response){
//
//        try {
//            lyrics = new Lyrics();
//            JSONObject jsonObject = new JSONObject(response);
//            String lrc = jsonObject.getString("lrc");
//            String[] lyric = lrc.split("\\\\n");
//            for (int i = 1;i<lyric.length-2;i++){
//                String[] timp =  lyric[i].split("]");
//                lyrics.lyric_line.add(timp[1]);               //每一行的歌词
//               timp[0]= timp[0].replace("[","");
//               timp[0]=timp[0].substring(0,5);
//                lyrics.line_time.add(timp[0]);                 //每一行的时间
//                Log.i("测试","每一句歌词"+timp[0]);
//            }
//                binding.frmLyrics.setContext(getContext());
//                binding.frmLyrics.setLyrics(lyrics);
////            LyricView lyricView = getActivity().findViewById(R.id.frm_lyrics);
////            lyricView.setContext(getContext());
////            lyricView.setLyrics(lyrics);
//
//
//
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}