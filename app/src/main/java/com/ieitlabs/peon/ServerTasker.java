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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    public GridView gv;
    public String mid = "";

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
        final DatabaseAdapter d = new DatabaseAdapter(mContext);

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
                case 7:     //View Group Users
                    if(res.equals("success"))
                    {
                        if(rowObject.has("data"))
                        {
                            try {
                                JSONArray myArray = new JSONArray(rowObject.getString("data"));
                                final TableView<String[]> tableView = (TableView<String[]>) v.findViewById(R.id.tblManageUser);
                                List<String[]> listStr = new ArrayList<String[]>();
                                for(int i1 = 0; i1 < myArray.length(); i1++){
                                    String[] s = {myArray.getJSONObject(i1).getString("email"),myArray.getJSONObject(i1).getString("role")};
                                    listStr.add(s);
                                }
                                if(listStr.size()==0)
                                {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try
                                            {
                                                Toast.makeText(mContext,"No users found!",Toast.LENGTH_SHORT).show();
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
                case 8:     //Create Notice
                    if(res.equals("success"))
                    {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try
                                {
                                    SideBar sideBar = (SideBar) mContext;
                                    sideBar.switchFragment(R.id.nav_notice);

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
                case 9:     //View Notices
                    if(res.equals("success")) {
                        if (rowObject.has("data")) {
                            try {
                                JSONArray myArray = new JSONArray(rowObject.getString("data"));
                                ArrayList<String[]> listStr = new ArrayList<String[]>();
                                for(int i1 = 0; i1 < myArray.length(); i1++){
                                    String[] s = {myArray.getJSONObject(i1).getString("id"),myArray.getJSONObject(i1).getString("subject"),myArray.getJSONObject(i1).getString("message"),myArray.getJSONObject(i1).getString("author"),myArray.getJSONObject(i1).getString("created")};
                                    listStr.add(s);
                                }
                                gv.setAdapter(new NoticeBoardAdapter(listStr, mContext));
                                if(listStr.size()==0)
                                {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try
                                            {
                                                Toast.makeText(mContext,"Empty Noticeboard",Toast.LENGTH_SHORT).show();
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
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
                case 10:    //Delete notice
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            try
                            {
                                Toast.makeText(mContext,rowObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                String url= "http://peon.ml/api/viewnotices?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8");
                                //Log.d("ViewGroups",url);
                                ServerTasker mViewGroupTask = new ServerTasker(mContext,activity,9,url);
                                mViewGroupTask.v = v;
                                mViewGroupTask.gv = gv;
                                mViewGroupTask.execute((Void)null);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });

                    break;
                case 11:    //Organization Admin Dashboard
                    if(res.equals("success")) {
                        if (rowObject.has("data")) {
                            try {
                                JSONObject myJSONObj = rowObject.getJSONObject("data");
                                ((TextView)v.findViewById(R.id.text_number_group)).setText(myJSONObj.getString("group_count"));
                                ((TextView)v.findViewById(R.id.text_number_user)).setText(myJSONObj.getString("user_count"));
                                JSONArray groupArray = new JSONArray(myJSONObj.getString("groups"));
                                JSONArray userArray = new JSONArray(myJSONObj.getString("users"));
                                String[] strGroupArray = new String[groupArray.length()];
                                String[] strUserArray = new String[userArray.length()];
                                for(int i1 = 0; i1 < groupArray.length(); i1++){
                                    strGroupArray[i1] = groupArray.getString(i1);
                                }
                                for(int i1 = 0; i1 < userArray.length(); i1++){
                                    strUserArray[i1] = userArray.getString(i1);
                                }
                                ListView lvnewUsers =  (ListView)v.findViewById(R.id.lv_users_new);
                                ListView lvnewGroups =  (ListView)v.findViewById(R.id.lv_groups_new);

                                ArrayAdapter<String> itemsAdapter1 =
                                        new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, strUserArray);
                                ArrayAdapter<String> itemsAdapter2 =
                                        new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, strGroupArray);
                                lvnewGroups.setAdapter(itemsAdapter2);
                                lvnewUsers.setAdapter(itemsAdapter1);

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
                case 12:        //Create new conversation
                    if(res.equals("success")) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try
                                {
                                    SideBar sideBar = (SideBar) mContext;
                                    sideBar.switchFragment(R.id.nav_messages);

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
                case 13:        //View inbox
                    if(res.equals("success")) {
                        if (rowObject.has("data")) {
                            try {
                                JSONArray myArray = new JSONArray(rowObject.getString("data"));
                                ArrayList<String[]> listStr = new ArrayList<String[]>();
                                for(int i1 = 0; i1 < myArray.length(); i1++){
                                    String[] s = {myArray.getJSONObject(i1).getString("id"),myArray.getJSONObject(i1).getString("subject"),myArray.getJSONObject(i1).getString("created_by"),myArray.getJSONObject(i1).getString("created")};
                                    listStr.add(s);
                                }
                                gv.setAdapter(new ConversationAdapter(listStr, mContext));
                                if(listStr.size()==0)
                                {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try
                                            {
                                                Toast.makeText(mContext,"Inbox is empty",Toast.LENGTH_SHORT).show();
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
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
                case 14:        //Delete Conversation
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            try
                            {
                                Toast.makeText(mContext,rowObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                String url= "http://peon.ml/api/viewconversations?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8");
                                //Log.d("ViewGroups",url);
                                ServerTasker mViewGroupTask = new ServerTasker(mContext,activity,13,url);
                                mViewGroupTask.v = v;
                                mViewGroupTask.gv = gv;
                                mViewGroupTask.execute((Void)null);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });

                    break;
                case 15:        //View Single Message
                    if(res.equals("success")) {
                        if (rowObject.has("data")) {
                            try {
                                JSONArray myArray = new JSONArray(rowObject.getString("data"));
                                ArrayList<String[]> listStr = new ArrayList<String[]>();
                                for(int i1 = 0; i1 < myArray.length(); i1++){
                                    String[] s = {myArray.getJSONObject(i1).getString("msg"),myArray.getJSONObject(i1).getString("sender"),myArray.getJSONObject(i1).getString("created")};
                                    listStr.add(s);
                                }
                                gv.setAdapter(new SingleMessageAdapter(listStr, mContext));
                                if(listStr.size()==0)
                                {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try
                                            {
                                                Toast.makeText(mContext,"Inbox is empty",Toast.LENGTH_SHORT).show();
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
                                    gv.smoothScrollToPosition(listStr.size());
                                }
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
                case 16:        //Send Message
                    if(res.equals("success")) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try
                                {
                                    String url= "http://peon.ml/api/viewsinglemessage?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8") + "&mid=" + URLEncoder.encode(mid,"UTF-8");
                                    //Log.d("ViewGroups",url);
                                    ServerTasker mViewGroupTask = new ServerTasker(mContext,activity,15,url);
                                    mViewGroupTask.v = v;
                                    mViewGroupTask.gv = gv;
                                    mViewGroupTask.execute((Void)null);
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
                case 17:        //View Docs


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
