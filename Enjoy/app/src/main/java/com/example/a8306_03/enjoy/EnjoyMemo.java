package com.example.a8306_03.enjoy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 8306-03 on 2018-05-18.
 */
public class EnjoyMemo extends MainActivity{

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enjoy_memo);

        requirepermission();

        Button button = (Button) findViewById(R.id.btnCamera);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                boolean camera =  ContextCompat.checkSelfPermission
                        (view.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean write = ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                if(camera && write){
                    //사진찍은 인텐트 코드 넣기

                    takePicture();
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent,0);
                }else{
                    Toast.makeText(EnjoyMemo.this, "카메라 권한 및 쓰기 권한을 주지 않았습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = createImageFile();//이미지 저장 장소
            //사진이 저장되는 경로
            Uri photoUri = FileProvider.getUriForFile(this,"com.example.sooyeon.camera.fileprovider",photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(intent,10);
        }catch (IOException e){
            e.printStackTrace();
        }


        startActivityForResult(intent,10);
    }
    void requirepermission(){
        String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> listPermissionNeeded = new ArrayList<>();

        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_DENIED){
                //권한이 허가가 안됬을 경우 요청할 권한을 모집하는 부분
                listPermissionNeeded.add(permission);
            }
        }
        if(!listPermissionNeeded.isEmpty()){
            //권한 요청 하는 부분
            ActivityCompat.requestPermissions(this,listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),1);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode ==10){
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));

        }
    }


    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    public void mOnDelete(View v) {
        //메인 클래스 호출
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }*/

}
