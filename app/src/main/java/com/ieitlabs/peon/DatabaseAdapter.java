package com.ieitlabs.peon;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Lord on 9/17/2017.
 */

class DatabaseAdapter extends SQLiteOpenHelper {
    private Context mContext;
    private static final String DBNAME = "localdb.sqlite";
    private static final String DBLOCATION = "data/data/com.ieitlabs.peon/databases/";
    private SQLiteDatabase mDatabase;
    public DatabaseAdapter(Context context) {
        super(context, DBNAME, null, 3);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void OpenDatabase()
    {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen())
        {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);

    }
    public void CloseDatabase(){

        if(mDatabase != null)
        {
            mDatabase.close();
        }
    }
    public boolean CopyDB()
    {
    Context context = mContext;
        try {
            InputStream IS = context.getAssets().open(DBNAME);
            String OF = DBLOCATION + DBNAME;
            File f = new File(OF);
            if(f.exists())
            {
                Log.d("DatabaseAdapter","Database already exists");
                return true;
            }
            else
            {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            OutputStream OS = new FileOutputStream(OF,true);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = IS.read(buff))>0)
            {
                OS.write(buff,0,length);
            }
            OS.flush();
            OS.close();
            Log.d("DatabaseAdapter","Database copied successfully");
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("DatabaseAdapter","Copy database failed with message: " + e.getMessage());
            return false;
        }
    }

    public String getSession(String metaname)
    {
        OpenDatabase();
        Cursor cur = mDatabase.rawQuery("SELECT * FROM `app_meta` WHERE `name` = ? Order by `id` ASC",new String[]{metaname});
        String tmpStr = "";
        if(cur.getCount()>0)
        {
            cur.moveToFirst();
            tmpStr =  String.valueOf(cur.getString(2));
        }
        return tmpStr;
    }
    public boolean setAppMeta(String metaname,String metavalue)
    {
        OpenDatabase();
        try{
            mDatabase.execSQL("UPDATE `app_meta` SET `value`=? WHERE `id`=?;",new String[]{metavalue,metaname});
        }
        catch(SQLException e)
        {
            CloseDatabase();
            return false;
        }
        return true;
    }


}
