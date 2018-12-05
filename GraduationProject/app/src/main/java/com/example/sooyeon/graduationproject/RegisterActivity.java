package com.example.sooyeon.graduationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.login.MainActivity;
import com.example.sooyeon.graduationproject.tab.TabMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccount;
    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private TextView AlreadyHave;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();


        InitializeFields();

        AlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmPassword.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)){
            if(password.equals(confirmPassword)){
                loadingBar.setTitle("계정을 등록중입니다");
                loadingBar.setMessage("잠시만 기다려 주세요...계정을 등록중입니다...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();

                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    //database에 추가
                                    String currentUserID = mAuth.getCurrentUser().getUid();
                                    RootRef.child("User").child(currentUserID).setValue("");

                                    SendUserToMainActivity();
                                    Toast.makeText(RegisterActivity.this,"계정이 성공적으로 등록되었습니다",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                                else{
                                    String message = task.getException().toString();
                                    Toast.makeText(RegisterActivity.this,"Error:"+message ,Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
            }else {
                Toast.makeText(this,"비밀번호가 일치하는지 확인해주세요",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"비어있는 내용이 없는지 확인해주세요",Toast.LENGTH_SHORT).show();
        }
    }


    private void InitializeFields() {
        CreateAccount = (Button)findViewById(R.id.register_button);
        UserEmail = (EditText)findViewById(R.id.register_email);
        UserPassword = (EditText)findViewById(R.id.register_password);
        UserConfirmPassword = (EditText)findViewById(R.id.register_confirm_password) ;
        AlreadyHave = (TextView)findViewById(R.id.register_already);
        loadingBar = new ProgressDialog(this);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, TabMainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
