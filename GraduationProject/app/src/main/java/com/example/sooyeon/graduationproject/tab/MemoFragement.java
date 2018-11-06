package com.example.sooyeon.graduationproject.tab;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.CustomDialog;
import com.example.sooyeon.graduationproject.R;
//import com.example.sooyeon.graduationproject.firebase.FirebaseHelper;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemoFragement extends Fragment {

    //인증정보 가져오기
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private EditText editContent;
    private TextView txtEmail;
    private TextView txtName;
    private String selectedMemoKey;
    private static FirebaseDatabase mFirebaseDatabase;

    static {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
    }

    //private TextView main_label;
    public MemoFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_memo, container, false);

        //TODO 여기에 View를 찾고 이벤트를 등록하고 등등의 처리를 할 수 있다.
        //getView().findViewById(R.id.btn1).setOnClickListener();

        //인스턴스를 얻어온다
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        editContent = view.findViewById(R.id.content);

        if(mFirebaseUser ==null){
            Intent i = new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }
        FloatingActionButton fabNewMemo = view.findViewById(R.id.btnPlus);


        // 저장을 여기에서
        fabNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //추가해야한다......!!!
                CustomDialog customDialog = new CustomDialog(getActivity());
                customDialog.callFunction(editContent);
            }
        });

        return view;
    }


}
