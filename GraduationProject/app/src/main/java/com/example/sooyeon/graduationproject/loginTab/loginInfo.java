package com.example.sooyeon.graduationproject.loginTab;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sooyeon.graduationproject.R;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class loginInfo extends Fragment {

    private Button btnJoinEdit;
    private Button btnLogout;
    private Button btnQnA;

    private FirebaseAuth mFirebaseAuth;

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

        return view;
    }
}
