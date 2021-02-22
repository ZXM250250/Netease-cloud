package com.example.macticity.fragment;
import android.app.Fragment;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.acticity.R;
import com.example.acticity.databinding.FragmentModuleLoginCtodeBinding;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.tools.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Module_fragment_login_register extends Fragment implements View.OnClickListener,NetworkRequest.OnNetworkRequestlistener {

    private NetworkRequest networkRequest;
    private FragmentModuleLoginCtodeBinding binding;
    private Module_fragment_login_verify fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_module_login_ctode, container, false);
        View view = binding.getRoot();
        networkRequest = new NetworkRequest();
        networkRequest.setOnNetworkRequestlistener(this::DataAnalysisMobile);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mobile_register_code:
                    register();
                break;
        }
    }

    private void DataAnalysisMobile(String response) {

    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysisMobile(response);
    }
    private void register(){
        String url = ApiConfig.BASE_URl+ApiConfig.MAIN_REGISTER;
       HashMap<String, String> map = new HashMap<>();
       map.put("phone",Module_fragment_login_verify.phone);
       map.put("password",binding.etPassword.getText().toString());
       map.put("captcha",binding.etVerify.getText().toString());
       map.put("nickname",binding.etNick.getText().toString());
       networkRequest.sendPostNetRequest(url,map);

    }

}