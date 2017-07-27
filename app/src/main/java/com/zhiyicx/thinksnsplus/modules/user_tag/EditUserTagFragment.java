package com.zhiyicx.thinksnsplus.modules.user_tag;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class EditUserTagFragment extends TSFragment<EditUserTagContract.Presenter> implements EditUserTagContract.View {

    @BindView(R.id.tv_choosed_tag_tip)
    TextView mTvChoosedTagTip;
    @BindView(R.id.tv_jump_over)
    TextView mTvJumpOver;
    @BindView(R.id.rv_choosed_tag)
    RecyclerView mRvChoosedTag;
    @BindView(R.id.rv_tag_class)
    RecyclerView mRvTagClass;

    public static EditUserTagFragment newInstance() {
        return new EditUserTagFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_edit_user_tag;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.user_tag_choose_tag);
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.save);
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
    }


    private void initListener() {
//
//        // 退出登录
//        RxView.clicks(mBtLoginOut)
//                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
//                .compose(this.<Void>bindToLifecycle())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        initLoginOutPopupWindow();
//                        mLoginoutPopupWindow.show();
//                    }
//                });
    }

}
