package com.ieitlabs.peon;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Created by masud on 19-Sep-17.
 */

public class HelpTitleChildViewerHolder extends ChildViewHolder {
    public TextView option1;
    public HelpTitleChildViewerHolder(View itemView){
        super(itemView);
        option1 = (TextView)itemView.findViewById(R.id.option1);
    }

}
