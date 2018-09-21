package com.example.sooyeon.graduationproject.login;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JoinActivity extends AppCompatActivity {

    private Button join;
    private EditText joinId;
    private EditText joinName;
    private EditText joinPwd;
    private EditText joinRpwd;

    private ImageView mImgCamera;
    //사진이 저장된 경로 - onActivityResult()로부터 받는 데이터
    private Uri mCaptureUri;
    //사진이 저장된 단말기상의 실제 경로
    private String mPhotoPath;
    //startActivityForResult에 넘겨주는 값. 이 값이 나중에 onActivityResult()로 돌아와서 내가 던진값인지를 구별할 때 사용하는 상수이다.
    public static final int REQUEST_IMAGE_CAPTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mImgCamera = findViewById(R.id.imgCamera);

        //카메라를 사용하기 위한 퍼미션을 요청한다.
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 0);

        //카메라 버튼
        findViewById(R.id.btnPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //Button btnPhoto = findViewById(R.id.btnPhoto);
        Button join = findViewById(R.id.join);
        joinId = (EditText) findViewById(R.id.joinId);
        joinName = (EditText) findViewById(R.id.joinName);
        joinPwd = (EditText) findViewById(R.id.joinPwd);
        joinRpwd = (EditText) findViewById(R.id.joinRpwd);

        joinRpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = joinPwd.getText().toString();
                String confirm = joinRpwd.getText().toString();

                if (password.equals(confirm)){
                    joinPwd.setBackgroundColor(Color.GREEN);
                    joinRpwd.setBackgroundColor(Color.GREEN);
                }else {
                    joinPwd.setBackgroundColor(Color.RED);
                    joinRpwd.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( joinId.getText().toString().length() == 0 ) {
                    Toast.makeText(JoinActivity.this, "ID을 입력하세요!", Toast.LENGTH_SHORT).show();
                    joinId.requestFocus();
                    return;
                }


                if( joinName.getText().toString().length() == 0 ) {
                    Toast.makeText(JoinActivity.this, "Name을 입력하세요!", Toast.LENGTH_SHORT).show();
                    joinName.requestFocus();
                    return;
                }

                if( joinPwd.getText().toString().length() == 0 ) {
                    Toast.makeText(JoinActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    joinPwd.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( joinRpwd.getText().toString().length() == 0 ) {
                    Toast.makeText(JoinActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                    joinRpwd.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !joinPwd.getText().toString().equals(joinRpwd.getText().toString()) ) {
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    joinPwd.setText("");
                    joinRpwd.setText("");
                    joinPwd.requestFocus();
                    return;
                }

                finish();
            }
        });
    }// onCreate

    private void takePicture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mCaptureUri = Uri.fromFile( getOutputMediaFile() );
        } else {
            mCaptureUri = FileProvider.getUriForFile(this,
                    "com.example.sooyeon.graduationproject.fileprovider", getOutputMediaFile());
        }

        i.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);

        //Uri uri = FileProvider.getUriForFile(this, "", getOutputMediaFile());
        i.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);

        //내가 원하는 액티비티로 이동하고, 그 액티비티가 종료되면(finish)되면 다시 나의 액티비티의 onActivityResult() 메서드가 호출되는 구조이다.
        //내가 어떤 데이터를 받고 싶을때 상대 액티비티를 호출해주고 그 액티비티에서 호출한 나의 액티비티로 데이터를 넘겨주는 구조이다. 이때 호출되는 메서드가 onActivityResult()메서드이다.
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

    private File getOutputMediaFile() {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "cameraDemo");
        if(!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        mPhotoPath = file.getAbsolutePath();

        return file;

    }

    private void sendPicture() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
        Bitmap resizedBmp = getResizedBitmap(bitmap, 4, 100, 100);

        //사진이 캡쳐되서 들어오면 뒤집어져 있다. 이애를 다시 원상복구 시킨다.
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mPhotoPath);
        }catch (Exception e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;
        if(exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrienToDegree(exifOrientation);
        }else{
            exifDegree = 0;
        }
        Bitmap roatedBmp = roate(bitmap, exifDegree);
        mImgCamera.setImageBitmap(roatedBmp);
    }

    private int exifOrienToDegree(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap roate(Bitmap bmp, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    //비트맵의 사이즈를 줄여준다.
    public static Bitmap getResizedBitmap(Bitmap srcBmp, int size, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap resized = Bitmap.createScaledBitmap(srcBmp, width, height, true);
        return resized;
    }

    public static Bitmap getResizedBitmap(Resources resources, int id, int size, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap src = BitmapFactory.decodeResource(resources, id, options);
        Bitmap resized = Bitmap.createScaledBitmap(src, width, height, true);
        return resized;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //카메라ㅏ로부터 오는 데이터를 취득한다.
        if(resultCode == RESULT_OK) {

            if(requestCode == REQUEST_IMAGE_CAPTURE) {
                sendPicture();
            }
        }
    }//end onActivityResult()
}
