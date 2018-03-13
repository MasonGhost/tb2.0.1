package com.zhiyi.richtexteditorlib.view.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private boolean needMaxInputFomatFilter = false;

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

        RxTextView.textChanges(urledt).subscribe((CharSequence charSequence) -> ok.setEnabled(!TextUtils.isEmpty(charSequence)));

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
            urledt.setGravity(Gravity.CENTER);
            if (needNumFomatFilter) {
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new MyNumFormatInputFilter();
                urledt.setFilters(filters);
                urledt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            } else if (needMaxInputFomatFilter) {
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new MaxTextLengthFilter(15);
                urledt.setFilters(filters);
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

    public boolean isNeedMaxInputFomatFilter() {
        return needMaxInputFomatFilter;
    }

    public LinkDialog setNeedMaxInputFomatFilter(boolean needMaxInputFomatFilter) {
        this.needMaxInputFomatFilter = needMaxInputFomatFilter;
        return this;
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

    public class MyNumFormatInputFilter implements InputFilter {
        Pattern mPattern;

        /**
         * 输入的最大天数
         */
        private static final int MAX_VALUE = 30;

        private static final String ZERO = "0";

        public MyNumFormatInputFilter() {
            mPattern = Pattern.compile("([0-9])*");
        }

        /**
         * @param source 新输入的字符串
         * @param start  新输入的字符串起始下标，一般为0
         * @param end    新输入的字符串终点下标，一般为source长度-1
         * @param dest   输入之前文本框内容
         * @param dstart 原内容起始坐标，一般为0
         * @param dend   原内容终点坐标，一般为dest长度-1
         * @return 输入内容
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceText = source.toString();
            String destText = dest.toString();


            if (TextUtils.isEmpty(sourceText)) {
                return "";
            }

            // 只能两位数
            if (dest.length() > 1) {
                return dest.subSequence(dstart, dend);
            }

            Matcher matcher = mPattern.matcher(source);

            if (!matcher.matches()) {
                return dest.subSequence(dstart, dend);
            } else {
                //首位不能输入 0
                if ((ZERO.equals(source.toString())) && TextUtils.isEmpty(destText)) {
                    return "";
                }
            }

            //验证输入金额的大小
            int day = Integer.parseInt(destText + sourceText);
            if (day > MAX_VALUE) {

            }
            return dest.subSequence(dstart, dend) + sourceText;
        }
    }

    class MaxTextLengthFilter implements InputFilter {

        private int mMaxLength;

        public MaxTextLengthFilter(int max) {
            mMaxLength = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int keep = mMaxLength - (dest.length() - (dend - dstart));
            if (keep < (end - start)) {
                setErrorMessage("编辑群名称，2–15个字符");
            }
            if (keep <= 0) {
                return "";
            } else if (keep >= end - start) {
                return null;
            } else {
                return source.subSequence(start, start + keep);
            }
        }
    }
}
