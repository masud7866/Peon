package com.ieitlabs.peon;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masud on 19-Sep-17.
 */

public class HelpTitleCreator {
    static HelpTitleCreator _titlecreator;
    List<HelpTitleParent> _titleparents;

    public HelpTitleCreator(Context context) {
        _titleparents = new ArrayList<>();
        String[] stringArray = context.getResources().getStringArray(R.array.help_faq);
        HelpTitleParent title;
        for(String s:stringArray) {
            title = new HelpTitleParent(String.format(s));
            _titleparents.add(title);
        }
    }

    public static HelpTitleCreator get(Context context){
        if(_titlecreator==null)
            _titlecreator = new HelpTitleCreator(context);
        return _titlecreator;
    }

    public List<HelpTitleParent> getAll() {
        return _titleparents;
    }
}
