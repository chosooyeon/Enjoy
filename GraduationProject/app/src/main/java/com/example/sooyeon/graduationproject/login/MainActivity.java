package com.example.sooyeon.graduationproject.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.R;
import com.example.sooyeon.graduationproject.tab.TabMainActivity;

public class MainActivity extends AppCompatActivity {

    private EditText joinId;
    private EditText joinPwd;
    private Button btnjoin;
    private Button mbtnLogin;
    private EditText txtId;
    private EditText txtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinId = (EditText) findViewById(R.id.joinId);
        joinPwd = (EditText) findViewById(R.id.joinPwd);
        mbtnLogin = findViewById(R.id.btnLogin);
        Button btnJoin = findViewById(R.id.btnJoin);
        txtId = findViewById(R.id.txtId);
        txtPwd = findViewById(R.id.txtPwd);


        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(i);
            }
        });

        mbtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, TabMainActivity.class);
                startActivity(k);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}