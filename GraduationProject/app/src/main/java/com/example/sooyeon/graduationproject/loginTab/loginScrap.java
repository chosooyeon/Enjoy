package com.example.sooyeon.graduationproject.loginTab;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sooyeon.graduationproject.R;

public class loginScrap extends Fragment {

    public loginScrap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_login_scrap, container, false);

        //TODO 여기에 View를 찾고 이벤트를 등록하고 등등의 처리를 할 수 있다.

        return view;
    }
}
