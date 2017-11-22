package com.zhiyicx.zhibolibrary.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.adapter.AdapterViewPager;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.components.AutoAppBarLayout;
import com.zhiyicx.zhibolibrary.ui.fragment.ZBLSearchTabFragement;
import com.zhiyicx.zhibolibrary.ui.view.SearchView;
import com.zhiyicx.zhibolibrary.util.CharactorHandler;
import com.zhiyicx.zhibolibrary.util.DeviceUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class ZBLSearchActivity extends ZBLBaseActivity implements SearchView, View.OnClickListener {
    protected RelativeLayout rlSearchBack;
    protected EditText mKeyWordET;
    protected ImageView mClearIV;
    protected ImageView mSearchTV;
    protected TextView tvSearchCancel;
    protected TabLayout mTabLayout;
    protected AutoAppBarLayout mAppBarLayout;
    protected ViewPager mViewPager;


    private List<ZBLBaseFragment> mFragmentList = new ArrayList<>();
    private String[] mTitles = new String[]{"直播", "回放"};
    private AdapterViewPager mAdapter;
    private String mKeyWord;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void initView() {
        setContentView(R.layout.zb_activity_search);
        rlSearchBack = (RelativeLayout) findViewById(R.id.rl_search_back);
        rlSearchBack.setOnClickListener(ZBLSearchActivity.this);
        mKeyWordET = (EditText) findViewById(R.id.et_search_keyword);
        mClearIV = (ImageView) findViewById(R.id.et_search_clear);
        mClearIV.setOnClickListener(ZBLSearchActivity.this);
        mSearchTV = (ImageView) findViewById(R.id.tv_search_search);
        mSearchTV.setOnClickListener(ZBLSearchActivity.this);
        tvSearchCancel = (TextView) findViewById(R.id.tv_search_cancel);
        mTabLayout = (TabLayout) findViewById(R.id.tl_search_tab);
        mAppBarLayout = (AutoAppBarLayout) findViewById(R.id.al_search);
        mViewPager = (ViewPager) findViewById(R.id.vp_search);
    }

    @Override
    protected void initData() {
        mInputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mKeyWordET.setFilters(new InputFilter[]{CharactorHandler.emojiFilter});//emoji过滤
        initViewPager();//初始化Viewpager
        initTabLayout();//初始化tablayout
        initListener();//初始化监听
    }

    private void initViewPager() {
        //设置缓存的个数
        mViewPager.setOffscreenPageLimit(4);
        mAdapter = new AdapterViewPager(getSupportFragmentManager());
        mFragmentList.add(ZBLSearchTabFragement.newInstance("stream"));
        mFragmentList.add(ZBLSearchTabFragement.newInstance("video"));
        mAdapter.bindData(mFragmentList, mTitles);//将List设置给adapter
        mViewPager.setAdapter(mAdapter);
    }

    private void initTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.setupWithViewPager(mViewPager);//将tablayout和viewpager关联
    }

    private void initListener() {
        //滚动监听
        if (mAppBarLayout != null)
            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                    if (verticalOffset >= 0) {//完全展开才可以刷新
                        mFragmentList.get(mViewPager.getCurrentItem()).setData(true);
                    }
                    else {
                        mFragmentList.get(mViewPager.getCurrentItem()).setData(false);
                    }
                }
            });

        if (mKeyWordET != null)
            mKeyWordET.addTextChangedListener(new TextWatcher() {//文字改变监听
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mClearIV.setVisibility(TextUtils.isEmpty(charSequence.toString()) ? View.GONE : View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        mKeyWordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                    /*判断是否是“search”键*/
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction())) {
                    /*隐藏软键盘*/
                    hideInput();
                    search();//执行搜索
                    return true;
                }
                return false;
            }
        });
        if (mViewPager != null)
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (!TextUtils.isEmpty(mKeyWord)) {//切换其他页面继续查询
                        mFragmentList.get(position).setData(mKeyWord);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        if (mTabLayout != null)
            mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        if (mInputMethodManager.isActive()) {//判断软件盘是否要显示
            mInputMethodManager.hideSoftInputFromWindow(
                    mKeyWordET.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        killMyself();
    }


    private void search() {//搜索内容
        mKeyWord = mKeyWordET.getText().toString();
        if (TextUtils.isEmpty(mKeyWord)) {
            showMessage("关键字不能为空!");
            return;
        }
        DeviceUtils.hideSoftKeyboard(getApplicationContext(), mKeyWordET);
        mFragmentList.get(mViewPager.getCurrentItem()).setData(mKeyWord);//搜索
    }

    @Override
    public void killMyself() {
        hideInput();
        finish();
        this.overridePendingTransition(R.anim.animate_null, R.anim.vote_slide_out_from_left);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeviceUtils.fixInputMethodManagerLeak(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_search_back) {
            killMyself();//自杀
        }
        else if (view.getId() == R.id.et_search_clear) {
            mKeyWordET.setText("");//清除文字
        }
        else if (view.getId() == R.id.tv_search_search) {
            search();
        }
    }
}
