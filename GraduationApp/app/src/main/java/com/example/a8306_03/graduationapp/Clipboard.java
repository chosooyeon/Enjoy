package com.example.a8306_03.graduationapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class Clipboard extends Service {
    private ClipboardManager mCM;
    IBinder mBinder;
    int mStartMode;
    String newClip;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mCM.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {
                newClip = mCM.getText().toString();
                Toast.makeText(getApplicationContext(), newClip.toString()+"을 등록해주세요!",  Toast.LENGTH_LONG).show();
                //StyleableToast.makeText(getApplicationContext(), newClip.toString(),  R.style.exampleToast).show();
                Log.i("LOG", newClip.toString() + "");
            }
        });
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}