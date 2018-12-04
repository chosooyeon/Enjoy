package com.example.sooyeon.graduationproject.tab;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sooyeon.graduationproject.Clipboard;
import com.example.sooyeon.graduationproject.R;
import com.example.sooyeon.graduationproject.SettingActivity;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.example.sooyeon.graduationproject.tab.MemoFragement.calledAlready;

public class TabMainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private PagerAdatper mPagerAdatper;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 액티비티의 최상단 status bar를 숨긴다. (setContentView보다 먼저 나온다.)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 액티비티의 타이틀을 숨긴다. (setContentView보다 먼저 나온다.)
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_tab_main);

        // 권한 획득 코드
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 0);

        mTabLayout = findViewById(R.id.tabLayout);
        mPager = findViewById(R.id.pager);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.star));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.search));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.account));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//탭의 가로 전체 사이즈 지정
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        //프래그먼트를 관리할 PagerAdapter를 생성한다.
        mPagerAdatper = new PagerAdatper(getSupportFragmentManager(),mTabLayout.getTabCount());
        mPager.setAdapter(mPagerAdatper);

        //Tab 레이아웃과 ViewPager를 이벤트로 서로 연결시켜준다.
        //ViewPager가 움직였을 때, 탭이 바뀌게끔 한다.
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        //탭레이아웃이 바뀌면 ViewPager의 Fragment도 바뀌는 작업을 연결한다.
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //현재 사용자가 바꾼 탭의 이벤트가 넘어온다.
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    protected void onStart(){
        super.onStart();
        startService(new Intent(this, Clipboard.class));

        mAuth.fetchSignInMethodsForEmail(currentUser.getEmail())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                // User can sign in with email/password
                                VerifyUserExistance();
                            } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                                // User can sign in with email/link
                                Intent TabMainIntent = new Intent(TabMainActivity.this, TabMainActivity.class);
                                TabMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(TabMainIntent);
                                finish();
                            }
                        } else {
                            Log.e("tag", "Error getting sign in methods for user", task.getException());
                        }
                    }
                });

//        if (!calledAlready)
//        {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
//            calledAlready = true;
//        }
    }


    //user가 존재하는지 확인한다
    private void VerifyUserExistance() {
        String currentUserID = mAuth.getCurrentUser().getUid();
        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name")){
                    //Toast.makeText(TabMainActivity.this,"환영합니다",Toast.LENGTH_SHORT).show();
                }else{
                    SendUserToSettingActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToSettingActivity() {
        Intent SettingsIntent = new Intent(TabMainActivity.this, SettingActivity.class);
        SettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SettingsIntent);
        finish();
    }
}
