package com.example.macticity.fragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import com.example.acticity.R;
import com.example.acticity.databinding.FragmentModuleLoginMainBinding;

import com.example.macticity.api.ApiConfig;
import com.example.macticity.mactivity.MainActivity;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.tools.ToolsLogin;

import org.json.JSONException;
import org.json.JSONObject;


public class Module_fragment_login_main extends android.app.Fragment implements View.OnClickListener,NetworkRequest.OnNetworkRequestlistener{
   // private String TAG = "调试";
    private FragmentModuleLoginMainBinding binding;
    private ToolsLogin toolsLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        toolsLogin = new ToolsLogin();
        // 1、对布局需要绑定的内容进行加载
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_module_login_main, container, false);
        // 2、获取到视图
        SharedPreferences sp = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        String phone = sp.getString("phone","敬请登录");
        binding.tvPhone.setText(phone);
        binding.tvRewrite.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.setData(new Module_fragment_login_main());
        View view = binding.getRoot();
        return view;
    }

    public void toMobilelogin(){
        Fragment fragment = new Module_fragment_login_verify();
        toolsLogin.switchFragment(getActivity(),R.id.frm_login,fragment);
    }

      @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_rewrite:
               toMobilelogin();
                break;
            case R.id.btn_login:
                if(binding.cbBtn.isChecked()){
                    read();
                }else {
                    Toast.makeText(getActivity(),"你必须勾选协议才能登录",Toast.LENGTH_SHORT).show();
                }

                break;
       }
   }
   private void read(){
      // Log.i("无语","读取数据");
       SharedPreferences sp = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
      // Log.i("无语","有没有");
       String phone = sp.getString("phone","00");
       String password = sp.getString("password","00");
      // Log.i("无语",phone+password);
       if(!phone.equals("00")&&!password.equals("00")){
           NetworkRequest networkRequest = new NetworkRequest();
           networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
           String url = ApiConfig.BASE_URl+ApiConfig.MOBILE_LOGIN+"?phone="+phone+"&password="+password;
           networkRequest.sendGetNetRequest(url);
       }else {
           Fragment fragment = new Module_fragment_login();
           toolsLogin.switchFragment(getActivity(),R.id.frm_login,fragment);
       }

   }
    private void DataAnalysisMobile(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
          //  Log.i("哈哈哈哈","请求码"+code);
            if(code.equals("200")) {
                JSONObject account = jsonObject.getJSONObject("account");
                JSONObject profile = jsonObject.getJSONObject("profile");
                String id = account.getString("id");
                String avatarUrl = profile.getString("avatarUrl");
                String nickname = profile.getString("nickname");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //Log.i("哈哈哈哈", "登录换活动");
                intent.putExtra("id", id);
                intent.putExtra("avatarUrl", avatarUrl);
                intent.putExtra("nickname", nickname);
                getActivity().startActivity(intent);
            }else {
                Toast.makeText(getActivity(),"密码输入错误",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysisMobile(response);
    }
}