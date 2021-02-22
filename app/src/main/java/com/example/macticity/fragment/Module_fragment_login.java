package com.example.macticity.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.acticity.R;
import com.example.acticity.databinding.FragmentModuleLoginBinding;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.mactivity.MainActivity;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.tools.ToolsLogin;


import org.json.JSONException;
import org.json.JSONObject;


public class Module_fragment_login extends Fragment implements View.OnClickListener,NetworkRequest.OnNetworkRequestlistener{
    private FragmentModuleLoginBinding binding;
    private NetworkRequest networkRequest;
    private ToolsLogin toolsLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_module_login,container,false);
        View view = binding.getRoot();
       initView();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mobile_login:       //post请求有接口缓存    频繁登录很可能会造成错误
                //HashMap<String,String>  datalogin = new HashMap<>();
                //datalogin.put("phone",binding.etMobile.getText().toString());
                //datalogin.put("password",binding.etMobilePassword.getText().toString());
                String url = ApiConfig.BASE_URl+ApiConfig.MOBILE_LOGIN+"?phone="+binding.etMobile.getText().toString()+"&password="+binding.etMobilePassword.getText().toString();
                //networkRequest.sendPostNetRequest(url,datalogin);
                networkRequest.sendGetNetRequest(url);
                break;
            case R.id.iv_back:
                toolsLogin.destoryFragment(getActivity(),this);
                break;
        }
    }
    private void DataAnalysisMobile(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
           // Log.i("哈哈哈哈","请求码"+code);
           // Log.i("哈哈哈哈","手机号"+binding.etMobile.getText().toString());
           // Log.i("哈哈哈哈","手机密码"+binding.etMobilePassword.getText().toString());
            if(code.equals("200")) {
                save(binding.etMobile.getText().toString(),binding.etMobilePassword.getText().toString());
                JSONObject account = jsonObject.getJSONObject("account");
                JSONObject profile = jsonObject.getJSONObject("profile");
                String id = account.getString("id");
                String avatarUrl = profile.getString("avatarUrl");
                String nickname = profile.getString("nickname");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Log.i("哈哈哈哈", "登录换活动");
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
    private void initView(){
        binding.btnMobileLogin.setOnClickListener(this::onClick);
        networkRequest = new NetworkRequest();
        toolsLogin = new ToolsLogin();
        binding.ivBack.setOnClickListener(this::onClick);
        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        wordOnclick();
    }
    @Override
    public void HandleMessage(String response) {
        //Log.i("哈哈哈哈","返回数据"+response);
        DataAnalysisMobile(response);
    }

    protected void wordOnclick(){                     //设置可点击文字（立即注册字体按钮）
        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append("忘记密码？立即找回");
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(@NonNull View widget) {
                Fragment fragment = new Module_fragment_login_change_password();
                toolsLogin.switchFragment(getActivity(),R.id.frm_login,fragment);

            }
        };
        style.setSpan(clickableSpan, 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        style.setSpan(foregroundColorSpan, 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        binding.tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvLogin.setText(style);
    }
    private void save(String phone,String password){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE).edit();
        editor.putString("phone",phone);
        editor.putString("password",password);
        editor.apply();
    }

}