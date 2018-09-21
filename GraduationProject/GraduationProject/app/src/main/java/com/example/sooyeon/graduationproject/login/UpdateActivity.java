package com.example.sooyeon.graduationproject.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sooyeon.graduationproject.R;

public class UpdateActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        final EditText edtId = findViewById(R.id.edtId);
        final EditText edtPw = findViewById(R.id.edtPw);
        final EditText edtPwCheck = findViewById(R.id.edtPwCheck);
        final ImageView imgCamera = findViewById(R.id.imgCamera);

        Button btnUpdate = findViewById(R.id.btnUpdate);

    }
}
