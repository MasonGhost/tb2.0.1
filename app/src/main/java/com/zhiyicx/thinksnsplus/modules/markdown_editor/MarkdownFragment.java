package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import com.zhiyi.richtexteditorlib.SimpleRichEditor;
import com.zhiyi.richtexteditorlib.base.RichEditor;
import com.zhiyi.richtexteditorlib.view.BottomMenu;
import com.zhiyi.richtexteditorlib.view.dialogs.LinkDialog;
import com.zhiyi.richtexteditorlib.view.dialogs.PictureHandleDialog;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle.ChooseCircleActivity;
import com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle.ChooseCircleFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;

/**
 * @author Jliuer
 * @Date 2017/11/17/13:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownFragment<Draft extends BaseDraftBean, P extends MarkdownContract.Presenter>
        extends TSFragment<P> implements
        SimpleRichEditor.OnEditorClickListener, PhotoSelectorImpl.IPhotoBackListener,
        MarkdownContract.View<P>, RichEditor.OnMarkdownWordResultListener, RichEditor
        .OnImageDeleteListener,
        BottomMenu.BottomMenuVisibleChangeListener, SimpleRichEditor.BottomMenuItemConfig {

    public static final String BUNDLE_SOURCE_DATA = "sourceId";

    @BindView(R.id.lu_bottom_menu)
    protected BottomMenu mBottomMenu;
    @BindView(R.id.rich_text_view)
    protected SimpleRichEditor mRichTextView;
    @BindView(R.id.ll_circle_container)
    protected LinearLayout mLlCircleContainer;
    @BindView(R.id.line)
    protected View mLine;
    @BindView(R.id.cb_syn_to_dynamic)
    protected CheckBox mCbSynToDynamic;
    @BindView(R.id.tv_name)
    protected TextView mCircleName;

    /**
     * 编辑器加载完成后 再 装载 草稿，不然 js 调不起来
     * 防止重复加载草稿
     */
    private boolean canLoadDraft = true;

    /**
     * 记录上传成功的照片 键值对：时间戳(唯一) <key-value> 图片地址
     */
    protected HashMap<Long, String> mInsertedImages;

    /**
     * 记录上传失败的照片 同上
     */
    protected HashMap<Long, String> mFailedImages;

    /**
     * 上传图片成功后返回的id
     */
    protected List<Integer> mImages;

    protected PhotoSelectorImpl mPhotoSelector;

    /**
     * 图片选择弹出
     */
    protected ActionPopupWindow mPhotoPopupWindow;

    /**
     * t保存草稿提示
     */
    protected ActionPopupWindow mEditWarningPopupWindow;

    /**
     * 标题+文字 长度
     */
    protected int mContentLength;

    protected Draft mDraftBean;

    /**
     * 发布资源之前的处理，比如封装数据
     *
     * @return 数据是否完整
     */
    protected boolean preHandlePublish() {
        return false;
    }

    /**
     * 发布
     *
     * @param title      标题
     * @param markdwon   内容（含有格式）
     * @param noMarkdown 内容（不含格式）
     */
    protected void handlePublish(String title, String markdwon, String noMarkdown, String html) {
    }

    /**
     * 初始化 传递过来的 参数
     */
    protected void initBundleDataWhenOnCreate() {
    }

    /**
     * 圈外发表帖子 选择圈子的回掉
     *
     * @param circleInfo
     */
    protected void onActivityResultForChooseCircle(CircleInfo circleInfo) {

    }

    /**
     * 为草稿箱的图片添加点击事件
     */
    protected void restoreImageData() {
    }

    /**
     * 圈子底部操作栏show or hide
     *
     * @param visible
     */
    @Override
    public void onBottomMenuVisibleChange(boolean visible) {

    }

    /**
     * 在这里初始化 编辑器
     */
    protected void editorPreLoad() {
        mDraftBean = getDraftData();
        if (mDraftBean == null) {
            mRichTextView.load();
        }
    }

    /**
     * 是否开启保存草稿
     *
     * @return
     */
    protected boolean openDraft() {
        return true;
    }

    /**
     * 还原草稿
     *
     * @param draft
     */
    protected void loadDraft(Draft draft) {
    }

    /**
     * 返回 时 触发
     *
     * @param title
     * @param markdwon
     * @param noMarkdown
     * @return 内容是否未空，触发保存草稿的条件之一
     */
    protected boolean contentIsNull(String title, String markdwon, String noMarkdown) {
        return TextUtils.isEmpty(title + noMarkdown);
    }

    /**
     * 草稿内容
     *
     * @return
     */
    protected Draft getDraftData() {
        return null;
    }

    /**
     * 右上角 点击事件
     *
     * @return true：提取 markdown 内容，做发布准备，false：提取整个网页内容
     */
    protected boolean rightClickkNeedMarkdown() {
        return true;
    }

    /**
     * 左上角 点击事件
     *
     * @return true, 提取 markdown 内容，做发布准备
     */
    protected boolean leftClickNeedMarkdown() {
        return false;
    }

    /**
     * 解析 markdown 为 html
     */
    protected void pareseBodyResult() {

    }

    /**
     * 设置内容的默认文字，仅支持问答部分修改，待完善中
     *
     * @return
     */
    protected String setInputInitText() {
        return getString(R.string.circle_post_default_title);
    }


    /**
     * 点击 来自 xxx ，可以跳转到相应圈子
     *
     * @return
     */
    protected boolean canGotoCircle() {
        return true;
    }

    public static MarkdownFragment newInstance(Bundle bundle) {
        MarkdownFragment markdownFragment = new MarkdownFragment();
        markdownFragment.setArguments(bundle);
        return markdownFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initBundleDataWhenOnCreate();
        }
    }

    /**
     * 控制是否显示 设置 item（底部菜单）
     *
     * @return
     */
    @Override
    public boolean needSetting() {
        return false;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.publish);
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        if (!preHandlePublish()) {
            return;
        }
        mRichTextView.getResultWords(rightClickkNeedMarkdown());
    }

    /**
     * @param title      标题
     * @param markdwon   markdown 格式内容
     * @param noMarkdown 纯文字内容
     * @param isPublish  是否是发送
     * @param html       全部 html 格式内容
     */
    @Override
    public void onMarkdownWordResult(String title, String markdwon, String noMarkdown, String
            html, boolean isPublish) {
        LogUtils.d("onMarkdownWordResult:::" + "\n" + "markdwon::" + markdwon
                + "\n" + "noMarkdown::" + noMarkdown);
        if (isPublish) {
            List<Integer> result = RegexUtils.getImageIdsFromMarkDown(MarkdownConfig
                    .IMAGE_FORMAT, markdwon);
            if (mImages.containsAll(result)) {
                mImages.clear();
                mImages.addAll(result);
            }
            handlePublish(title, markdwon, noMarkdown, html);
        } else {
            boolean canSaveDraft = !contentIsNull(title, markdwon, noMarkdown);
            if (!canSaveDraft || !openDraft()) {
                mActivity.finish();
                return;
            }
            initEditWarningPop(title, markdwon, noMarkdown, html);
            DeviceUtils.hideSoftKeyboard(mActivity.getApplication(), mRichTextView);
        }
    }

    @Override
    public void onImageDelete(long tagId) {
        mInsertedImages.remove(tagId);
        mFailedImages.remove(tagId);
    }

    protected String getImageIds() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Long, String> entry : mInsertedImages.entrySet()) {
            stringBuilder.append(String.valueOf(entry.getKey()));
            stringBuilder.append(",");
        }
        for (Map.Entry<Long, String> entry : mFailedImages.entrySet()) {
            stringBuilder.append(String.valueOf(entry.getKey()));
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void initView(View rootView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
        mToolbarRight.setEnabled(false);
        mInsertedImages = new HashMap<>();
        mFailedImages = new HashMap<>();
        mImages = new ArrayList<>();

        initListener();
    }

    @Override
    protected void initData() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();

        editorPreLoad();
    }

    @Override
    public void onBackPressed() {
        mRichTextView.getResultWords(leftClickNeedMarkdown());
    }


    protected void initListener() {
        mRichTextView.setBottomMenuItemConfig(this);
        mRichTextView.setOnEditorClickListener(this);
        mRichTextView.setOnImageDeleteListener(this);
        mRichTextView.setOnTextLengthChangeListener(length -> {

        });
        mRichTextView.setBottomMenuItemConfig(this);
        mRichTextView.setOnMarkdownWordResultListener(this);
        mRichTextView.setBottomMenu(mBottomMenu);

        mBottomMenu.setBottomMenuVisibleChangeListener(this);

        mLlCircleContainer.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, ChooseCircleActivity.class);
            mActivity.startActivityForResult(intent, ChooseCircleFragment.CHOOSE_CIRCLE);
        });
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.publish_post);
    }

    @Override
    public void onLinkButtonClick() {
        showLinkDialog(LinkDialog.createLinkDialog(), false);
    }

    @Override
    public void onInsertImageButtonClick() {
        DeviceUtils.hideSoftKeyboard(mActivity.getApplication(), mRichTextView);
        initPhotoPopupWindow();
    }

    @Override
    public void onSettingImageButtionClick(boolean isSelected) {

    }

    @Override
    public void onLinkClick(String name, String url) {
        showLinkDialog(LinkDialog.createLinkDialog(name, url), true);
    }

    @Override
    public void onImageClick(Long id) {
        if (mInsertedImages.containsKey(id)) {
//            showPictureClickDialog(PictureHandleDialog.createDeleteDialog(id), new
//                    CharSequence[]{getString(R.string.delete)});
        } else if (mFailedImages.containsKey(id)) {
            showPictureClickDialog(PictureHandleDialog.createDeleteDialog(id),
                    new CharSequence[]{getString(R.string.delete), getString(R.string.retry)});
        }
    }

    @Override
    public void onTextStypeClick(boolean isSelect) {
        setSynToDynamicCbVisiable(!isSelect);
    }

    /**
     * 编辑器加载完成
     *
     * @param ready
     */
    @Override
    public void onAfterInitialLoad(boolean ready) {
        if (mDraftBean != null && ready && canLoadDraft) {
            loadDraft(mDraftBean);
            restoreImageData();
            canLoadDraft = false;
        }
    }

    @Override
    public void onInputListener(int titleLength, int contentLength) {
        mContentLength = titleLength * contentLength;
        setRightClickable(mContentLength > 0);
    }

    protected void setRightClickable(boolean clickable) {
        mToolbarRight.setEnabled(clickable);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_markd_down;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooseCircleFragment.CHOOSE_CIRCLE) {
            if (data != null && data.getExtras() != null && data.getExtras().getParcelable
                    (ChooseCircleFragment.BUNDLE_CIRCLE) != null) {
                onActivityResultForChooseCircle(data.getExtras().getParcelable
                        (ChooseCircleFragment.BUNDLE_CIRCLE));
            }
        } else {
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList.isEmpty()) {
            return;
        }
        String path = photoList.get(0).getImgUrl();
        long id = SystemClock.currentThreadTimeMillis();
        long size[] = ImageUtils.getBitmapSize(path);
        mRichTextView.insertImage(path, id, size[0], size[1]);
        mInsertedImages.put(id, path);
        mPresenter.uploadPic(path, id);
    }

    @Override
    public void onUploading(long id, String filePath, int progress, int imgeId) {
        getActivity().runOnUiThread(() -> {
            if (progress == 100) {
                mImages.add(imgeId);
            }
            mRichTextView.setImageUploadProcess(id, progress, imgeId);
        });
    }

    @Override
    public void onFailed(String filePath, long id) {
        getActivity().runOnUiThread(() -> {
            mRichTextView.setImageFailed(id);
            mInsertedImages.remove(id);
            mFailedImages.put(id, filePath);
        });
    }

    /**
     * @param isVisiable true  显示
     */
    protected void setSynToDynamicCbVisiable(boolean isVisiable) {

    }

    /**
     * 初始化图片选择弹框
     */
    protected void initPhotoPopupWindow() {

        if (mPhotoPopupWindow != null) {
            mPhotoPopupWindow.show();
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.choose_from_photo))
                .item2Str(getString(R.string.choose_from_camera))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 选择相册，单张
                    mPhotoSelector.getPhotoListFromSelector(1, null);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
        mPhotoPopupWindow.show();
    }

    protected void initEditWarningPop(String title, String markdown, String noMarkdown, String
            html) {
        if (mEditWarningPopupWindow != null) {
            mEditWarningPopupWindow.show();
            return;
        }
        mEditWarningPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.edit_quit))
                .item2Str(getString(canSaveDraft() ? R.string.save_to_draft_box : R.string.empty))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    cancleEdit();
                    mEditWarningPopupWindow.hide();
                    getActivity().finish();
                })
                .item2ClickListener(() -> {
                    saveDraft(title, html, noMarkdown);
                    mEditWarningPopupWindow.hide();
                    getActivity().finish();
                })
                .bottomClickListener(() -> mEditWarningPopupWindow.hide())
                .build();
        mEditWarningPopupWindow.show();
    }

    protected boolean canSaveDraft() {
        return true;
    }

    protected void saveDraft(String title, String html, String noMarkdown) {
    }

    protected void cancleEdit() {

    }

    protected void showLinkDialog(final LinkDialog dialog, final boolean isChange) {
        dialog.setListener(new LinkDialog.OnDialogClickListener() {
            @Override
            public void onConfirmButtonClick(String name, String url) {
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url)) {
                    ToastUtils.showToast(R.string.not_empty);
                } else {
                    if (!isChange) {
                        mRichTextView.insertLink(url, name);
                    } else {
                        mRichTextView.changeLink(url, name);
                    }
                    onCancelButtonClick();
                }
            }

            @Override
            public void onCancelButtonClick() {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), LinkDialog.Tag);
    }

    protected void showPictureClickDialog(final PictureHandleDialog dialog, CharSequence[] items) {

        dialog.setListener(new PictureHandleDialog.OnDialogClickListener() {
            @Override
            public void onDeleteButtonClick(Long id) {
                mRichTextView.deleteImageById(id);
                removeFromLocalCache(id);
            }

            @Override
            public void onReloadButtonClick(Long id) {
                mRichTextView.setImageReload(id);
                mPresenter.uploadPic(mFailedImages.get(id), id);
                mInsertedImages.put(id, mFailedImages.get(id));
                mFailedImages.remove(id);
            }
        });
        dialog.setItems(items);
        dialog.show(getFragmentManager(), PictureHandleDialog.Tag);
    }

    protected void removeFromLocalCache(long id) {
        if (mInsertedImages.containsKey(id)) {
            mInsertedImages.remove(id);
        } else if (mFailedImages.containsKey(id)) {
            mFailedImages.remove(id);
        }
    }

    /**
     * 这个还原的 顺序不能变
     *
     * @param body
     * @return
     */
    protected String pareseBody(String body) {

        // 还原 <img ...>
        String result;
        String imageReplace = "-星星-tym-星星-";
        Matcher imageMatcher = Pattern.compile(MarkdownConfig.IMAGE_FORMAT_HTML).matcher(body);
        result = body.replaceAll(MarkdownConfig.IMAGE_FORMAT, imageReplace);
        while (imageMatcher.find()) {
            String name = imageMatcher.group(2);
            int id = Integer.parseInt(imageMatcher.group(3));
            mImages.add(id);
            long tagId = SystemClock.currentThreadTimeMillis();
            String imagePath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
            mInsertedImages.put(tagId, imagePath);
            result = result.replaceFirst(imageReplace, getImageHtml(tagId, id, name, imagePath));
        }

        // 还原 <a ...></a>
        String linkReplace = "-星星-link-星星-";
        Matcher linkMatcher = Pattern.compile(MarkdownConfig.LINK_FORMAT).matcher(result);
        result = result.replaceAll(MarkdownConfig.LINK_FORMAT, linkReplace);
        while (linkMatcher.find()) {
            result = result.replaceFirst(linkReplace, getLinkHtml(linkMatcher.group(2),
                    linkMatcher.group(1)));
        }

        // 兼容就连接  http://www.baidu.com
        Matcher oldLinkMatcher = Pattern.compile(MarkdownConfig.NETSITE_A_FORMAT).matcher(body);
        while (oldLinkMatcher.find()) {
            String html =
                    "<a href=\" " + oldLinkMatcher.group(0) + " \" class=\"editor-link\">网页链接</a>";
            body = body.replaceFirst(oldLinkMatcher.group(0), html);
        }

        // markdown to html
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(result);
        result = renderer.render(document);
        pareseBodyResult();
        return result;
    }

    protected String getImageHtml(long tagId, int id, String name, String imagePath) {
        String markdown = " @![" + name + "](" + id + ")";
        return "<div><br></div>" +
                "<div class=\"block\" contenteditable=\"false\">" +
                "   <div class=\"img-block\">" +
                "       <div style=\"width: 100% \" class=\"process\">" +
                "           <div class=\"fill\"></div>" +
                "       </div>" +
                "       <img class=\"images\" data-id=\"" + tagId + "\" style=\"width: 100% ; " +
                "height: auto\"" +
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

    protected String getLinkHtml(String url, String name) {
        return "<a href=\"" + url + "\" class=\"editor-link\">" + name + "</a>";
    }

    protected String getHtml(String title, String content) {
        onInputListener(title.length(), content.length());
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
                "    <div class=\"content\" contenteditable=\"false\" id=\"content\">\n" +
                "        <header>\n" +
                "            <div class=\"title\" title-placeholder=\"请输入标题\" id=\"title\" " +
                "contenteditable=\"true\">" + title + "</div>\n" +
                "            <span id=\"stay\" style=\"display: none;text-align:right\"><span " +
                "id=\"txtCount\"></span>/20</span>\n" +
                "        </header>\n" +
                "        <div class=\"line\"></div>\n" +
                "        <div id=\"editor\" contenteditable=\"true\" editor-placeholder=\"" +
                setInputInitText() + "\">" + content + "</div>\n" +
                "    </div>\n" +
                "    <script src=\"./richeditor.js\" id=\"script\"></script>\n" +
                "</body>\n" +
                "</html>";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mPhotoPopupWindow);
        dismissPop(mEditWarningPopupWindow);
    }
}
