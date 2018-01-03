package com.zhiyi.richtexteditorlib.view.dialogs.base;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;

public class BaseDialogFragment extends AppCompatDialogFragment {

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
