package com.ieitlabs.peon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.net.URLEncoder;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
               setContentView(R.layout.activity_splash);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    (new DatabaseAdapter(getApplicationContext())).CopyDB();
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    DatabaseAdapter d = new DatabaseAdapter(SplashActivity.this);
                    try {
                        String url= "http://peon.ml/api/checkauth?u="+ URLEncoder.encode(d.getAppMeta("email"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8");

                        (new ServerTasker(SplashActivity.this,SplashActivity.this,1,url)).execute((Void)null);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        timerThread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
