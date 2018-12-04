package com.example.sooyeon.graduationproject.tab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.Memo;
import com.example.sooyeon.graduationproject.R;
//import com.example.sooyeon.graduationproject.firebase.FirebaseHelper;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemoFragement extends Fragment {

    //인증정보 가져오기
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private EditText editContent;
    private static FirebaseDatabase mFirebaseDatabase;

    private ListView lst_url;
    //List<Object> MemoList = new ArrayList<Object>();
    private List<Memo> MemoList;
    private ArrayAdapter<String> UrlAdapter;
    static boolean calledAlready = false;

    private String selectedMemoKey;

    static {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
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
        lst_url = view.findViewById(R.id.lstMemo);


        FloatingActionButton fabNewMemo = view.findViewById(R.id.btnPlus);


        // 저장을 여기에서
        fabNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        return view;
    }//onCreate end


    public boolean isUrl(String url) {
        String urlRegex = "^(file|gopher|news|nntp|telnet|https?|ftps?|sftp)://([a-z0-9-]+.)+[a-z0-9]{2,4}.*$";
        return url.matches(urlRegex);
    }

    private void initMemo(){
        selectedMemoKey = null;
    }

}
