package com.example.sooyeon.graduationproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sooyeon.graduationproject.login.MainActivity;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(getApplicationContext(), MainActivity.class );
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //액티비티의 타이틀을 숨긴다.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        GifImageView popcorn = (GifImageView) findViewById(R.id.gif_image);
        try {
            InputStream inputStream = getAssets().open("pop.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            popcorn.setBytes(bytes);
            popcorn.startAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_splash);

    }//end onCreate

    @Override
    protected void onResume() {
        super.onResume();
        //다시 화면에 들어왔을때 예약 걸어주기
        handler.postDelayed(r, 2000); //2초 뒤에 Runnable객체 수행
    }

    @Override
    protected void onPause() {
        super.onPause();
        //화면을 벗어나면, handler에 예약해놓은 작업을 취소하자
        handler.removeCallbacks(r);
    }
}
