package com.zhiyi.richtexteditorlib.view.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyi.richtexteditorlib.R;
import com.zhiyi.richtexteditorlib.view.dialogs.base.BaseDialogFragment;

/**
 * @Author Jliuer
 * @Date 2017/11/19/18:52
 * @Email Jliuer@aliyun.com
 * @Description 链接编辑
 */
public class LinkDialog extends BaseDialogFragment {
    public static final String Tag = "link_dialog_fragment";
    private OnDialogClickListener listener;
    private String name;
    private String url;

    private String titleStr;

    private String nameHinit;
    private String urlHinit;

    private String confirmStr;
    private String cancleStr;

    private boolean nameVisible = true;
    private boolean urlVisible = true;
    private boolean needNumFomatFilter = false;

    private TextView errorTip;


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
        errorTip = (TextView) dialog.findViewById(R.id.tv_error_tip);
        TextView title = (TextView) dialog.findViewById(R.id.tv_tittle);
        TextView cancle = (TextView) dialog.findViewById(R.id.cancel_btn);
        final EditText urledt = (EditText) dialog.findViewById(R.id.et_linkurl);
        final EditText nameedt = (EditText) dialog.findViewById(R.id.et_linkname);

        RxTextView.textChanges(urledt).subscribe(charSequence -> {
            errorTip.setText("");
            errorTip.setVisibility(View.GONE);
        });

        if (titleStr != null) {
            title.setText(titleStr);
        }
        if (name != null) {
            nameedt.setText(name);
        }
        if (url != null) {
            urledt.setText(url);
        }

        if (nameHinit != null) {
            nameedt.setHint(nameHinit);
        }

        if (urlHinit != null) {
            urledt.setHint(urlHinit);
            if (needNumFomatFilter) {
                urledt.setGravity(Gravity.CENTER);
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new MyNumFormatInputFilter();
                urledt.setFilters(filters);
                urledt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            }
        }

        if (confirmStr != null) {
            ok.setText(nameHinit);
        }
        if (cancleStr != null) {
            cancle.setText(urlHinit);
        }

        nameedt.setVisibility(nameVisible ? View.VISIBLE : View.GONE);
        urledt.setVisibility(urlVisible ? View.VISIBLE : View.GONE);


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

    public LinkDialog setListener(OnDialogClickListener listener) {
        this.listener = listener;
        return this;
    }

    public String getName() {
        return name;
    }

    public LinkDialog setName(String name) {
        this.name = name;
        return this;
    }

    @SuppressWarnings("unused")
    public String getUrl() {
        return url;
    }

    public LinkDialog setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public LinkDialog setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    public String getNameHinit() {
        return nameHinit;
    }

    public LinkDialog setNameHinit(String nameHinit) {
        this.nameHinit = nameHinit;
        return this;
    }

    public boolean isNeedNumFomatFilter() {
        return needNumFomatFilter;
    }

    public LinkDialog setNeedNumFomatFilter(boolean needNumFomatFilter) {
        this.needNumFomatFilter = needNumFomatFilter;
        return this;
    }

    public String getUrlHinit() {
        return urlHinit;
    }

    public LinkDialog setUrlHinit(String urlHinit) {
        this.urlHinit = urlHinit;
        return this;
    }

    public String getConfirmStr() {
        return confirmStr;
    }

    public LinkDialog setConfirmStr(String confirmStr) {
        this.confirmStr = confirmStr;
        return this;
    }

    public String getCancleStr() {
        return cancleStr;
    }

    public LinkDialog setCancleStr(String cancleStr) {
        this.cancleStr = cancleStr;
        return this;
    }

    public boolean isNameVisible() {
        return nameVisible;
    }

    public LinkDialog setNameVisible(boolean nameVisible) {
        this.nameVisible = nameVisible;
        return this;
    }

    public boolean isUrlVisible() {
        return urlVisible;
    }

    public LinkDialog setUrlVisible(boolean urlVisible) {
        this.urlVisible = urlVisible;
        return this;
    }

    public void setErrorMessage(String string) {
        errorTip.setVisibility(View.VISIBLE);
        errorTip.setText(string);
    }

    public interface OnDialogClickListener {
        void onConfirmButtonClick(String name, String url);

        void onCancelButtonClick();
    }

    class MyNumFormatInputFilter extends LoginFilter.UsernameFilterGeneric {

        private String digits = "1234567890";

        @Override
        public boolean isAllowed(char c) {
            if (digits.indexOf(c) != -1) {
                return true;
            }
            return false;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // 不能以 0  开始
            String replace = source.toString().replaceAll("^0*", "");
            return super.filter(replace, start, end, dest, dstart, dend);
        }
    }
}
