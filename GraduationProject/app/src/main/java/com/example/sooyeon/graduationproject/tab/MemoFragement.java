package com.example.sooyeon.graduationproject.tab;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sooyeon.graduationproject.CustomDialog;
import com.example.sooyeon.graduationproject.Memo;
import com.example.sooyeon.graduationproject.R;
//import com.example.sooyeon.graduationproject.firebase.FirebaseHelper;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    List<Object> MemoList = new ArrayList<Object>();
    private ArrayAdapter<String> UrlAdapter;
    static boolean calledAlready = false;

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

        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }

        if(mFirebaseUser ==null){
            Intent i = new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }
        FloatingActionButton fabNewMemo = view.findViewById(R.id.btnPlus);


        // 저장을 여기에서
        fabNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(getActivity());
                customDialog.callFunction(editContent);

            }
        });

        return view;
    }//onCreate end

    public void onResume() {

        super.onResume();

        // Read from the database
        mFirebaseDatabase
                .getReference(mFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //MemoList.clear();
                        // 클래스 모델이 필요?
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Memo memo = new Memo();
                            memo.setTitle(dataSnapshot.getValue(Memo.class).getTitle());

                            MemoList.add(memo.title);
                            UrlAdapter.add(memo.title);
                        }
                        UrlAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", "Failed to read value", databaseError.toException());
                    }
                });

        UrlAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view, new ArrayList<String>());
        lst_url.setAdapter(UrlAdapter);

    }
}
