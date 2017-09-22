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

public class NoticeBoardAdapter extends BaseAdapter {
    ArrayList<String[]> result;
    Context context;
    private static LayoutInflater inflater=null;

    public NoticeBoardAdapter(ArrayList<String[]> result, Context context) {
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
        TextView txtNoticeID;
        TextView tvSubject;
        TextView tvMessage;
        TextView tvAuthor;
        TextView tvDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String[] strCurRow = result.get(position);
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_notice_layout, null);
        holder.tvSubject = (TextView) rowView.findViewById(R.id.txt_subject);
        holder.tvMessage = (TextView) rowView.findViewById(R.id.txtMessage);
        holder.tvAuthor = (TextView) rowView.findViewById(R.id.txt_filename);
        holder.tvDate = (TextView) rowView.findViewById(R.id.txtDate);
        holder.txtNoticeID = (TextView)rowView.findViewById(R.id.txtNoticeID);

        holder.txtNoticeID.setText(strCurRow[0]);
        holder.tvSubject.setText(strCurRow[1]);
        holder.tvMessage.setText(strCurRow[2]);
        holder.tvAuthor.setText(strCurRow[3]);
        holder.tvDate.setText(strCurRow[4]);



        return rowView;
    }


}
