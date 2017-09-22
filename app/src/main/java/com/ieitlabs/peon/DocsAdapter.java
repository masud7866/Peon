package com.ieitlabs.peon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lord on 9/20/2017.
 */

public class DocsAdapter extends BaseAdapter {
    ArrayList<String[]> result;
    Context context;
    private static LayoutInflater inflater=null;

    public DocsAdapter(ArrayList<String[]> result, Context context) {
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
        TextView txtDocID;
        TextView txtLink;
        TextView tvDescription;
        TextView tvAuthor;
        TextView tvDate;
        Button btnDownload;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String[] strCurRow = result.get(position);
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_conversation_layout, null);

        holder.tvDescription = (TextView) rowView.findViewById(R.id.txtDescription);
        holder.tvAuthor = (TextView) rowView.findViewById(R.id.txt_author);
        holder.tvDate = (TextView) rowView.findViewById(R.id.txt_time);
        holder.btnDownload = (Button) rowView.findViewById(R.id.btnDownload);
        holder.txtLink = (TextView) rowView.findViewById(R.id.txtDocLink);
        holder.txtDocID = (TextView)rowView.findViewById(R.id.txtDocID);


        holder.txtDocID.setText(strCurRow[0]);
        holder.txtLink.setText(strCurRow[1]);
        holder.tvDescription.setText(strCurRow[2]);
        holder.tvDate.setText(strCurRow[3]);
        holder.tvAuthor.setText(strCurRow[4]);

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return rowView;
    }


}
