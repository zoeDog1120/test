package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.myapplication.MySurfaceView;

import java.util.HashMap;

public class MainActivity extends Activity {
//    SoundPool sp;
//    HashMap<Integer,Integer> hm;
//    int curStramId;
    //MyTdView mView;
    MySurfaceView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        intSoundPool();
//        Button b1 = (Button) this.findViewById(R.id.button1);
//        Button b2 = (Button) this.findViewById(R.id.button2);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playSound(1,0);
//            }
//        });
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sp.stop(curStramId);
//            }
//        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        mView = new MyTdView(this);
//        mView.requestFocus();
//        mView.setFocusableInTouchMode(true);
//        setContentView(mView);
        mView = new MySurfaceView(this);
        mView.requestFocus();
        mView.setFocusableInTouchMode(true);
        setContentView(mView);
    }
    @Override
    public void onResume(){
        super.onResume();
        mView.onResume();
    }

    @Override
    public  void onPause(){
        super.onPause();
        mView.onPause();
    }


//    public void intSoundPool(){
//        sp = new SoundPool.Builder()
//                .setMaxStreams(4)
//                .setAudioAttributes(new AudioAttributes.Builder()
//                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                        .build())
//                .build();
//        hm = new HashMap<Integer, Integer>();
//        hm.put(1,sp.load(this,R.raw.click1,1));
//    }
//    public void playSound(int sound,int loop){
//        AudioManager am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
//        float curvol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
//        float maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        float vol = curvol / maxVol;
//        curStramId = sp.play(hm.get(sound),vol,vol,1,loop,1.0f);
//    }
}
