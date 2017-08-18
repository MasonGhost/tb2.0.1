package com.zhiyicx.thinksnsplus.modules.q_a.answer;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.QAAnswerBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.PublishContentFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.xrichtext.RichTextEditor;

import java.util.List;
import java.util.Locale;

/**
 * @Author Jliuer
 * @Date 2017/08/15/16:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishAnswerFragment extends PublishContentFragment {

    public static final String BUNDLE_SOURCE_ID = "source_id";
    public static final String BUNDLE_SOURCE_BODY = "source_body";
    private String mBody;

    public static PublishAnswerFragment newInstance(Bundle bundle) {
        if (bundle == null || bundle.getLong(BUNDLE_SOURCE_ID) <= 0) {
            throw new IllegalArgumentException("questin_id can not be null");
        }
        PublishAnswerFragment publishContentFragment = new PublishAnswerFragment();
        publishContentFragment.setArguments(bundle);
        return publishContentFragment;
    }

    @Override
    protected void initData() {
        super.initData();
        mBody = getArguments().getString(BUNDLE_SOURCE_BODY, "");
        mToolbarCenter.setText(getString(mBody.isEmpty() ? R.string.qa_update_answer : R.string.qa_publish_answer));
        if (!mBody.isEmpty()) {
            mRicheTest.clearAllLayout();
            mPresenter.pareseBody(mBody);
        }
    }

    @Override
    public void addImageViewAtIndex(String iamge, int iamge_id, String markdonw, boolean isLast) {
        mImageIdArray[mPicTag] = iamge_id;
        mPicTag++;
        mRicheTest.updateImageViewAtIndex(mRicheTest.getLastIndex(), iamge, markdonw, isLast);
    }

    @Override
    public void addEditTextAtIndex(String text) {
        mRicheTest.updateEditTextAtIndex(mRicheTest.getLastIndex(), text);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_publish_answer);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_btn);
    }

    @Override
    protected void setRightClick() {
        if (mBody.isEmpty()) {
            mPresenter.publishAnswer(getArguments().getLong(BUNDLE_SOURCE_ID), getContentString(), mAnonymity);
        } else {
            mPresenter.updateAnswer(getArguments().getLong(BUNDLE_SOURCE_ID), getContentString(), mAnonymity);
        }

    }

    @NonNull
    @Override
    protected String getContentString() {
        StringBuilder builder = new StringBuilder();
        List<RichTextEditor.EditData> datas = mRicheTest.buildEditData();
        for (RichTextEditor.EditData editData : datas) {
            builder.append(editData.inputStr);
            if (!editData.imagePath.isEmpty()) {
                if (editData.imagePath.contains("![image]")) {
                    builder.append(editData.imagePath);
                } else {
                    builder.append(String.format(Locale.getDefault(),
                            MarkdownConfig.IMAGE_TAG, MarkdownConfig.IMAGE_TITLE, mImageIdArray[mPicAddTag]));
                }
                mPicAddTag++;
            }
        }
        return builder.toString();
    }

    @Override
    public void publishSuccess(QAAnswerBean answerBean) {
        getActivity().finish();
    }

    @Override
    public void updateSuccess() {
        super.updateSuccess();
        getActivity().finish();
    }
}
