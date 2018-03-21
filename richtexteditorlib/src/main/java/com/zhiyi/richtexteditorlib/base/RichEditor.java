package com.zhiyi.richtexteditorlib.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

@SuppressWarnings({"unused"})
public abstract class RichEditor extends WebView {

    public enum Type {
        BOLD(0x06),
        ITALIC(0x07),
        STRIKETHROUGH(0x08),
        BLOCKQUOTE(0x09),
        H1(0x0a),
        H2(0x0b),
        H3(0x0c),
        H4(0x0d);

        //SUPERSCRIPT(1),//SUBSCRIPT(2),//UNDERLINE(3),
        private long typeCode;

        Type(long i) {
            typeCode = i;
        }

        public long getTypeCode() {
            return typeCode;
        }

        public boolean isMapTo(long id) {
            return typeCode == id;
        }
    }

    public interface OnTextChangeListener {
        void onTextChange(int titleLenght, int contentLenght);
    }

    public interface OnStateChangeListener {
        void onStateChangeListener(String text, List<Type> types);
    }

    public interface OnLinkClickListener {
        void onLinkClick(String linkName, String url);
    }

    public interface OnFocusChangeListener {
        void onFocusChange(boolean isFocus);
    }

    public interface AfterInitialLoadListener {
        void onAfterInitialLoad(boolean isReady);
    }

    public interface OnImageClickListener {
        void onImageClick(Long url);
    }

    public interface OnTextLengthChangeListener {
        void onTextLengthChange(long length);
    }

    public interface OnNoMarkdownWordChangeListener {
        void onNoMarkdownWordChange(String noMarkdwon);
    }

    public interface OnMarkdownWordChangeListener {
        void onMarkdownWordChange(String markdwon);
    }

    public interface OnImageDeleteListener {
        void onImageDelete(long tagId);
    }

    public interface OnMarkdownWordResultListener {
        void onMarkdownWordResult(String title, String markdwon, String noMarkdown, String html, boolean isPublish);
    }

    private static final String SETUP_HTML = "file:///android_asset/markdown/editor.html";
    private static final String SETUP_BASEURL = "file:///android_asset/markdown/";
    private static final String CALLBACK_SCHEME = "callback://";
    private static final String STATE_SCHEME = "state://";
    private static final String LINK_CHANGE_SCHEME = "change://";
    private static final String FOCUS_CHANGE_SCHEME = "focus://";
    private static final String IMAGE_CLICK_SCHEME = "image://";
    private boolean isReady = false;
    private boolean isDraftReady = false;
    private String mContents;
    private long mContentLength;
    private OnTextChangeListener mTextChangeListener;
    private OnStateChangeListener mStateChangeListener;
    private AfterInitialLoadListener mLoadListener;
    private OnScrollChangedCallback mOnScrollChangedCallback;
    private OnLinkClickListener mOnLinkClickListener;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnImageClickListener mOnImageClickListener;
    private OnTextLengthChangeListener mOnTextLengthChangeListener;

    private OnMarkdownWordChangeListener mOnMarkdownWordChangeListener;
    private OnImageDeleteListener mOnImageDeleteListener;
    private OnNoMarkdownWordChangeListener mOnNoMarkdownWordChangeListener;
    private OnMarkdownWordResultListener mOnMarkdownWordResultListener;


    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint({"SetJavaScriptEnabled", "addJavascriptInterface"})
    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }

        addJavascriptInterface(new Android4JsInterface(), "AndroidInterface");
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setWebViewClient(createWebViewClient());
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (!isReady) {
                    isReady = view.getUrl().equalsIgnoreCase(SETUP_HTML) || view.getUrl().equalsIgnoreCase(SETUP_BASEURL);
                }
                if (newProgress == 100 && mLoadListener != null) {
                    mLoadListener.onAfterInitialLoad(isReady);
                }
            }
        });
        mContentLength = 0;
        getSettings().setJavaScriptEnabled(true);
        load();
        //applyAttributes(context, attrs);
    }

    protected EditorWebViewClient createWebViewClient() {
        return new EditorWebViewClient();

    }

    protected void setOnTextChangeListener(OnTextChangeListener listener) {
        mTextChangeListener = listener;
    }

    public void setOnTextLengthChangeListener(OnTextLengthChangeListener onTextLengthChangeListener) {
        this.mOnTextLengthChangeListener = onTextLengthChangeListener;
    }

    protected void setOnDecorationChangeListener(OnStateChangeListener listener) {
        mStateChangeListener = listener;
    }

    protected void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        mLoadListener = listener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.mOnFocusChangeListener = onFocusChangeListener;
    }

    protected void setOnLinkClickListener(OnLinkClickListener onLinkClickListener) {
        this.mOnLinkClickListener = onLinkClickListener;
    }

    protected void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.mOnImageClickListener = onImageClickListener;
    }

    public void setOnMarkdownWordChangeListener(OnMarkdownWordChangeListener onMarkdownWordChangeListener) {
        mOnMarkdownWordChangeListener = onMarkdownWordChangeListener;
    }

    public void setOnImageDeleteListener(OnImageDeleteListener onImageDeleteListener) {
        mOnImageDeleteListener = onImageDeleteListener;
    }

    public void setOnNoMarkdownWordChangeListener(OnNoMarkdownWordChangeListener onNoMarkdownWordChangeListener) {
        mOnNoMarkdownWordChangeListener = onNoMarkdownWordChangeListener;
    }

    public void setOnMarkdownWordResultListener(OnMarkdownWordResultListener onMarkdownWordResultListener) {
        mOnMarkdownWordResultListener = onMarkdownWordResultListener;
    }

    private void callback(String text) {
        mContents = text.replaceFirst(CALLBACK_SCHEME, "");
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChange(mContents.length(), text.length());
        }
    }

    private void linkChangeCallBack(String text) {
        text = text.replaceFirst(LINK_CHANGE_SCHEME, "");
        String[] result = text.split("@_@");
        if (mOnLinkClickListener != null && result.length >= 2) {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
            mOnLinkClickListener.onLinkClick(result[0], result[1]);
        }
    }

    private void imageClickCallBack(String url) {
        if (mOnImageClickListener != null) {
            mOnImageClickListener.onImageClick(Long.valueOf(url.replaceFirst(IMAGE_CLICK_SCHEME, "")));
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }

    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    @SuppressWarnings("WeakerAccess")
    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }


    public void stateCheck(String text) {

        String state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH);
        List<Type> types = new ArrayList<>();
        for (Type type : Type.values()) {
            if (TextUtils.indexOf(state, type.name()) != -1) {
                types.add(type);
            }
        }

        if (mStateChangeListener != null) {
            mStateChangeListener.onStateChangeListener(state, types);
        }
    }

    public void getHtmlAsyn() {
        exec("javascript:RE.getHtml4Android()");
    }

    public String getHtml() {
        return mContents;
    }

    public void load() {
        LogUtils.d("load", "before load");
        loadUrl(SETUP_HTML);
        LogUtils.d("load", "after load");
    }

    public void loadDraft(String title, String html) {
        LogUtils.d("loadDraft", "before loadDraft");
        loadDataWithBaseURL(SETUP_BASEURL, html, "text/html", "utf-8", null);
        LogUtils.d("loadDraft", "after loadDraft");
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // still not support RTL.
        setPadding(start, top, end, bottom);
    }

    public void setEditorBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    public void destryWeb() {
        clearHistory();
        ((ViewGroup) getParent()).removeView(this);
        loadUrl("about:blank");
        stopLoading();
        setWebChromeClient(null);
        setWebViewClient(null);
        destroy();
    }

    public void setPlaceholder(String placeholder) {
        exec("javascript:RE.setPlaceholder('" + placeholder + "');");
    }

    public void loadCSS(String cssFile, String jsFile) {
        String jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +

                "    var script  = document.createElement(\"script\");" +
                "    script.type = \"text/javascript\";" +
                "    script.src = \"" + jsFile + "\";" +
                "    head.appendChild(script);" +

                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +

                "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    public void undo() {
        exec("javascript:RE.exec('undo');");
    }

    public void redo() {
        exec("javascript:RE.exec('redo');");
    }

    public void setBold() {

        exec("javascript:RE.saveRange();");
        exec("javascript:RE.exec('bold');");
    }


    public void setItalic() {
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.exec('italic');");
    }

    public void setStrikeThrough() {
        exec("javascript:RE.saveRange()");
        exec("javascript:RE.exec('strikethrough');");
    }

    public void setHeading(int heading, boolean b) {
        exec("javascript:RE.saveRange();");
        if (b) {
            exec("javascript:RE.exec('h" + heading + "')");
        } else {
            exec("javascript:RE.exec('p')");
        }
    }

    public void setBlockquote(boolean b) {
        exec("javascript:RE.saveRange();");
        if (b) {
            exec("javascript:RE.exec('blockquote')");
        } else {
            exec("javascript:RE.exec('p')");
        }
    }

    public void insertImage(String url, Long id, long width, long height) {
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.insertImage('" + url + "'," + id + ", " + width + "," + height + ");");
    }

    public void insertHtml(String html) {
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.insertHtml('" + html + "');");
    }


    public void insertHtmlDIV(String html) {
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.insertHtmlDIV('" + html + "');");
    }

    public void hideTitle() {
        exec("javascript:RE.hideTitle();");
    }

    public void addImageClickListener(String ids) {
        exec("javascript:RE.addImageClickListener('" + ids + "');");
    }

    public void deleteImageById(Long id) {
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.removeImage(" + id + ");");
    }

    public void insertHr() {
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.insertLine();");
    }


    public void insertLink(String href, String title) {
        if (!href.matches(MarkdownConfig.SCHEME_TAG)) {
            href = MarkdownConfig.SCHEME_HTTP + href;
        }
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.insertLink('" + title + "', '" + href + "');");
    }

    public void changeLink(String href, String title) {
        if (!href.matches(MarkdownConfig.SCHEME_TAG)) {
            href = MarkdownConfig.SCHEME_HTTP + href;
        }
        exec("javascript:RE.saveRange();");
        exec("javascript:RE.changeLink('" + title + "', '" + href + "');");
    }

    public void insertTodo() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setTodo('" + ConvertUtils.getCurrentTime() + "');");
    }

    public void setImageUploadProcess(long id, int process, int imageId) {
        exec("javascript:RE.changeProcess(" + id + ", " + process + ", " + imageId + ");");
    }

    public void setImageFailed(long id) {
        exec("javascript:RE.uploadFailure(" + id + ");");
    }

    public void setImageReload(long id) {
        exec("javascript:RE.uploadReload(" + id + ");");
    }

    public void getNoMarkdownWords() {
        exec("javascript:RE.noMarkdownWords();");
    }

    public void getMarkdownWords() {
        exec("javascript:RE.markdownWords();");
    }

    /**
     * 获取编辑器中内容
     *
     * @param isPublish
     */
    public void getResultWords(boolean isPublish) {
        exec("javascript:RE.resultWords(" + isPublish + ");");
    }

    public void focusEditor() {
        requestFocus();
    }

    public void clearFocusEditor() {
        exec("javascript:RE.blurFocus();");
    }

    protected void exec(final String trigger) {
        if (isReady) {
            load(trigger);
        } else {
            postDelayed(() -> exec(trigger), 100);
        }
    }

    protected void exec(final String trigger, long delay) {
        if (isReady) {
            load(trigger);
        } else {
            postDelayed(() -> exec(trigger), delay);
        }
    }

    private void load(String trigger) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
        } else {
            loadUrl(trigger);
        }
    }

    protected class EditorWebVIewClient2 extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }
    }

    private class EditorWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.d("load", "after onPageFinished");
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            String decode;
            try {
                decode = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // No handling
                return false;
            }

            LogUtils.d("decode", decode);

            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode);
                return true;
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode);
                return true;
            }
            if (TextUtils.indexOf(url, LINK_CHANGE_SCHEME) == 0) {
                linkChangeCallBack(decode);
                return true;
            }

            if (TextUtils.indexOf(url, IMAGE_CLICK_SCHEME) == 0) {
                imageClickCallBack(decode);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    public long getContentLength() {
        return mContentLength;
    }

    private class Android4JsInterface {
        @JavascriptInterface
        public void setViewEnabled(boolean enabled) {
            Observable.just(mOnFocusChangeListener)
                    .filter(listener -> listener != null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listener -> listener.onFocusChange(enabled));
        }

        @JavascriptInterface
        public void setHtmlContent(int titleLenght, int contentLenght) {
            mContents = titleLenght * contentLenght + "";
            Observable.just(mTextChangeListener)
                    .filter(listener -> listener != null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listener -> listener.onTextChange(titleLenght, contentLenght));
        }

        @JavascriptInterface
        public void noMarkdownWords(String noMarkdownWords) {
            // 注意 js 回调 在异步线程中
            if (mOnNoMarkdownWordChangeListener != null) {
                mOnNoMarkdownWordChangeListener.onNoMarkdownWordChange(noMarkdownWords);
            }
            LogUtils.d("noMarkdownWords:::" + noMarkdownWords);
        }

        @JavascriptInterface
        public void markdownWords(String markdown) {
            if (mOnMarkdownWordChangeListener != null) {
                mOnMarkdownWordChangeListener.onMarkdownWordChange(markdown);
            }
            LogUtils.d("markdown:::" + markdown);
        }

        @JavascriptInterface
        public void deleteImage(String tagId) {
            Observable.just(mOnImageDeleteListener)
                    .filter(listener -> listener != null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listener -> listener.onImageDelete(Long.parseLong(tagId)));
        }

        @JavascriptInterface
        public void resultWords(String title, String markdown, String noMarkdownWords, String allHtml, boolean isPublish) {
            Observable.just(mOnMarkdownWordResultListener)
                    .filter(onMarkdownWordResultListener -> onMarkdownWordResultListener != null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listener -> {
                        String result = noMarkdownWords;
                        Matcher matcher = Pattern.compile(MarkdownConfig.LINK_WORDS_FORMAT).matcher(result);
                        while (matcher.find()) {
                            result = result.replaceFirst(MarkdownConfig.LINK_WORDS_FORMAT, matcher.group(3));
                        }

                        if (noMarkdownWords.length() >= 191) {
                            result = noMarkdownWords.substring(0, 191);
                        }
                        String need = RegexUtils.getMarkdownWords(markdown);
                        listener.onMarkdownWordResult(title, need, result, allHtml, isPublish);
                    });
        }

    }
}