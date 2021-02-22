package com.example.macticity.mactivity;
import androidx.databinding.DataBindingUtil;
import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.example.acticity.R;
import com.example.acticity.databinding.ActivityMainBinding;
import com.example.macticity.fragment.Module_fragment_login_main;
import com.example.macticity.tools.ToolsLogin;

import java.io.IOException;


public class LoginActivity extends Activity   {
private ActivityMainBinding binding;
private ToolsLogin toolsLogin; //沉浸式工具类
private String TAG = "调试";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        toolsLogin = new ToolsLogin();
        toolsLogin.FullScreen(this);
        Fragment fragment = new Module_fragment_login_main();
        toolsLogin.switchFragment(this,R.id.frm_login,fragment);

    }


}