package com.zhiyi.richtexteditorlib.view.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.zhiyi.richtexteditorlib.R;
import com.zhiyi.richtexteditorlib.view.dialogs.base.BaseDialogFragment;

/**
 * @Author Jliuer
 * @Date 2017/11/19/18:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class LinkDialog extends BaseDialogFragment {
    public static final String Tag = "link_dialog_fragment";
    private OnDialogClickListener listener;
    private String name;
    private String url;

    public static LinkDialog createLinkDialog(String name,String url){
        LinkDialog dialog = createLinkDialog();
        dialog.setUrl(url);
        dialog.setName(name);
        return dialog;
    }

    public static LinkDialog createLinkDialog(){

        return new LinkDialog();
    }

    public LinkDialog(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @SuppressLint("FragmentLayoutNameNotPrefixed")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialog = inflater.inflate(R.layout.dialog_fragment_link, container);
        Button ok = (Button) dialog.findViewById(R.id.confirm_btn);
        Button cancle = (Button) dialog.findViewById(R.id.cancel_btn);
        final EditText urledt = (EditText) dialog.findViewById(R.id.url_edt);
        final EditText nameedt = (EditText) dialog.findViewById(R.id.name_edt);

        if(name != null){
            nameedt.setText(name);
        }
        if(url != null){
            urledt.setText(url);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onConfirmButtonClick(nameedt.getText().toString(), urledt.getText().toString());
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onCancelButtonClick();
                }
            }
        });

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title
        return dialog;
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public interface OnDialogClickListener {
        void onConfirmButtonClick(String name, String url);
        void onCancelButtonClick();
    }
}
