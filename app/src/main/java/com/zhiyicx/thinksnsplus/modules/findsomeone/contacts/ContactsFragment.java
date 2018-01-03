package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.recycleview.stickygridheaders.StickyHeaderGridLayoutManager;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ContactsBean;
import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_LIST_ITEM_SPACING;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.contacts.ContactsAdapter.DEFAULT_MAX_ADD_SHOW_NUMS;

/**
 * @Describe 通讯录
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class ContactsFragment extends TSFragment<ContactsContract.Presenter> implements ContactsContract.View, ContactsAdapter.OnMoreClickLitener {
    private static final String BUNDLE_DATA = "data";
    private static final String BUNDLE_TITLE = "title";

    private static final int SPAN_SIZE = 1;

    @BindView(R.id.rv_contacts)
    RecyclerView mRvTagClass;

    private StickyHeaderGridLayoutManager mTagClassLayoutManager;

    private ArrayList<ContactsContainerBean> mListData = new ArrayList<>();

    private ContactsAdapter mTagClassAdapter;

    private ArrayList<ContactsContainerBean> mBundleData;

    private String mTitle;


    /**
     * 通讯录
     */
    public static void startToEditTagActivity(Context context, String title, ArrayList<ContactsContainerBean> listData) {

        Intent intent = new Intent(context, ContactsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        bundle.putSerializable(BUNDLE_DATA, listData);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 100);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }


    public static ContactsFragment newInstance(Bundle bundle) {
        ContactsFragment editUserTagFragment = new ContactsFragment();
        editUserTagFragment.setArguments(bundle);
        return editUserTagFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_contacts;
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
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBundleData = (ArrayList<ContactsContainerBean>) getArguments().getSerializable(BUNDLE_DATA);
            mTitle = getArguments().getString(BUNDLE_TITLE);
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = getString(R.string.contacts);
        }
    }

    @Override
    protected void initView(View rootView) {
        mToolbarCenter.setVisibility(View.VISIBLE);
        mToolbarCenter.setText(mTitle);
        updateChooseTip();
        initRvTagClass();
        initListener();


    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        closeLoadingView();
    }

    private void updateChooseTip() {
    }


    @Override
    protected void initData() {
        if (mBundleData == null) {
            mPresenter.getContacts();
        } else {
            mListData.addAll(mBundleData);
            mTagClassAdapter.notifyAllSectionsDataSetChanged();
            hideLoading();
        }
    }


    private void initRvTagClass() {
        mTagClassLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
//        mTagClassLayoutManager.setHeaderBottomOverlapMargin(getResources().getDimensionPixelSize(R.dimen.spacing_small));
        mTagClassLayoutManager.setSpanSizeLookup(new StickyHeaderGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int section, int position) {
                return 1;
            }
        });

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(
        mRvTagClass.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        mRvTagClass.setLayoutManager(mTagClassLayoutManager);
        mRvTagClass.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), DEFAULT_LIST_ITEM_SPACING), 0, 0));
        mTagClassAdapter = new ContactsAdapter(mListData, mPresenter);
        mTagClassAdapter.setOnMoreClickLitener(this);
        mRvTagClass.setAdapter(mTagClassAdapter);

    }

    private void initListener() {


    }


    @Override
    public void onMoreClick(int categoryPosition) {
        ArrayList<ContactsContainerBean> data = new ArrayList<>();
        ContactsContainerBean contactsContainerBean = mListData.get(categoryPosition);
        for (ContactsContainerBean containerBean : mBundleData) {
            if (containerBean.getTitle().equals(contactsContainerBean.getTitle())) {
                data.add(containerBean);
                break;
            }
        }
        startToEditTagActivity(getActivity(), mListData.get(categoryPosition).getTitle(), data);
    }

    @Override
    public void updateContacts(ArrayList<ContactsContainerBean> data) {

        mBundleData = new ArrayList<>();
        if (data.size() > 0) {
            ContactsContainerBean contactsContainerBean = new ContactsContainerBean();
            contactsContainerBean.setTitle(data.get(0).getTitle());
            contactsContainerBean.setContacts(new ArrayList<>());
            contactsContainerBean.getContacts().addAll(data.get(0).getContacts());
            mBundleData.add(contactsContainerBean);
        }
        if (data.size() > 1) {
            ContactsContainerBean contactsContainerBean2 = new ContactsContainerBean();
            contactsContainerBean2.setTitle(data.get(1).getTitle());
            contactsContainerBean2.setContacts(new ArrayList<>());
            contactsContainerBean2.getContacts().addAll(data.get(1).getContacts());
            mBundleData.add(contactsContainerBean2);
        }
        mListData.clear();
        mListData.addAll(data);
        for (int i = 0; i < mListData.size(); i++) {
            if (mListData.get(i).getContacts().size() > DEFAULT_MAX_ADD_SHOW_NUMS) {
                mListData.get(i).setContacts(new ArrayList<>(mListData.get(i).getContacts().subList(0, ContactsAdapter.DEFAULT_MAX_ADD_SHOW_NUMS)));
            }
        }
        mTagClassAdapter.notifyAllSectionsDataSetChanged();
    }
}
