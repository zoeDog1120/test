package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import com.cacher_func.catcherFun.MyActivity;

public class MainActivity extends Activity {
    MySurfaceView1 mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1 = (Button) this.findViewById(R.id.button1);
        Button b2 = (Button) this.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MyActivity.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public  void onPause(){
        super.onPause();
    }
}
