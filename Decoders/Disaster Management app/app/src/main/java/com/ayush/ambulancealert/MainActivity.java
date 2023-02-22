package com.ayush.ambulancealert;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    double ax,ay,az;   // these are the acceleration in x,y and z axis
    double thresholdLower = 7.5;

    double thresholdUpper = 10.5;
    int count=0;
    int earthquake=0;

    TextView cordinate_representer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        MediaPlayer mp;
//        mp=MediaPlayer.create(getApplicationContext(),R.raw.alert);
//        mp.start();


//        try {
//            mp= new MediaPlayer();
//
//            mp.setDataSource("/storage/emulated/0/Download/Baby_320(PaglaSongs).mp3");
//            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                             @Override
//                                             public void onPrepared(MediaPlayer mediaPlayer) {
//                                                 mediaPlayer.start();
//                                             }
//
//                                         });}
//        catch (Exception e){
//            e.printStackTrace();
//        }


        cordinate_representer=findViewById(R.id.cords);
        new RequestTask().execute("http://10.142.5.85:5000/");
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                System.out.println(token);
                String s= String.format("http://10.142.5.85:5000/register?fcm=%s&phoneNo=1234",token);
                new RequestTask().execute(s);
                Log.d(TAG, "retrieve token successful : " + token);

            } else{
                Log.w(TAG, "token should not be null...");
            }
        }).addOnFailureListener(e -> {

        }).addOnCanceledListener(() -> {

        }).addOnCompleteListener(task -> Log.v(TAG, "This is the token : " + task.getResult()));



    }
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];
            cordinate_representer.setText(ax+"   "+ay+"     "+az);
//            System.out.println(ax+"    "+ay+"    "+az);

            if((ax<thresholdLower || ax>=thresholdUpper) && (ay<thresholdLower || ay>=thresholdUpper )&& (az<thresholdLower || az>=thresholdUpper)){
//                System.out.println("earthquake   earthquake");
//                System.out.println(count);
                count++;
//                System.out.println(count);
                if(count>=5 && earthquake==0){
                    earthquake=1;
                    new RequestTask().execute("http://10.142.5.85:5000/earthquake/1234/5678");}
            }
            else{
//                System.out.println("NOT EQ");
                count=0;
//                System.out.println(count);
            }
        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("data get data get ");
            checkOverlayPermission();
            startService();

        }};

    public void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, ForegroundService.class));
                } else {
                    startService(new Intent(this, ForegroundService.class));
                }
            }
        }else{
            startService(new Intent(this, ForegroundService.class));
        }
    }

    public void checkOverlayPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        startService();
    }

}