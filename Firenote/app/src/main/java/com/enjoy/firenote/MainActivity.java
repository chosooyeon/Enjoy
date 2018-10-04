package com.enjoy.firenote;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //인증정보 가져오기
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private EditText editContent;
    private TextView txtEmail;
    private TextView txtName;
    private String selectedMemoKey;

    private static FirebaseDatabase mFirebaseDatabase;
    private NavigationView mNavigationView;

    static {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //인스턴스를 얻어온다
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        editContent = (EditText)findViewById(R.id.content);

        if(mFirebaseUser ==null){
            startActivity(new Intent(MainActivity.this,AuthActivity.class));
            finish();
            return;
        }

        setSupportActionBar(toolbar);

        FloatingActionButton fabNewMemo = (FloatingActionButton) findViewById(R.id.new_memo);
        FloatingActionButton fabSaveMemo = (FloatingActionButton) findViewById(R.id.save_memo);

        fabNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMemo();
            }
        });

        fabSaveMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedMemoKey==null){
                    saveMemo();
                }else{
                    updateMemo();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        mNavigationView.setNavigationItemSelectedListener(this);
        txtEmail = (TextView) headerView.findViewById(R.id.txtEmail);
        txtName = (TextView) headerView.findViewById(R.id.txtName);
        profileUpdate();
        displayMemos();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteMemo();
        }else if(id == R.id.action_logout){
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Memo selectedMemo = (Memo)item.getActionView().getTag();
        //클릭된 메모를 content에다가 출력한다.
        editContent.setText(selectedMemo.getTxt());
        selectedMemoKey = selectedMemo.getKey();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initMemo(){
        selectedMemoKey = null;
        editContent.setText("");
    }

    private void logout(){
        Snackbar.make(editContent,"로그아웃을 하시겠습니까?",Snackbar.LENGTH_LONG)
                .setAction("로그아웃", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFirebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this,AuthActivity.class));
                        finish();
                    }
                }).show();
    }

    private void saveMemo() {
        String text = editContent.getText().toString();
        if(text.isEmpty()){
            return;
        }
        Memo memo = new Memo();
        memo.setTxt(editContent.getText().toString());
        memo.setCreateDate(new Date().getTime());
        mFirebaseDatabase
                .getReference("memos/"+mFirebaseUser.getUid())
                .push()
                .setValue(memo)
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(editContent,"메모가 저장되었습니다.",Snackbar.LENGTH_LONG).show();
                        initMemo();
                    }
                });
    }

    private void deleteMemo(){
        //메뉴 상단에 메뉴를 삭제하는 부분을 만든다.
        if(selectedMemoKey ==null)
            return;
        Snackbar.make(editContent,"메모를 삭제하시겠습니까?",Snackbar.LENGTH_LONG)
                .setAction("삭제", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFirebaseDatabase
                                .getReference("memos/"+mFirebaseUser.getUid()+"/"+selectedMemoKey)
                                .removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        //정상적으로 삭제가 되었을 때 firebase에서 콜백해준다.
                                        Snackbar.make(editContent,"삭제가 완료되었습니다.",Snackbar.LENGTH_LONG).show();
                                    }
                                });
                    }
                }).show();
    }

    private void updateMemo(){
        String text = editContent.getText().toString();
        if(text.isEmpty()){
            return;
        }
        Memo memo = new Memo();
        memo.setTxt(editContent.getText().toString());
        memo.setCreateDate(new Date().getTime());
        mFirebaseDatabase
                .getReference("memos/"+mFirebaseUser.getUid()+"/"+selectedMemoKey)
                .setValue(memo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(editContent, "메모가 수정되었습니다.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void profileUpdate(){
        txtEmail.setText(mFirebaseUser.getEmail());
        txtName.setText(mFirebaseUser.getDisplayName());
    }
    private void displayMemos(){
        mFirebaseDatabase.getReference("memos/"+mFirebaseUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Memo memo = dataSnapshot.getValue(Memo.class);
                        memo.setKey(dataSnapshot.getKey());
                        displayMemoList(memo);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Memo memo = dataSnapshot.getValue(Memo.class);
                        memo.setKey(dataSnapshot.getKey());
                        //메모를 수정한다.
                        for (int i=0;i<mNavigationView.getMenu().size();i++){
                            MenuItem menuItem = mNavigationView.getMenu().getItem(i);
                            if(memo.getKey().equals(((Memo)menuItem.getActionView().getTag()).getKey())){
                                menuItem.getActionView().setTag(memo);
                                menuItem.setTitle(memo.getTitle());
                                break;
                            }
                        }

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        initMemo();
                        Memo memo = dataSnapshot.getValue(Memo.class);
                        memo.setKey(dataSnapshot.getKey());
                        for (int i = 0; i < mNavigationView.getMenu().size(); i++) {
                            MenuItem menuItem = mNavigationView.getMenu().getItem(i);
                            if (memo.getKey().equals(((Memo) menuItem.getActionView().getTag()).getKey())) {
                                menuItem.setVisible(false);
                            }
                        }
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void displayMemoList(Memo memo){
        Menu leftMenu = mNavigationView.getMenu();
        MenuItem item = leftMenu.add(memo.getTitle());
        View view = new View(getApplication());
        view.setTag(memo);
        item.setActionView(view);
    }
}
