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
import android.widget.TextView;

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

    public static LinkDialog createLinkDialog(String name, String url) {
        LinkDialog dialog = createLinkDialog();
        dialog.setUrl(url);
        dialog.setName(name);
        return dialog;
    }

    public static LinkDialog createLinkDialog() {

        return new LinkDialog();
    }

    public LinkDialog() {

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
        TextView ok = (TextView) dialog.findViewById(R.id.confirm_btn);
        TextView cancle = (TextView) dialog.findViewById(R.id.cancel_btn);
        final EditText urledt = (EditText) dialog.findViewById(R.id.et_linkurl);
        final EditText nameedt = (EditText) dialog.findViewById(R.id.et_linkname);

        if (name != null) {
            nameedt.setText(name);
        }
        if (url != null) {
            urledt.setText(url);
        }

        ok.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirmButtonClick(nameedt.getText().toString(), urledt.getText().toString());
            }
        });

        cancle.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelButtonClick();
            }
        });

        //remove title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
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
