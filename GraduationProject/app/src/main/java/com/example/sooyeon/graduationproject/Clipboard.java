package com.example.sooyeon.graduationproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Clipboard extends Service {
    private ClipboardManager mCM;
    IBinder mBinder;
    int mStartMode;
    String newClip;

    NotificationManager Notifi_M;
    Notification Notifi ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mCM.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {
                newClip = mCM.getText().toString();
                Toast.makeText(getApplicationContext(), newClip.toString()+"을 등록해주세요!",  Toast.LENGTH_LONG).show();
                Log.i("LOG", newClip.toString() + "");

//                Intent intent = new Intent(Clipboard.this, MainActivity.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(Clipboard.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                Notifi = new Notification.Builder(getApplicationContext())
//                        .setContentTitle("Content Title")
//                        .setContentText("Content Text")
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setTicker("알림!!!")
//                        .setContentIntent(pendingIntent)
//                        .build();
//
//                //소리추가
//                Notifi.defaults = Notification.DEFAULT_SOUND;
//                //알림 소리를 한번만 내도록
//                Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
//                //확인하면 자동으로 알림이 제거 되도록
//                Notifi.flags = Notification.FLAG_AUTO_CANCEL;
//                Notifi_M.notify(777, Notifi);
//                //토스트 띄우기
//                Toast.makeText(Clipboard.this, "noti", Toast.LENGTH_LONG).show();
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