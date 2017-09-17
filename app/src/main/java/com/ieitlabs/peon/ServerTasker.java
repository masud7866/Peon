package com.ieitlabs.peon;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;

/**
 * Created by Lord on 9/17/2017.
 */

public class ServerTasker extends AsyncTask<Void,Void,Boolean> {

    private Context mContext; // context reference
    private int TaskCode;

    public ServerTasker(Context context,int TaskCode){ //constructor
        this.mContext = context;
        this.TaskCode = TaskCode;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        switch (TaskCode)
        {
            case 1:     //Check Authorization

                break;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
