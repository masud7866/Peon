package com.ieitlabs.peon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.util.Log;

import com.loopj.android.http.HttpGet;

import org.json.JSONObject;

import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Lord on 9/17/2017.
 */

public class ServerTasker extends AsyncTask<Void,Void,Boolean> {

    private Context mContext; // context reference
    private int TaskCode;
    private Activity activity;
    public ServerTasker(Context context,Activity activity,int TaskCode){ //constructor
        this.mContext = context;
        this.TaskCode = TaskCode;
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Intent i;
        if(!aBoolean)
        {
            switch (TaskCode)
            {
                case 1:
                     i = new Intent(activity,LoginActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                    break;
                case 2:
                    i = new Intent(activity,LoginActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                    break;
            }
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        switch (TaskCode)
        {
            case 1:     //Check Authorization
                try
                {
                    DatabaseAdapter d = new DatabaseAdapter(mContext);

                    String url="http://peon.ml/api/checkauth?u="+ URLEncoder.encode(d.getAppMeta("email"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8");

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet request = new HttpGet(url);

                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    String content = EntityUtils.toString(entity);
                    final JSONObject rowObject = new JSONObject(content);
                    String res = rowObject.getString("response");
                    if(res.equals("success"))
                    {
                        Intent i = new Intent(activity,SideBar.class);
                        activity.startActivity(i);
                        return true;
                    }
                    else if(res.equals("error"))
                    {
                        Intent i = new Intent(activity,LoginActivity.class);
                        activity.startActivity(i);
                        return false;
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }

                break;
            case 2: //Logout
                try
                {
                    DatabaseAdapter d = new DatabaseAdapter(mContext);

                    String url="http://peon.ml/api/logout?uid="+ d.getAppMeta("uid") +"&skey=" + d.getAppMeta("session");

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet request = new HttpGet(url);

                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();

                    d.setAppMeta("session","");
                    d.setAppMeta("email","");
                    d.setAppMeta("ac_type","");
                    d.setAppMeta("org","");
                    d.setAppMeta("uid","");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
                break;
            case 3:

                break;
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
