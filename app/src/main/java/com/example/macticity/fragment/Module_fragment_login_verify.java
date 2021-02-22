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
import com.example.acticity.databinding.FragmentModuleLoginMobileBinding;
import com.example.macticity.api.ApiConfig;
import com.example.macticity.tools.NetworkRequest;
import com.example.macticity.tools.ToolsLogin;
import org.json.JSONObject;


public class Module_fragment_login_verify extends Fragment implements View.OnClickListener,NetworkRequest.OnNetworkRequestlistener{
        private FragmentModuleLoginMobileBinding binding;
        private ToolsLogin toolsLogin;
        private NetworkRequest networkRequest;
        //private String TAG ="调试四";
        public  static String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_module_login_mobile,container,false);
        View view = binding.getRoot();
        toolsLogin = new ToolsLogin();
        binding.ivBack.setOnClickListener(this::onClick);
        binding.btnMobileNext.setOnClickListener(this::onClick);
        networkRequest = new NetworkRequest();
        networkRequest.setOnNetworkRequestlistener(this::HandleMessage);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                toolsLogin.destoryFragment(getActivity(),this);
                break;
            case R.id.et_mobile:

                break;
            case R.id.btn_mobile_next:
                    checkMobile(binding.etMobile.getText().toString());
                break;
        }
    }
    public void checkMobile(String phone){
      //  Log.i("哈哈哈","手机号长度"+phone.length());
        if (phone.length()==11){
            Log.i("哈哈哈","checkMobile手机号长度"+phone.length());
            //phone_map.put("phone",binding.etMobile.getText().toString());
            String url = ApiConfig.BASE_URl+ApiConfig.MOBILE_TEST+"?phone="+phone;
            ///networkRequest.sendPostNetRequest(url,phone_map);
           // Log.i(TAG,"网络请求所用手机号"+phone);
            networkRequest.sendGetNetRequest(url);
        }else {
            Toast.makeText(getActivity(),"手机号输入错误！",Toast.LENGTH_SHORT).show();
        }
    }
    private void DataAnalysisMobile(String data){
        try {
           // Log.i(TAG,"解析数据");
           // Log.i(TAG,"是空吗？"+data);
            JSONObject jsonObject1 = new JSONObject(data);
           // Log.i(TAG,"解析数据");
            String hasPassword = jsonObject1.getString("hasPassword");
          //  Log.i(TAG,"hasPassword"+hasPassword);
            if (hasPassword.equals("true")){  //表示已经注册   直接登录
//                Log.i(TAG,"进来了");
////                String url = ApiConfig.BASE_URl+ApiConfig.MOBILE_CTCODE;
////                networkRequest.sendPostNetRequest(url,phone_map);  //给手机发送验证码
////                Fragment fragment = new Module_fragment_login();
////                phone = binding.etMobile.getText().toString();
////                toolsLogin.switchFragment(getActivity(),R.id.frm_login,fragment);   // 跳转验证码输入的界面

                Fragment fragment = new Module_fragment_login();
                toolsLogin.switchFragment(getActivity(),R.id.frm_login,fragment);
                Toast.makeText(getActivity(),"此手机号已经注册,已为你切换到登录界面",Toast.LENGTH_SHORT).show();

            }else{      //注册登录
                phone = binding.etMobile.getText().toString();
                String url = ApiConfig.BASE_URl+ApiConfig.MOBILE_CTCODE+"?phone="+phone;
                networkRequest.sendGetNetRequest(url);
                  Toast.makeText(getActivity(),"手机号未注册，已为你跳转注册界面",Toast.LENGTH_SHORT).show();
                  Fragment fragment = new Module_fragment_login_register();
                  toolsLogin.switchFragment(getActivity(),R.id.frm_login,fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void HandleMessage(String response) {
        DataAnalysisMobile(response);
    }
}