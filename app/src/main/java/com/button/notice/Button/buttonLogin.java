package com.button.notice.Button;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.button.notice.Fragment.MainActivity;

import com.button.notice.JPush.JpushUtil;
import com.button.notice.JPush.MyApplication;
import com.button.notice.R;
import com.button.notice.service.CommonRequest;
import com.button.notice.service.CommonResponse;
import com.button.notice.service.ResponseHandler;
import com.button.notice.util.MD5;
import com.button.notice.util.StringUtil;

import java.security.NoSuchAlgorithmException;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class buttonLogin extends AppCompatActivity  {

    private EditText etAccount;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_login);

//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(getApplicationContext());   //初始化推送服务

        final SharedPreferences sp=this.getSharedPreferences("DODODO",Context.MODE_PRIVATE);

        etAccount = (EditText) findViewById(R.id.UserId);
        etPassword = (EditText) findViewById(R.id.UserKey);

        final Button btnLogin = (Button) findViewById(R.id.login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonRequest request = new CommonRequest();

                if (checkNetwork()==false)
                {
                    Toast.makeText(buttonLogin.this, "无法连接到网络", Toast.LENGTH_SHORT).show();
                }
                else if (checkNetwork()==true){


                        if (!StringUtil.isEmpty(etAccount.getText().toString())
                                && !StringUtil.isEmpty(etPassword.getText().toString())) {


                            request.setUserName(etAccount.getText().toString());

                            try {
                                String passmd= MD5.md5(etPassword.getText().toString());
                                request.setPassWord(passmd);

                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }

                            request.Login(new ResponseHandler() {
                                @Override
                                public void success(CommonResponse response) {

                                    SharedPreferences.Editor editor=sp.edit();
                                    editor.putString("Id",  response.getResMsg());
                                    editor.commit();

                                    Toast.makeText(buttonLogin.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(buttonLogin.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void fail(String failCode, String failMsg) {
                                    Toast.makeText(buttonLogin.this, "学号或密码错误", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {




                            Toast.makeText(buttonLogin.this, "账号、密码都不能为空！", Toast.LENGTH_SHORT).show();

                        }

                    }


                }
        });



        Button bttest = (Button) findViewById(R.id.test); //测试用跳转

        bttest.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(buttonLogin.this,MainActivity.class );
                        startActivity(intent);
                    }
                })
        );

    }

    boolean checkNetwork() {
        // 实例化ConnectivityManager
        ConnectivityManager manager = (ConnectivityManager) buttonLogin.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获得当前网络信息
        NetworkInfo info = manager.getActiveNetworkInfo();
        // 判断是否连接
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }   //检查网络


}