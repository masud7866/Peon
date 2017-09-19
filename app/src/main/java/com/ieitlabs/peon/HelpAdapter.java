package com.ieitlabs.peon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by masud on 19-Sep-17.
 */

public class HelpAdapter extends ExpandableRecyclerAdapter<HelpTitleParentViewHolder,HelpTitleChildViewerHolder>{

    LayoutInflater inflater;

    public HelpAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public HelpTitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.help_layout_parent,viewGroup,false);
        return new HelpTitleParentViewHolder(view);
    }

    @Override
    public HelpTitleChildViewerHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.help_layout_child,viewGroup,false);
        return new HelpTitleChildViewerHolder(view);

    }

    @Override
    public void onBindParentViewHolder(HelpTitleParentViewHolder helpTitleParentViewHolder, int i, Object o) {
        HelpTitleParent title = (HelpTitleParent) o;
        helpTitleParentViewHolder._textView.setText(title.getTitle());

    }

    @Override
    public void onBindChildViewHolder(HelpTitleChildViewerHolder helpTitleChildViewerHolder, int i, Object o) {
        HelpTitleChild title = (HelpTitleChild) o;
        helpTitleChildViewerHolder.option1.setText(title.getOption1());


    }
}
