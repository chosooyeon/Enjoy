package com.example.sooyeon.graduationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.tab.TabMainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private Button UpdateAccountSettings;
    private EditText userName;
    private ImageView imgGellery;
    public static Uri resultUri;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private static final int GalleryPick = 1;
    private StorageReference UserProfileImage;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImage = FirebaseStorage.getInstance().getReference().child("Profile Images");

        InitializeFields();
        RetrieveUserInfo(); //프로필이 있다면 setting 해준다

        imgGellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });

        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setUserName = userName.getText().toString();
                if (TextUtils.isEmpty(setUserName)) {
                    Toast.makeText(SettingActivity.this, "사용자 이름을 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> profileMap = new HashMap<>();
                    profileMap.put("uid", currentUserID);
                    profileMap.put("name", setUserName);

                    RootRef.child("Users").child(currentUserID).updateChildren(profileMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //TabMain으로 보내준다.
                                        Intent mainIntent = new Intent(SettingActivity.this, TabMainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();

                                        Toast.makeText(SettingActivity.this, "프로필이 업데이트 되었습니다", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String message = task.getException().toString();
                                        Toast.makeText(SettingActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void InitializeFields() {
        UpdateAccountSettings = (Button)findViewById(R.id.update_settings_button);
        userName = (EditText)findViewById(R.id.set_user_name);
        imgGellery = (ImageView) findViewById(R.id.imgGellery);
        loadingBar = new ProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null){
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(SettingActivity.this);
        }

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                loadingBar.setTitle("이미지를 등록중입니다");
                loadingBar.setMessage("잠시만 기다려 주세요...이미지를 등록중입니다...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                resultUri = result.getUri();

                final StorageReference fileReference = UserProfileImage.child(currentUserID + ".jpg");
                UploadTask uploadTask = fileReference.putFile(resultUri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) { //이미지가 storage에 성공적으로 저장됐을 경우
                            final String downloadUrl = task.getResult().toString();

                            //파이어베이스 database에 크롭한 이미지를 저장한다.
                            RootRef.child("Users").child(currentUserID).child("image")
                                    .setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loadingBar.dismiss();
                                            }
                                            else{
                                                String message = task.getException().toString();
                                                Toast.makeText(SettingActivity.this, "database 저장 오류: " + message, Toast.LENGTH_LONG).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(SettingActivity.this, "storage 저장오류: " + message, Toast.LENGTH_LONG).show();
                            //loadingBar.dismiss();
                        }
                    }
                });
            }else {
                Toast.makeText(this, "선택된 사진이 없습니다. 사진을 선택해주세요.",Toast.LENGTH_LONG).show();
            }
                RetrieveUserInfo();
            }
        }

    //다시 가져오다
    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()&&(dataSnapshot.hasChild("name")&&dataSnapshot.hasChild("image"))){
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

                            userName.setText(retrieveUserName);
                            Picasso.get().load(retrieveProfileImage).resize(500,500).into(imgGellery);

                        }else if(dataSnapshot.exists() && dataSnapshot.hasChild("name")){
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();

                            userName.setText(retrieveUserName);
                        }else{
                            //userName이 보이지 않게 한다
                            //userName.setVisibility(View.INVISIBLE);
                            Toast.makeText(SettingActivity.this,"프로필을 설정해주세요",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
