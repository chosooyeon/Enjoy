package com.example.sooyeon.graduationproject.loginTab;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.Info;
import com.example.sooyeon.graduationproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class loginScrap extends Fragment {

    private Toolbar mToolbar;
    private RecyclerView FindFriendsList;
    private DatabaseReference UserRef;


    public loginScrap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_login_scrap, container, false);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        FindFriendsList = view.findViewById(R.id.find_friends_list);
        FindFriendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //TODO 여기에 View를 찾고 이벤트를 등록하고 등등의 처리를 할 수 있다.

        return view;
    }

    @Override
    public void onStart() {
        FirebaseRecyclerOptions<Info> options =
                new FirebaseRecyclerOptions.Builder<Info>()
                        .setQuery(UserRef, Info.class)
                        .build();

        FirebaseRecyclerAdapter<Info, FindFriendsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Info, FindFriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull Info model) {
                        holder.userName.setText(model.getName());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_launcher_foreground).into(holder.profileImage, new Callback() {
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
                    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display, parent,false);
                        FindFriendsViewHolder viewHolder = new FindFriendsViewHolder(view);
                        return viewHolder;
                    }
                };

        FindFriendsList.setAdapter(adapter);
        if(adapter != null) {
            adapter.startListening();
        }
        super.onStart();
    }
    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        CircleImageView profileImage;

        public FindFriendsViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            profileImage = itemView.findViewById(R.id.user_image);
        }
    }
}
