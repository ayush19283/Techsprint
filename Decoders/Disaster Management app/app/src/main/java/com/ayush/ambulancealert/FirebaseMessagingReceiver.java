package com.ayush.ambulancealert;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Collections;
import java.util.Map;

public class FirebaseMessagingReceiver extends FirebaseMessagingService {
    String text,sender;
//    Database db = new Database(this);
    @Override
    public void onNewToken(String token) {
        Log.d("TAG", "New token: " + token);
        System.out.println(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MediaPlayer mp;
        mp=MediaPlayer.create(getApplicationContext(),R.raw.alert);
        mp.start();
        Map<String, String> data = remoteMessage.getData();
//        AlertDialog dialog = AlertDialog.builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
        text= data.get("title");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, ForegroundService.class));
        }

//        if(text.matches("show"))
            send_userDetails();
        System.out.println(text);
        System.out.println("coming    coming");
        System.out.println(R.raw.alert);


        }

    public void send_userDetails(){
        System.out.println("asdfasjdfaljsfk alsdkjfkla lkjasdflk ");
        Intent intent = new Intent("GPSLocationUpdates");
        intent.putExtra("ring", "yes");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



    }


