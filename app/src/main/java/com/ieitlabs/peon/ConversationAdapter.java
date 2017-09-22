package com.ieitlabs.peon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lord on 9/20/2017.
 */

public class ConversationAdapter extends BaseAdapter {
    ArrayList<String[]> result;
    Context context;
    private static LayoutInflater inflater=null;

    public ConversationAdapter(ArrayList<String[]> result, Context context) {
        this.result = result;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder
    {
        TextView txtConvID;
        TextView tvSubject;
        TextView tvAuthor;
        TextView tvDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String[] strCurRow = result.get(position);
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_conversation_layout, null);
        holder.tvSubject = (TextView) rowView.findViewById(R.id.txt_subject);
        holder.tvAuthor = (TextView) rowView.findViewById(R.id.txt_author);
        holder.tvDate = (TextView) rowView.findViewById(R.id.txt_time);
        holder.txtConvID = (TextView)rowView.findViewById(R.id.txtConvID);


        holder.txtConvID.setText(strCurRow[0]);
        holder.tvSubject.setText("Subject: " + strCurRow[1]);
        holder.tvAuthor.setText("By: " + strCurRow[2]);
        holder.tvDate.setText(strCurRow[3]);


        return rowView;
    }


}
