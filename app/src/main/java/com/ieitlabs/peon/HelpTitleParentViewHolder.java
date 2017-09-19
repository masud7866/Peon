package com.ieitlabs.peon;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by masud on 19-Sep-17.
 */

public class HelpTitleParentViewHolder extends ParentViewHolder{
    public TextView _textView;
    public HelpTitleParentViewHolder(View itemView){
        super(itemView);
        _textView = (TextView)itemView.findViewById(R.id.parentTitle);
    }

}
