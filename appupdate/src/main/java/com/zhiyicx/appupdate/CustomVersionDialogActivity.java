package com.zhiyicx.appupdate;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.callback.APKDownloadListener;
import com.allenliu.versionchecklib.callback.CommitClickListener;
import com.allenliu.versionchecklib.callback.DialogDismissListener;
import com.allenliu.versionchecklib.core.VersionDialogActivity;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.SharePreferenceUtils;

import java.io.File;
import java.util.ArrayList;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

/**
 * 注意为了展示本库的所有功能
 * 所以代码看上去会比较多，不过都是重写方法和监听回调
 * 如果不想自定义界面和一些自定义功能不用设置
 * versionParams.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
 * 使用库默认自带的就行了
 *
 * @important 如果要重写几个ui:
 * ，请分别使用父类的versionDialog／loadingDialog/failDialog以便库管理显示和消失
 */
public class CustomVersionDialogActivity extends VersionDialogActivity implements CommitClickListener, DialogDismissListener, APKDownloadListener {
    public static final String BUNDLE_VERSIONDATA = "versionData";
    public static final String SHAREPREFERENCE_TAG_ABORD_VERION = "abord_vertion";

    public static int customVersionDialogIndex = 3;
    public static boolean isForceUpdate = false;
    public static boolean isCustomDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里是几个回调
        setApkDownloadListener(this);
        setCommitClickListener(this);
        setDialogDimissListener(this);
    }

    /**
     * 下载文件成功也关闭app
     * 也判断是否强制更新
     *
     * @param file
     */
    @Override
    public void onDownloadSuccess(File file) {
        forceCloseApp();
        Log.e("CustomVersionDialogActi", "文件下载成功回调");
    }

    @Override
    public void onDownloadFail() {

    }

    @Override
    public void onDownloading(int progress) {

        Log.e("CustomVersionDialogActi", "正在下载中回调...");
    }

    @Override
    public void onCommitClick() {
        Log.e("CustomVersionDialogActi", "确认按钮点击回调");
    }


    /**
     * 自定义更新展示界面 直接重写此方法就好
     */
    @Override
    public void showVersionDialog() {
        //使用默认的提示框直接调用父类的方法,如果需要自定义的对话框，那么直接重写此方法
        // super.showVersionDialog();
        if (customVersionDialogIndex == 1 || customVersionDialogIndex == 2) {
            customVersionDialogTwo();
        } else {
            super.showVersionDialog();
        }
    }


    /**
     * 自定义dialog two
     */
    private void customVersionDialogTwo() {
        final Bundle bundle = getVersionParamBundle();
        final AppVersionBean appVersionBean = bundle.getParcelable(BUNDLE_VERSIONDATA);
        versionDialog = new BaseDialog(this, R.style.BaseDialog, R.layout.custom_dialog_two_layout);
        TextView tvTitle = (TextView) versionDialog.findViewById(R.id.tv_title);
        final MarkdownView mdMsg = (MarkdownView) versionDialog.findViewById(R.id.md_msg);

        TextView tvAbord = (TextView) versionDialog.findViewById(R.id.tv_abord);
        TextView tvInstall = (TextView) versionDialog.findViewById(R.id.tv_install);

        versionDialog.show();
        //设置dismiss listener 用于强制更新,dimiss会回调dialogDismiss方法
        versionDialog.setOnDismissListener(this);
        //可以使用之前从service传过来的一些参数比如：title。msg，downloadurl，parambundle
        tvTitle.setText(getVersionTitle());
        InternalStyleSheet css = new Github();
        css.addRule(".container", "padding-right:0", ";padding-left:0", "text-align:justify","text-align-last:left", "letter-spacing: 0.3px");
        css.addRule("body", "line-height: 1.59", "padding: 0px", "font-size: 17px", "color: #333333");
        css.addRule("h1", "color: #333333", "size: 25px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("h2", "color: #333333", "size: 23px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("h3", "color: #333333", "size: 21px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("h4", "color: #333333", "size: 19px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("img", "margin-top: 20px", "margin-bottom: 20px","align:center", "margin: 0 auto","max-width: 100%", "display: block");
        /*设置 a 标签文字颜色，不知道为什么，要这样混合才能有效*/
        css.addMedia("color: #59b6d7; a:link {color: #59b6d7}");
        css.endMedia();
        css.addRule("a", "font-weight: bold");
        mdMsg.addStyleSheet(css);
        mdMsg.loadMarkdown(appVersionBean.getDescription());
        WebViewClient mWebViewClient = new WebViewClient() {
            /**
             * 多页面在同一个 WebView 中打开，就是不新建 activity 或者调用系统浏览器打开
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /**
             * 网页开始加载
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            /**
             *   网页加载结束
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mdMsg.scrollTo(0, 0);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                System.out.println("errorCode = " + errorCode);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        };
        mdMsg.setWebViewClient(mWebViewClient);

        //可以使用之前service传过来的值
        tvInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                versionDialog.dismiss();
                CustomVersionDialogActivity.super.dealAPK();

            }
        });
        if (!isForceUpdate) {
            tvAbord.setVisibility(View.VISIBLE);
            tvAbord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    versionDialog.dismiss();
                    ArrayList<Integer> abordVersions = SharePreferenceUtils.getObject(getApplicationContext(), CustomVersionDialogActivity.SHAREPREFERENCE_TAG_ABORD_VERION);
                    if (abordVersions == null) {
                        abordVersions = new ArrayList<>();
                    }
                    abordVersions.add(appVersionBean.getVersion_code());
                    SharePreferenceUtils.saveObject(getApplicationContext(), CustomVersionDialogActivity.SHAREPREFERENCE_TAG_ABORD_VERION, abordVersions);

                }
            });
        }
        versionDialog.setCanceledOnTouchOutside(!isForceUpdate);
        versionDialog.setCancelable(!isForceUpdate);//设置这个对话框不能被用户按[返回键]而取消掉,但测试发现如果用户按了KeyEvent.KEYCODE_SEARCH,对话框还是会Dismiss掉
        //由于设置alertDialog.setCancelable(false); 发现如果用户按了KeyEvent.KEYCODE_SEARCH,对话框还是会Dismiss掉,这里的setOnKeyListener作用就是屏蔽用户按下KeyEvent.KEYCODE_SEARCH
        if (isForceUpdate) {
            versionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false; //默认返回 false
                    }
                }
            });
        }
        versionDialog.show();
    }

    /**
     * 自定义下载失败重试对话框
     * 使用父类的failDialog
     */
    @Override
    public void showFailDialog() {
        super.showFailDialog();
        Toast.makeText(this, "下载失败，重新尝试", Toast.LENGTH_SHORT).show();
    }


    View loadingView;

    /**
     * 要更改下载中界面 只需要重写此方法即可
     * 因为下载的时候会不断回调此方法
     * dialog使用全局 只初始化一次
     * 使用父类的loadingDialog保证下载成功会dimiss掉dialog
     *
     * @param currentProgress
     */
    @Override
    public void showLoadingDialog(int currentProgress) {
        if (!isCustomDownloading) {
            super.showLoadingDialog(currentProgress);
        } else {
            //使用父类的loadingDialog保证下载成功会dimiss掉dialog
            if (loadingDialog == null) {
                loadingView = LayoutInflater.from(this).inflate(R.layout.custom_download_layout, null);
                loadingDialog = new AlertDialog.Builder(this).setTitle("").setView(loadingView).create();
                loadingDialog.setCancelable(false);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
            }
            ProgressBar pb = (ProgressBar) loadingView.findViewById(com.allenliu.versionchecklib.R.id.pb);
            TextView tvProgress = (TextView) loadingView.findViewById(com.allenliu.versionchecklib.R.id.tv_progress);
            tvProgress.setText(String.format(getString(com.allenliu.versionchecklib.R.string.versionchecklib_progress), currentProgress));
            pb.setProgress(currentProgress);
            loadingDialog.show();
        }
//        Toast.makeText(this, "显示自定义的下载加载框", Toast.LENGTH_SHORT).show();
    }


    /**
     * versiondialog dismiss 的时候会回调此方法
     * 这里面可以进行强制更新操作
     * <p>
     * 建议用一个ActivityManger记录每个Activity出入堆栈
     * 最后全部关闭activity 实现app exit
     * ActivityTaskManger.finishAllActivity();
     *
     * @param dialog
     */
    @Override
    public void dialogDismiss(DialogInterface dialog) {
        forceCloseApp();
    }

    /**
     * 在dialogDismiss和onDownloadSuccess里面强制更新
     * 分别表示两种情况：
     * 一种用户取消下载  关闭app
     * 一种下载成功安装的时候 应该也关闭app
     */
    private void forceCloseApp() {
        if (isForceUpdate) {
            //我这里为了简便直接finish 就行了
            ActivityHandler.getInstance().finishAllActivity();
        }
    }

}
