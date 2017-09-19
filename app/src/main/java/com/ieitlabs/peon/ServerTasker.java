package com.ieitlabs.peon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.loopj.android.http.HttpGet;

import org.json.JSONObject;

import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;
import  android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Lord on 9/17/2017.
 */

public class ServerTasker extends AsyncTask<Void,Void,String> {

    private Context mContext; // context reference
    private int TaskCode;
    private Activity activity;
    private String strURL = "";
    public View v;

    public ServerTasker(Context context,Activity activity,int TaskCode,String url){ //constructor
        this.mContext = context;
        this.TaskCode = TaskCode;
        this.activity = activity;
        this.strURL = url;
    }


    @Override
    protected void onPostExecute(String content) {
        super.onPostExecute(content);
        Intent i;
        DatabaseAdapter d = new DatabaseAdapter(mContext);

        if(content.equals(""))
        {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try
                    {
                        Toast.makeText(mContext,"Error: No internet",Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
        try {
            final JSONObject rowObject = new JSONObject(content);
            String res = rowObject.getString("response");
            switch (TaskCode)
            {
                case 1:
                    if(res.equals("success"))
                    {
                        Intent ii = new Intent(activity,SideBar.class);
                        activity.startActivity(ii);
                    }
                    else
                    {
                        i = new Intent(activity,LoginActivity.class);
                        activity.startActivity(i);
                        activity.finish();
                    }
                    break;
                case 2:
                    d.setAppMeta("session","");
                    d.setAppMeta("email","");
                    d.setAppMeta("ac_type","");
                    d.setAppMeta("org","");
                    d.setAppMeta("uid","");
                    i = new Intent(activity,LoginActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                    break;
                case 3:
                    if(res.equals("success"))
                    {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try
                                {
                                    SideBar sideBar = (SideBar) mContext;
                                    sideBar.switchFragment(R.id.nav_vgroups);

                                    Toast.makeText(mContext,rowObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else
                    {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try
                                {
                                    Toast.makeText(mContext,rowObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try
                    {
                        Toast.makeText(mContext,"Error: No internet",Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });
            return;
        }

    }

    @Override
    protected String doInBackground(Void... params) {

        try
        {
            DatabaseAdapter d = new DatabaseAdapter(mContext);

           // String url="http://peon.ml/api/checkauth?u="+ URLEncoder.encode(d.getAppMeta("email"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8");

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(strURL);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
