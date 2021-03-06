package com.example.sooyeon.graduationproject.tab;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.Memo;
import com.example.sooyeon.graduationproject.PostActivity;
import com.example.sooyeon.graduationproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MemoFragement extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View ListView;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference RootRef;

    private FirebaseAuth mFirebaseAuth;
    private String currentUserID;
    public String uploadId;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public OnItemClickListener onItemClickListener;

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
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        RootRef = mFirebaseDatabase.getReference().child("Posts");
        uploadId = RootRef.push().getKey();

        FloatingActionButton fabNewMemo = ListView.findViewById(R.id.btnPlus);
        mSwipeRefreshLayout = ListView.findViewById(R.id.swipe_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);

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

    @Override
    public void onStart() {
        FirebaseRecyclerOptions<Memo> options =
                new FirebaseRecyclerOptions.Builder<Memo>()
                        .setQuery(RootRef.child(currentUserID).orderByChild(uploadId), Memo.class)
                        .build();

        FirebaseRecyclerAdapter<Memo, ListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Memo, ListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull Memo model) {
                        holder.txtUrl.setText(model.getUrl());
                        holder.txtHashTag.setText(model.getHashTag());
                        Picasso.get().load(model.getImg()).placeholder(R.drawable.ic_launcher_foreground).into(holder.imgView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent,false);
                        ListViewHolder viewHolder = new ListViewHolder(view);
                        return viewHolder;
                    }
                };

        mRecyclerView.setAdapter(adapter);
        if(adapter != null) {
            adapter.startListening();
        }

        super.onStart();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },500);

    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{

        TextView txtUrl;
        TextView txtHashTag;
        ImageView imgView;

        public ListViewHolder(View itemView) {
            super(itemView);

            txtUrl = itemView.findViewById(R.id.txtUrl);
            txtHashTag = itemView.findViewById(R.id.txtHashTag);
            imgView = itemView.findViewById(R.id.imgView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(onItemClickListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    onItemClickListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem doWhatEver = contextMenu.add(Menu.NONE,1,1,"WhatEver");
            MenuItem delete = contextMenu.add(Menu.NONE,2,2,"Delete");

            doWhatEver.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(onItemClickListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            onItemClickListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            onItemClickListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

}
