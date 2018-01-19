package com.zhiyicx.thinksnsplus.modules.information.publish.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownFragment;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;

/**
 * @Author Jliuer
 * @Date 2018/01/18/9:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishInfoFragmentV2 extends MarkdownFragment<PostDraftBean> {

    public static final String INFO_REFUSE = "info_refuse";
    public static InfoPublishBean mInfoPublishBean;

    /**
     * 提示信息弹窗
     */
    private ActionPopupWindow mInstructionsPopupWindow;

    private boolean isRefuse;

    public static PublishInfoFragmentV2 getInstance(Bundle bundle) {
        PublishInfoFragmentV2 publishInfoFragmentV2 = new PublishInfoFragmentV2();
        publishInfoFragmentV2.setArguments(bundle);
        return publishInfoFragmentV2;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.edit_info);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.next);
    }

    /**
     * 取消提示选择弹框
     */
    private ActionPopupWindow mCanclePopupWindow;

    @Override
    protected void initBundleDataWhenOnCreate() {
        super.initBundleDataWhenOnCreate();
        if (getArguments() != null) {
            mInfoPublishBean = getArguments().getParcelable(INFO_REFUSE);
        }
    }

    @Override
    protected boolean preHandlePublish() {
        if (mInfoPublishBean == null) {
            mInfoPublishBean = new InfoPublishBean();
        }
        return true;
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown) {
        super.handlePublish(title, markdwon, noMarkdown);
        mInfoPublishBean.setContent(markdwon);
        mInfoPublishBean.setAmout(mPresenter.getSystemConfigBean().getNewsPayContribute());

        // 封面
        int cover;
        if (isRefuse) {
            cover = mInfoPublishBean.getCover();
        } else {
            cover = RegexUtils.getImageId(markdwon);
        }
        mInfoPublishBean.setCover(cover);
        mInfoPublishBean.setImage(cover < 0 ? null : (long) cover);

        mInfoPublishBean.setTitle(title);
        Intent intent = new Intent(getActivity(), AddInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initEditWarningPop(String title, String html, String noMarkdown) {
        if (mCanclePopupWindow != null) {
            mCanclePopupWindow.show();
            return;
        }
        mCanclePopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_send_cancel_hint))
                .item2Str(getString(R.string.determine))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mInfoPublishBean = null;
                    mCanclePopupWindow.hide();
                    mActivity.finish();
                })
                .bottomClickListener(() -> mCanclePopupWindow.hide()).build();
        mCanclePopupWindow.show();
    }

    @Override
    protected void initPhotoPopupWindow() {
        if (mInsertedImages.size() + mFailedImages.size() >= 9) {
            initInstructionsPop(getString(R.string.instructions), String.format(Locale.getDefault
                    (), getString(R.string.choose_max_photos), 9));
            return;
        }
        super.initPhotoPopupWindow();
    }

    protected void initInstructionsPop(String title, String des) {
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow.newBuilder().item1Str(title).desStr(des);
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(title)
                .desStr(des)
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> {
                    mInstructionsPopupWindow.hide();
                })
                .build();
        mInstructionsPopupWindow.show();
    }

    @Override
    protected PostDraftBean getDraftData() {
        mDraftBean = new PostDraftBean();
        if (mInfoPublishBean != null) {
            mDraftBean.setTitle(mInfoPublishBean.getTitle());
            mDraftBean.setHtml(getHtml(mDraftBean.getTitle(), pareseBody(mInfoPublishBean.getContent())));
        }
        return mInfoPublishBean == null ? null : mDraftBean;
    }

    @Override
    protected void loadDraft(PostDraftBean postDraftBean) {
        super.loadDraft(postDraftBean);
        mRichTextView.loadDraft("", mDraftBean.getHtml());
    }

    private String pareseBody(String body) {
        String result;
        String reg = "@!(\\[(.*?)])\\(((\\d+))\\)";
        String replace = "-星星-tym-星星-";
        Matcher matcher = Pattern.compile(reg).matcher(body);
        result = body.replaceAll(MarkdownConfig.IMAGE_FORMAT, replace);
        while (matcher.find()) {
            String name = matcher.group(2);
            int id = Integer.parseInt(matcher.group(3));
            mImages.add(id);
            long tagId = SystemClock.currentThreadTimeMillis();
            String imagePath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
            mInsertedImages.put(tagId, imagePath);
            result = result.replaceFirst(replace, getImageHtml(tagId, id, name, imagePath));
        }
        mDraftBean.setFailedImages(mFailedImages);
        mDraftBean.setInsertedImages(mInsertedImages);
        mDraftBean.setImages(mImages);
        return result;
    }

    private String getImageHtml(long tagId, int id, String name, String imagePath) {
        String markdown = "@![" + name + "](" + id + ")";
        return "<div><br></div>" +
                "<div class=\"block\" contenteditable=\"false\">" +
                "   <div class=\"img-block\">" +
                "       <div style=\"width: 100% \" class=\"process\">" +
                "           <div class=\"fill\"></div>" +
                "       </div>" +
                "       <img class=\"images\" data-id=\"" + tagId + "\" style=\"width: 100% ; height: auto\"" +
                "           src=\"" + imagePath + "\">" +
                "       <div class=\"cover\" style=\"width: 100% ; height: auto\"></div>" +
                "       <div class=\"delete\">" +
                "           <img class=\"error\" src=\"./reload.png\">" +
                "           <div class=\"tips\">图片上传失败，请点击重试</div>" +
                "           <div class=\"markdown\">" + markdown + "</div>" +
                "       </div>" +
                "   </div>" +
                "   <input class=\"dec\" type=\"text\" placeholder=\"请输入图片名字\">" +
                "</div>" +
                "<div><br></div>";

    }

    private String getHtml(String title, String content) {
        onInputListener(title.length() + content.length());
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Zhiyicx</title>\n" +
                "    <link rel=\"stylesheet\" href=\"./index.css\">\n" +
                "</head>\n" +
                "<body contenteditable=\"false\">\n" +
                "    <div class=\"content\" contenteditable=\"false\">\n" +
                "        <header>\n" +
                "            <div class=\"title\" title-placeholder=\"请输入标题\" id=\"title\" contenteditable=\"true\">" + title + "</div>\n" +
                "            <span id=\"stay\" style=\"display: none;text-align:right\"><span id=\"txtCount\"></span>/20</span>\n" +
                "        </header>\n" +
                "        <div class=\"line\"></div>\n" +
                "        <div id=\"editor\" contenteditable=\"true\" editor-placeholder=\"请输入正文\">" + content + "</div>\n" +
                "    </div>\n" +
                "    <script src=\"./richeditor.js\" id=\"script\"></script>\n" +
                "</body>\n" +
                "</html>";
    }
}
