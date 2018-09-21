package com.example.sooyeon.graduationproject.login;

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

public class JoinEditActivity extends AppCompatActivity {

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

        setContentView(R.layout.activity_join_edit);

        mImgCamera = findViewById(R.id.imgCamera);
        mTxtLoginId = findViewById(R.id.txtLoginId);
        mBtnLogout = findViewById(R.id.btnLogout);
        mBtnUpdate = findViewById(R.id.btnUpdate);

        final EditText edtId = findViewById(R.id.edtId);
        final EditText edtPw = findViewById(R.id.edtPw);
        final EditText edtPwCheck = findViewById(R.id.edtPwCheck);

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!edtPw.getText().toString().equals(edtPwCheck.getText().toString())) {
                    Toast.makeText(JoinEditActivity.this, "비밀번호가 일치하는지 다시 한 번 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else if (edtPw.getText().toString().equals(edtPwCheck.getText().toString())) {
                    Toast.makeText(JoinEditActivity.this, "회원 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();

                    finish();
                }

            }
        });

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JoinEditActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
    }

}//end onCreate()


