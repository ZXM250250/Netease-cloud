package com.example.macticity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.acticity.R;
import com.example.acticity.databinding.FragmentModuleLoginBackBinding;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.tools.ToolsLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Module_fragment_login_change_password extends Fragment implements View.OnClickListener,NetworkRequest.OnNetworkRequestlistener{

    private NetworkRequest networkRequest;
    private FragmentModuleLoginBackBinding binding;
    private Module_fragment_login_verify fragment;
    private boolean ischange =false;
    private ToolsLogin toolsLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_module_login_back,container,false);
        View view = binding.getRoot();
        toolsLogin = new ToolsLogin();
        networkRequest = new NetworkRequest();
        binding.btnMobileCode.setOnClickListener(this::onClick);
        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mobile_code:
             //   Log.i("测试七","点击事件生效");
                if (!ischange){
                    checkMobile(binding.etMobile.getText().toString());

                }else {
                    //修改密码
                changePassword();
                }

                break;
        }
    }

    private void DataAnalysisMobile(String response){

    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysisMobile(response);
        //Log.i("测试七","开始修改密码返回数据"+response);
    }


    public void checkMobile(String phone){
        Log.i("哈哈哈","手机号长度"+phone.length());
        if (phone.length()==11){
          //  Log.i("哈哈哈","checkMobile手机号长度"+phone.length());
            //phone_map.put("phone",binding.etMobile.getText().toString());
            String url = ApiConfig.BASE_URl+ApiConfig.MOBILE_CTCODE+"?phone="+phone;
            networkRequest.sendGetNetRequest(url);
            ischange=true;
            binding.btnMobileCode.setText("修改密码");
            binding.tvCheck.setText("验证码已发送至"+binding.etMobile.getText().toString()+"请注意查收");
        }else {
            Toast.makeText(getActivity(),"手机号输入错误！",Toast.LENGTH_SHORT).show();
        }
    }

    public void changePassword(){
       // Log.i("测试七","开始修改密码");
        String url = ApiConfig.BASE_URl+ApiConfig.MAIN_REGISTER+"?phone="+binding.etMobile.getText().toString()
                +"&password="+binding.etPassword.getText().toString()+"&captcha="+binding.etYanz.getText().toString();
        networkRequest.sendGetNetRequest(url);
        toolsLogin.destoryFragment(getActivity(),this);
    }
}