package com.ieitlabs.peon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.BoolRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.util.Log;

import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

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
    public View mProgressView;
    public View mLoginFormView;

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
                case 1:   //Check Authenication on SplashActivity
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
                case 2:    //Logout
                    d.setAppMeta("session","");
                    d.setAppMeta("email","");
                    d.setAppMeta("ac_type","");
                    d.setAppMeta("org","");
                    d.setAppMeta("uid","");
                    i = new Intent(activity,LoginActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                    break;
                case 3:     //Create Group
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
                case 4:     //View Groups

                    if(res.equals("success"))
                    {
                        if(rowObject.has("data"))
                        {
                            try {
                                JSONArray myArray = new JSONArray(rowObject.getString("data"));
                                final TableView<String[]> tableView = (TableView<String[]>) v.findViewById(R.id.tblGroupView);
                                List<String[]> listStr = new ArrayList<String[]>();
                                for(int i1 = 0; i1 < myArray.length(); i1++){
                                    String[] s = {myArray.getJSONObject(i1).getString("title"),myArray.getJSONObject(i1).getString("members")};
                                    listStr.add(s);
                                }
                                if(listStr.size()==0)
                                {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try
                                            {
                                                Toast.makeText(mContext,"No group found!",Toast.LENGTH_SHORT).show();
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }

                                tableView.setColumnCount(2);
                                tableView.setDataAdapter(new SimpleTableDataAdapter(mContext, listStr));
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
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
                case 5:     //Invite to group
                    if(res.equals("success"))
                    {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try
                                {
                                    SideBar sideBar = (SideBar) mContext;
                                    sideBar.switchFragment(R.id.nav_manage_user);

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
                case 6:         //Get User Meta after Login if the user is not organization administrator
                    if(res.equals("success"))
                    {
                        if(rowObject.has("data"))
                        {
                            try {
                                JSONArray myArray = new JSONArray(rowObject.getString("data"));
                                d.setAppMeta("groupid",myArray.getJSONObject(0).getString("group_id"));
                                d.setAppMeta("group_role",myArray.getJSONObject(0).getString("group_role"));
                                d.setAppMeta("group_title",myArray.getJSONObject(0).getString("group_title"));
                                d.setAppMeta("fname",myArray.getJSONObject(0).getString("fname"));
                                d.setAppMeta("lname",myArray.getJSONObject(0).getString("lname"));
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        i = new Intent(activity,SideBar.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                    }
                    else
                    {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try
                                {
                                    Toast.makeText(mContext,rowObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                    LoginActivity loginActivity = (LoginActivity) mContext;
                                    loginActivity.showProgress(false);
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
            e.printStackTrace();
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
