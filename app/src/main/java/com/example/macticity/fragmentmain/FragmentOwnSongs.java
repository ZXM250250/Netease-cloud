package com.example.macticity.fragmentmain;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.acticity.R;
import com.example.acticity.databinding.FragmentOwnSongsBinding;
import com.example.macticity.adapter.RecycleradapterMainSongs;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.mactivity.MainActivity;
import com.example.macticity.music.Mysongs;
import com.example.macticity.tools.NetworkRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FragmentOwnSongs extends Fragment implements NetworkRequest.OnNetworkRequestlistener{

    private FragmentOwnSongsBinding binding;
    private NetworkRequest networkRequest;
    private List<Mysongs> list = new ArrayList<>();
    //private String TAG = "调试";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_own_songs,container,false);
        View view = binding.getRoot();
        networkRequest = new NetworkRequest();
        networkRequest.setOnNetworkRequestlistener(this::DataAnalysisMobile);
        send();
        return view;
    }
    private void initview(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rlOwnSongs.setLayoutManager(linearLayoutManager);

        binding.rlOwnSongs.setAdapter(new RecycleradapterMainSongs(list,getActivity()));
    }

    private void send(){
        String ulr = ApiConfig.BASE_URl+ApiConfig.MAIN_OWN_SONGS;
        HashMap<String,String> map = new HashMap<>();
        map.put("uid", MainActivity.id);
        networkRequest.sendPostNetRequest(ulr,map);

     }
    private void DataAnalysisMobile(String response){

        try {
           // Log.i(TAG,"这个方法执行了吗");
            JSONObject jsonObject = new JSONObject(response);
          //  Log.i(TAG,"这个方法执行了吗"+jsonObject);
            JSONArray jsonArray = jsonObject.getJSONArray("playlist");
            for (int i = 0;i<jsonArray.length();i++){
                 Mysongs mysongs = new Mysongs();
                JSONObject jsonObjec = jsonArray.getJSONObject(i);
                JSONObject creator = jsonObjec.getJSONObject("creator");
                String coverImgUrl = jsonObjec.getString("coverImgUrl");
                String name = jsonObjec.getString("name");
                String id = jsonObjec.getString("id");
                String signature = creator.getString("signature");
                mysongs.setCoverImgUrl(coverImgUrl);
                mysongs.setName(name);
                mysongs.setSignature(signature);
                mysongs.setId(id);
                list.add(mysongs);
                initview();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysisMobile(response);
       // Log.i(TAG,"方法被回调了吗");
    }
}