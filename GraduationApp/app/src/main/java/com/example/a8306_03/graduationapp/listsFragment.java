package com.example.a8306_03.graduationapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class listsFragment extends Fragment {

    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_group = new ArrayList<>();

    private DatabaseReference GroupRef;
    private String currentGroupName;

    public listsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_lists, container, false);

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        IntializeFields();

        RetrieveAndDisplayGroups();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position값을 받아와서 현재 값을 currentGroupName으로 넘겨준다
                String currentGroupName = parent.getItemAtPosition(position).toString();
                Intent listsIntent = new Intent(getContext(), BrowseActivity.class);
                listsIntent.putExtra("groupName",currentGroupName);
                startActivity(listsIntent);
            }
        });

        //list를 길게 클릭 했을 때
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                String[] list = {"삭제","공유"};
                builder1.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0: //삭제
                                //position값을 받아온다
                                currentGroupName = parent.getItemAtPosition(position).toString();
                                //롱클릭 시 삭제하시겠습니까? 후 firebase에서 삭제
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);
                                builder.setTitle("삭제하시겠습니까?");

                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //위치값을 받아와서 firebase에서 삭제
                                        DeleteGroup(currentGroupName);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                                break;
                            case 1: //공유
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                //intent.putExtra(Intent.EXTRA_SUBJECT, "주제");
                                String url = parent.getItemAtPosition(position).toString();
                                intent.putExtra(Intent.EXTRA_TEXT, url);

                                Intent chooser = Intent.createChooser(intent, "공유");
                                startActivity(chooser);

                                break;
                        }
                    }
                });

                AlertDialog dialog = builder1.create();
                dialog.show();

            return true;
            }
        });

        return groupFragmentView;
    }//end onCreate

    private void DeleteGroup(final String currentGroupName) {
        //이 groupName이 url이 되어야 한다.
        String realgroupName = currentGroupName;
        realgroupName = realgroupName.replace("/", "");
        realgroupName = realgroupName.replace(".","");
        realgroupName = realgroupName.replace(":","");
        GroupRef.child(realgroupName).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),currentGroupName+"이 삭제되었습니다",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void IntializeFields() {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_group);
        list_view.setAdapter(arrayAdapter);
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
