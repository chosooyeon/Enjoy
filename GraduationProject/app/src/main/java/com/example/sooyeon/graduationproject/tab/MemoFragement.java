package com.example.sooyeon.graduationproject.tab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.Memo;
import com.example.sooyeon.graduationproject.PostActivity;
import com.example.sooyeon.graduationproject.R;
//import com.example.sooyeon.graduationproject.firebase.FirebaseHelper;
import com.example.sooyeon.graduationproject.RegisterActivity;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemoFragement extends Fragment {

    private View ListView;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference RootRef;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;
    private String currentUserID;

    public MemoFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ListView = inflater.inflate(R.layout.fragement_memo, container, false);

        mRecyclerView = ListView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //경로상에 뭔가 문제가...
        RootRef = mFirebaseDatabase.getReference().child("Posts").child(currentUserID);

        FloatingActionButton fabNewMemo = ListView.findViewById(R.id.btnPlus);

        fabNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //postActivity로 간다.
                Intent i = new Intent(getActivity(), PostActivity.class);
                startActivity(i);
            }
        });
        return ListView;
    }//onCreate end

//    @Override
//    public void onStart() {
//        FirebaseRecyclerOptions<Memo> options =
//                new FirebaseRecyclerOptions.Builder<Memo>()
//                        .setQuery(RootRef, Memo.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Memo, ListViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Memo, ListViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull Memo model) {
//                        holder.txtUrl.setText(model.getUrl());
//                        holder.txtHashTag.setText(model.getHashTag());
//                        Picasso.get().load(model.getImg()).placeholder(R.drawable.ic_launcher_foreground).into(holder.imgView, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError(Exception e) {
//                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display, parent,false);
//                        ListViewHolder viewHolder = new ListViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//
//        mRecyclerView.setAdapter(adapter);
//        if(adapter != null) {
//            adapter.startListening();
//        }
//        super.onStart();
//    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        TextView txtUrl;
        TextView txtHashTag;
        ImageView imgView;

        public ListViewHolder(View itemView) {
            super(itemView);

            txtUrl = itemView.findViewById(R.id.txtUrl);
            txtHashTag = itemView.findViewById(R.id.txtHashTag);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }



}
