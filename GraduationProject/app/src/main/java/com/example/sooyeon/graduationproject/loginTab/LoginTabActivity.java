package com.example.sooyeon.graduationproject.loginTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.R;
import com.example.sooyeon.graduationproject.login.MainActivity;

public class LoginTabActivity extends AppCompatActivity {

    private TextView mTxtLoginId;
    private Button mBtnUpdate, mBtnLogout;
    private ImageView mImgCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //액티비티의 최상단 status bar 를 숨긴다.(setContentView보다 먼저 나온다.)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액티비티의 타이틀을 숨긴다. (setContentView보다 먼저 나온다.)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login_tab);

        mImgCamera = findViewById(R.id.imgCamera);
        mTxtLoginId = findViewById(R.id.txtLoginId);
        mBtnLogout = findViewById(R.id.btnLogout);
        mBtnUpdate = findViewById(R.id.btnUpdate);

        final EditText edtId = findViewById(R.id.edtId);
        final EditText edtPw = findViewById(R.id.edtPw);
        final EditText edtPwCheck = findViewById(R.id.edtPwCheck);


    }

}//end onCreate()


