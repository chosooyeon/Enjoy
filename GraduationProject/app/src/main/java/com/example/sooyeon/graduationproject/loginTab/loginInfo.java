package com.example.sooyeon.graduationproject.loginTab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.R;
import com.example.sooyeon.graduationproject.SettingActivity;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.example.sooyeon.graduationproject.tab.TabMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class loginInfo extends Fragment {

    private Button btnJoinEdit;
    private Button btnLogout;
    private Button btnQnA;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;

    public loginInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_login_info, container, false);

        //TODO 여기에 View를 찾고 이벤트를 등록하고 등등의 처리를 할 수 있다.
        btnJoinEdit = view.findViewById(R.id.btnJoinEdit);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnQnA = view.findViewById(R.id.btnQnA);
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snackbar.make(getView(), "로그아웃을 하시겠습니까?", Snackbar.LENGTH_LONG)
                        .setAction("로그아웃", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFirebaseAuth.signOut();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }
                        }).show();
            }
        });

        btnQnA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                try {
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"1whtndus@gmail.com"});

                    emailIntent.setType("text/html");
                    emailIntent.setPackage("com.google.android.gm");
                    if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null)
                        startActivity(emailIntent);

                    startActivity(emailIntent);
                } catch (Exception e) {
                    e.printStackTrace();

                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@gmail.com"});

                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
            }
        });

        btnJoinEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.fetchSignInMethodsForEmail(currentUser.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signInMethods = result.getSignInMethods();
                                    if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                        // User can sign in with email/password
                                        Intent i = new Intent(getActivity(),SettingActivity.class);
                                        startActivity(i);
                                    } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                                        // User can sign in with email/link
                                        Toast.makeText(getActivity(), "Gmail로 로그인하면 회원정보를 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("tag", "Error getting sign in methods for user", task.getException());
                                }
                            }
                        });
            }
        });

        return view;
    }
}
