package com.surewang.signstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.surewang.signstudent.Net.HttpUtils;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LoginSuccess() ;break;
                case 2:
                    LoginFail(); break;
            }

        }
        ;

        private void LoginFail() {
            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }

        private void LoginSuccess() {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            //保存登录的状态和用户名
            final EditText Account = findViewById(R.id.userAccount);
            Log.d("得到 account ", Account.getText().toString());
            saveLoginStatus(Account.getText().toString());
            //登录成功的状态保存到MainActivity
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            intent.putExtra("account",Account.getText().toString() );
            startActivity(intent);
            LoginActivity.this.finish();
        }
    };

    private void saveLoginStatus(String account) {
        SharedPreferences preference = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putString("account",account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btn_login);
        final EditText Account =  findViewById(R.id.userAccount);
        final EditText Password = findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String account = Account.getText().toString();
               final  String password = Password.getText().toString();
               final String url = "http://10.0.2.2:8081/user/getUser";

                if(TextUtils.isEmpty(account)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    HashMap params = new HashMap();
                    params.put("account",account);
                    params.put("password",password);
                    HttpUtils.post(url,params,handler);
                }
            }
        });


    }
}