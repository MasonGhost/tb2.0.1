package com.zhiyicx.thinksnsplus.modules.circle.search.onlypost;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.circle.search.SearchCirclePostFragment;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class SearchOnlyCirclePostFragment extends SearchCirclePostFragment {
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_search_cancle)
    View mFragmentSearchCancle;

    public static SearchOnlyCirclePostFragment newInstance(BaseCircleRepository.CircleMinePostType circleMinePostType, long circleGroupId) {
        SearchOnlyCirclePostFragment circleDetailFragment = new SearchOnlyCirclePostFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CIRCLE_TYPE, circleMinePostType);
        bundle.putLong(CIRCLE_ID, circleGroupId);
        circleDetailFragment.setArguments(bundle);
        return circleDetailFragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mFragmentInfoSearchEdittext.setHint(getString(R.string.info_search));
        RxTextView.editorActionEvents(mFragmentInfoSearchEdittext).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                if (!TextUtils.isEmpty(mFragmentInfoSearchEdittext.getText().toString())) {
                    onEditChanged(mFragmentInfoSearchEdittext.getText().toString());
                    DeviceUtils.hideSoftKeyboard(getContext(), mFragmentInfoSearchEdittext);
                }
            }
        });
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected boolean needMusicWindowView() {
        return true;
    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mFragmentSearchCancle;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_circle_search_only_post_list;
    }

    @OnClick({R.id.fragment_search_cancle, R.id.bt_do})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_cancle:
                getActivity().finish();
                break;
            case R.id.bt_do:
                // 创建圈子帖子
                BaseMarkdownActivity.startActivityForPublishPostOutCircle(mActivity);
                break;
            default:
        }
    }

}
