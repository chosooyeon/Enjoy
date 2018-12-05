package com.example.sooyeon.graduationproject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.tab.TabMainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.google.firebase.storage.UploadTask.*;

public class PostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btnUpload;
    private ImageView imgPost;
    private EditText txtUrl;
    private EditText txtHashTag;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;
    private String currentUserID;

    private Uri mImageUri;
    private ProgressDialog loadingBar;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnUpload = (Button)findViewById(R.id.btnUpload);
        imgPost = (ImageView)findViewById(R.id.imgView);
        txtUrl = (EditText) findViewById(R.id.txtUrl);
        txtHashTag = (EditText) findViewById(R.id.txtHashTag);
        loadingBar = new ProgressDialog(this);

        //storage 저장경로
        storageReference = FirebaseStorage.getInstance().getReference("Posts Images");
        //database 저장경로!!
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){

        loadingBar.setTitle("등록중입니다");
        loadingBar.setMessage("잠시만 기다려 주세요...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
        UploadTask uploadTask = fileReference.putFile(mImageUri);
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
                    String setUrl = txtUrl.getText().toString();
                    String setHashTag = txtHashTag.getText().toString();

                    String realUrl = setUrl;
                    realUrl = realUrl.replace("/", "");
                    realUrl = realUrl.replace(".","");
                    realUrl = realUrl.replace(":","");

                    String realHash = setHashTag;
                    realHash = realHash.replace("#", "");

                    if (TextUtils.isEmpty(setUrl) && TextUtils.isEmpty(setHashTag) && isUrl(setUrl)) {
                        loadingBar.dismiss();
                        Toast.makeText(PostActivity.this,"url과 hashTag가 제대로 입력되었는지 확인해주세요!",Toast.LENGTH_LONG).show();
                    } else {
                        //파이어베이스 database에 크롭한 이미지를 저장한다.
                        String uploadId = databaseReference.push().getKey();

                        HashMap<String, Object> postMap = new HashMap<>();
                        postMap.put("url", realUrl);
                        postMap.put("hashTag", realHash);
                        postMap.put("img",downloadUrl);

                        databaseReference.child(currentUserID).child(uploadId)
                                .updateChildren(postMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadingBar.dismiss();
                                            //TabMain으로 보내준다.
                                            Intent mainIntent = new Intent(PostActivity.this, TabMainActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                        } else {
                                            String message = task.getException().toString();
                                            Toast.makeText(PostActivity.this, "database 저장 오류: " + message, Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                    }
                }else {
                    String message = task.getException().toString();
                    Toast.makeText(PostActivity.this, "storage 저장오류: " + message, Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imgPost);
        }
    }

    public boolean isUrl(String url) {
        String urlRegex = "^(file|gopher|news|nntp|telnet|https?|ftps?|sftp)://([a-z0-9-]+.)+[a-z0-9]{2,4}.*$";
        return url.matches(urlRegex);
    }
}
