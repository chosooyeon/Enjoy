package com.example.a8306_03.graduationapp;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class findFragment extends Fragment {

    private EditText mEdtSearch;
    private ImageButton mBtnSearch;
    private View view;

    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_group = new ArrayList<>();
    private DatabaseReference GroupRef;

    public findFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_find, container, false);

        mEdtSearch = view.findViewById(R.id.edtSearch);
        mBtnSearch = view.findViewById(R.id.btnSearch);
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        IntializeFields();
        RetrieveAndDisplayGroups();

        mEdtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // Enter 키 눌렀을 때 처리
                    searchList();
                    return true;
                    // 리턴이 true일 경우에는 사용자가 입력한 엔터키의 이벤트를 os 단까지 보내지 않겠다는 뜻이다.
                    // 즉 사용자가 입력한 엔터키가 소면된다.
                }
                return false; // false일 경우, 사용자가 입력한 키값을 os에 다시 전달한다.
            }
        });

        // 검색버튼
        view.findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList();
            }
        });

        return view;
    }

    private void IntializeFields() {
        list_view = (ListView) view.findViewById(R.id.lstFindMember);
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_group);
        list_view.setAdapter(arrayAdapter);
    }

    // 리스트에서 항목을 검색하는 메서드
    public void searchList(){
        // 검색어
        final String search = mEdtSearch.getText().toString();

        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Set<String> sets = new HashSet<>();  //찾는 list
                sets.addAll(set);   //sets에 set을 복사

                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add((String) ((DataSnapshot)iterator.next()).getValue());
                }
                list_group.clear();
                list_group.addAll(set);

                //검색기능이 안된다...
                for(int i=0; i<sets.size(); i++){
                    if (sets.contains(search))
                    {
                        // 검색된 데이터를 리스트에 추가한다.
                        list_group.clear();
                        list_group.addAll(sets);
                    }
                }

                if (search != null && search.length()>0 && set.size()>0){
                    list_group.clear();
                    list_group.addAll(sets);
                    arrayAdapter.notifyDataSetChanged();
                }
                else if(search != null && search.length()>0 && set.size() ==0){
                    list_group.clear();
                    list_group.addAll(sets);
                    arrayAdapter.notifyDataSetChanged();
                }
                else if(search == null || search.length() ==0){
                    list_group.clear();
                    list_group.addAll(set);
                    arrayAdapter.notifyDataSetChanged();
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEdtSearch.requestFocus(); // 포커스를 입력창에 다시 준다.
            }
        }, 700);

    }

    //group에 있는 데이터를 list로 가지고 오고 있는 중이다...
    private void RetrieveAndDisplayGroups() {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add((String) ((DataSnapshot)iterator.next()).getValue());
                }
                list_group.clear();
                list_group.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
