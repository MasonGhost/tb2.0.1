package com.zhiyicx.zhibolibrary.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.LiveModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.ApiArea;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.view.LiveView;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.KnifeUtil;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhiyicx on 2016/4/8.
 */
public class LivePresenter extends BasePresenter<LiveModel, LiveView> implements View.OnClickListener {

    TextView mFilterTitle1;

    TextView mFilterTitle2;

    TextView mFilterTitle3;

    TextView mFilterSubTitle2;
    View LLFilterSubTitle2;
    View LLFilterSubTitle3;

    TextView mFilterSubTitle3;

    TextView mFilterTimeStart;

    TextView mFilterTimeEnd;


    private SimpleDateFormat mFormat;
    private TimePickerView mTimePicker;
    private OptionsPickerView mOptionsPicker;
    private OptionsPickerView mAreaPicker;
    private LinearLayout mCotainer;
    private Date mDate;
    private Calendar mCalendar;
    private ArrayList<String> mList1;
    private Map<String, Object> mMap;

    private ArrayList<ApiArea> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<ApiArea>> options2Items = new ArrayList<>();
    private TimePickerView.OnTimeSelectListener mStartDateListener;
    private TimePickerView.OnTimeSelectListener mEndDateListener;
    private OptionsPickerView.OnOptionsSelectListener mAreaPickListener;
    private OptionsPickerView.OnOptionsSelectListener mGenderPickListener;

    private String mStartDate;
    private String mEndDate;
    private Integer mGender;
    private String mArea;
    private String mAreaText;

    private int mCityOption1;//用来记录滚轮的位置
    private int mCityOption2;
    private String mFilterTime;

    public LivePresenter(LiveView rootView) {
        super(rootView);

        mFilterTitle1 = (TextView) ((Fragment) rootView).getActivity().findViewById(R.id.tv_filter_title1);
        mFilterTitle2 = (TextView) ((Fragment) rootView).getActivity().findViewById(R.id.tv_filter_title2);
        mFilterTitle3 = (TextView) ((Fragment) rootView).getActivity().findViewById(R.id.tv_filter_title3);
        mFilterSubTitle2 = (TextView) ((Fragment) rootView).getActivity().findViewById(R.id.tv_filter_subTitle2);
        LLFilterSubTitle2= ((Fragment) rootView).getActivity().findViewById(R.id.ll_filter_subTitle2);
        LLFilterSubTitle2.setOnClickListener(this);
        mFilterSubTitle3 = (TextView) ((Fragment) rootView).getActivity().findViewById(R.id.tv_filter_subTitle3);
        LLFilterSubTitle3= ((Fragment) rootView).getActivity().findViewById(R.id.ll_filter_subTitle3);
        LLFilterSubTitle3 .setOnClickListener(this);
        mFilterTimeStart = (TextView) ((Fragment) rootView).getActivity().findViewById(R.id.tv_filter_time_start);
        mFilterTimeStart.setOnClickListener(this);
        mFilterTimeEnd = (TextView) ((Fragment) rootView).getActivity().findViewById(R.id.tv_filter_time_end);

        mFilterTimeEnd.setOnClickListener(this);
        ((Fragment) rootView).getActivity().findViewById(R.id.bt_filter_ok).setOnClickListener(this);


        initPickerView((Fragment) rootView);
    }

    public void initFilter(LinearLayout cotainer) {
        this.mCotainer = cotainer;
        KnifeUtil.bindTarget(this, cotainer);
        if (ZBLApi.sZBApiConfig == null || ZBLApi.sZBApiConfig.filter_list == null) {
            return;
        }
        mMap = new HashMap<>();//储存请求的key和value
        mFormat = new SimpleDateFormat();//创建日期格式
        mFormat = new SimpleDateFormat(ZBLApi.sZBApiConfig.filter_list.live[0].value[0]);//创建日期格式
        mDate = new Date();//创建当前时间
        mFilterTitle1.setText(ZBLApi.sZBApiConfig.filter_list.live[0].desc);
        if (ZBLApi.sZBApiConfig.filter_list.live.length > 1) {
            mFilterTitle2.setText(ZBLApi.sZBApiConfig.filter_list.live[1 - 1].desc);
            mFilterSubTitle2.setText(ZBLApi.sZBApiConfig.filter_list.live[1 - 1].value[0]);

            mFilterTitle3.setText(ZBLApi.sZBApiConfig.filter_list.live[2 - 1].desc);
            mFilterSubTitle3.setText(ZBLApi.sZBApiConfig.filter_list.live[2 - 1].value[0]);
        }
        else if (ZBLApi.sZBApiConfig.filter_list.live.length > 0) {
            mFilterTitle3.setText(ZBLApi.sZBApiConfig.filter_list.live[1 - 1].desc);
            mFilterSubTitle3.setText(ZBLApi.sZBApiConfig.filter_list.live[1 - 1].value[0]);
        }

        mCalendar.setTime(mDate);
        mCalendar.add(Calendar.DAY_OF_MONTH, -1);//前一天
        mFilterTimeStart.setText(mFormat.format(mCalendar.getTime()));
        mFilterTimeEnd.setText(mFormat.format(mDate));//今天


        //性别选择器要的数据
        mList1 = new ArrayList<>();
        for (String s : ZBLApi.sZBApiConfig.filter_list.live[1 - 1].value) {
            mList1.add(s);
        }
        mOptionsPicker.setPicker(mList1);//初始化
        mOptionsPicker.setCyclic(false);

        //地区选择器要的数据
        EventBus.getDefault().post("1", "get_area_result");
    }

    @Subscriber(tag = "get_area_result", mode = ThreadMode.ASYNC)
    private void getAreaResult(String string) {
        Log.w("test", string);
        try {
            String s = DataHelper.getAreaData(UiUtils.getContext());//本地数据
            if (s == null) {
                return;
            }
            //解析json
            JSONArray root = new JSONArray(s);
            for (int x = 0; x < root.length(); x++) {
                JSONObject parent = root.getJSONObject(x);
                ApiArea parentJson = new ApiArea(
                        parent.getString("area_id"),
                        parent.getString("title"));
                options1Items.add(parentJson);//选择器第一级
                ArrayList<ApiArea> options2ItemsChilds = new ArrayList<>();
                options2Items.add(options2ItemsChilds);//选择器第二级
                JSONArray childArray = parent.getJSONArray("child");
                for (int y = 0; y < childArray.length(); y++) {
                    JSONObject child = childArray.getJSONObject(y);
                    ApiArea childJson = new ApiArea(
                            child.getString("area_id"),
                            child.getString("title"));
                    options2ItemsChilds.add(childJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAreaPicker.setPicker(options1Items, options2Items, true);//初始化
        mAreaPicker.setCyclic(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_filter_time_start) {

            inflateStartDate();
            mTimePicker.show();
        }
        else if (v.getId() == R.id.tv_filter_time_end) {
            inflateEndDate();
            mTimePicker.show();
        }
        else if (v.getId() == R.id.ll_filter_subTitle2) {
            inflateGender();
            mOptionsPicker.show();
        }
        else if (v.getId() == R.id.ll_filter_subTitle3) {
            inflateArea();
            mAreaPicker.show();
        }
        else if (v.getId() == R.id.bt_filter_ok) {
            commit();
        }
    }

    /**
     * 提交筛选的请求
     */
    private void commit() {
        if (mMap != null)
            mMap.clear();//清除上一次的数据
        if (mStartDate != null && mEndDate != null) {
            mFilterTime = mStartDate + "," + mEndDate;
        }
        else if (mStartDate != null) {
            mFilterTime = mStartDate;
        }
        else if (mEndDate != null) {
            mFilterTime = "," + mEndDate;
        }

        if (mFilterTime != null) {
            mMap.put(ZBLApi.sZBApiConfig.filter_list.live[0].key, mFilterTime);
        }
        try {
            if (mGender != null && mMap != null) {
                mMap.put(ZBLApi.sZBApiConfig.filter_list.live[1 - 1].key, mGender + "");
            }

            if (mArea != null && mMap != null) {
                mMap.put(ZBLApi.sZBApiConfig.filter_list.live[2 - 1].key, mArea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mMap != null && mMap.size() != 0) {
            mRootView.startFilter(mMap);
        }

        mRootView.hideFilter();
    }

    /**
     * 填充开始日期选择器
     */
    private void inflateStartDate() {
        if (mStartDate == null) {
            mTimePicker.setTime(mCalendar.getTime());
        }
        else {
            try {
                mTimePicker.setTime(mFormat.parse(mStartDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mTimePicker.setOnTimeSelectListener(mStartDateListener);
    }

    /**
     * 填充结束日期选择器
     */
    private void inflateEndDate() {
        if (mEndDate == null) {
            mTimePicker.setTime(mDate);
        }
        else {
            try {
                mTimePicker.setTime(mFormat.parse(mEndDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mTimePicker.setOnTimeSelectListener(mEndDateListener);
    }

    /**
     * 填充地区选择器
     */
    private void inflateArea() {
        mAreaPicker.setOnoptionsSelectListener(mAreaPickListener);
        mAreaPicker.setSelectOptions(mCityOption1, mCityOption2);//显示之前选择的地区
    }

    /**
     * 填充性别选择器要的内容
     */
    private void inflateGender() {

        mOptionsPicker.setOnoptionsSelectListener(mGenderPickListener);
        if (mGender == null) {//选择之前选择的性别
            mOptionsPicker.setSelectOptions(0);
        }
        else {
            mOptionsPicker.setSelectOptions(mGender);
        }
    }

    /**
     * 初始化选择器
     *
     * @param rootView
     */
    private void initPickerView(Fragment rootView) {
        //时间选择器
        mTimePicker = new TimePickerView(rootView.getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        mCalendar = Calendar.getInstance();
        mTimePicker.setRange(mCalendar.get(Calendar.YEAR) - 1, mCalendar.get(Calendar.YEAR));
        mTimePicker.setTitle("请选择起止日期");
        mTimePicker.setCyclic(false);
        mTimePicker.setCancelable(true);

        //性别选择器

        mOptionsPicker = new OptionsPickerView(rootView.getActivity());
        mOptionsPicker.setCancelable(true);
        mOptionsPicker.setTitle("请选择性别");


        //地区选择器
        mAreaPicker = new OptionsPickerView(rootView.getActivity());
        mAreaPicker.setCancelable(true);
        mAreaPicker.setTitle("请选择地区");


        initPickerListener();//初始化PickView监听器
    }

    /**
     * pickview监听器
     */
    private void initPickerListener() {
        mStartDateListener = new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() > mDate.getTime()) {
                    mRootView.showMessage("请选择正确的日期!");
                    return;
                }
                mStartDate = mFormat.format(date);
                updateStartDate();

            }
        };
        mEndDateListener = new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() > mDate.getTime()) {
                    mRootView.showMessage("请选择正确的日期!");
                    return;
                }
                mEndDate = mFormat.format(date);
                updateEndDate();
            }
        };
        mGenderPickListener = new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mGender = options1;
                updateGenderText();
            }
        };
        mAreaPickListener = new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mArea = options2Items.get(options1).get(option2).getAreaId();
                mAreaText = options1Items.get(options1).getPickerViewText();
                String city = options2Items.get(options1).get(option2).getPickerViewText();
                mAreaText += city.equals("全部") ? "" : "-" + city;//如果为全部则不显示
                mCityOption1 = options1;
                mCityOption2 = option2;
                updateAreaText();
            }
        };
    }

    /**
     * 更新开始时间
     */
    public void updateStartDate() {
        if (mStartDate != null) {
            mFilterTimeStart.setText(mStartDate);
        }
    }

    /**
     * 更新关闭时间
     */
    public void updateEndDate() {
        if (mEndDate != null) {
            mFilterTimeEnd.setText(mEndDate);
        }
    }

    /**
     * 更新性别
     */
    public void updateGenderText() {
        if (mGender != null) {
            mFilterSubTitle2.setText(ZBLApi.sZBApiConfig.filter_list.live[1 - 1].value[mGender]);
        }

    }

    /**
     * 更新地区
     */
    public void updateAreaText() {
        if (mArea != null) {
            mFilterSubTitle3.setText(mAreaText);
        }
    }

}
