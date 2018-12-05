package com.example.sooyeon.graduationproject.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.RegisterActivity;
import com.example.sooyeon.graduationproject.SplashActivity;
import com.example.sooyeon.graduationproject.tab.TabMainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.example.sooyeon.graduationproject.R;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener{

    private Button btnjoin, mbtnLogin;  //회원가입, 로그인
    private EditText txtId, txtPwd;
    private ProgressDialog loadingBar;

    private SignInButton mSigninBtn;//google 로그인
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

        //mSigninBtn = (SignInButton)findViewById(R.id.sign_in_btn);
        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //구글 api 클라이언트
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

//        mSigninBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(intent,100);
//            }
//        });

        mbtnLogin = findViewById(R.id.btnLogin);
        Button btnJoin = findViewById(R.id.btnJoin);
        txtId = findViewById(R.id.txtId);
        txtPwd = findViewById(R.id.txtPwd);
        loadingBar = new ProgressDialog(this);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 창으로 이동
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        mbtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String email = txtId.getText().toString();
                String password = txtPwd.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.setTitle("로그인중입니다");
                    loadingBar.setMessage("잠시만 기다려 주세요...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    mFirebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(MainActivity.this, TabMainActivity.class);
                                        startActivity(i);
                                        Toast.makeText(MainActivity.this,email+"님 환영합니다",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else{

                                        String message = task.getException().toString();
                                        Toast.makeText(MainActivity.this,"Error:"+message ,Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }


        });
    }//end onCreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            //Toast.makeText(AuthActivity.this, "onActivityResult", Toast.LENGTH_LONG).show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();

            if(result.isSuccess()){
                firebaseWithGoogle(account);
            }else{
                Toast.makeText(this,"인증에 실패하였습니다.",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"인증에 실패하였습니다.",Toast.LENGTH_LONG).show();
    }

    private void firebaseWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        Task<AuthResult> authResultTask = mFirebaseAuth.signInWithCredential(credential);

        authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(MainActivity.this, TabMainActivity.class));
                FirebaseUser firebaseUser = authResult.getUser();
                Toast.makeText(MainActivity.this, firebaseUser.getEmail()+"님 환영합니다", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}