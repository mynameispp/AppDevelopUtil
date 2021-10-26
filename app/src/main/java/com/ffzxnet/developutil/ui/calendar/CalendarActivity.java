package com.ffzxnet.developutil.ui.calendar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.calendar.view.MyCalendarPainter;
import com.ffzxnet.developutil.ui.calendar.view.MyWeekBar;
import com.ffzxnet.developutil.utils.tools.DateUtils;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.EmuiCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;

import org.joda.time.LocalDate;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class CalendarActivity extends BaseActivity {
    @BindView(R.id.my_week)
    MyWeekBar myWeek;
    @BindView(R.id.my_calendar_rv)
    RecyclerView myCalendarRv;
    @BindView(R.id.my_calendar)
    EmuiCalendar myCalendar;

    //当前选中的日期
    private String nowSelectDate;
    //是否可折叠.默认打开
    private boolean canSlow = true;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_calendar;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "");
        initCalendar();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    private void initCalendar() {
        MyCalendarPainter myCalendarPainter = new MyCalendarPainter(this);
        //显示模式-上下个月的日期颜色为灰色
        myCalendarPainter.setShowModule(1);
        //添加日期标记点
//        List<String> point = new ArrayList<>();
//        point.add("2021-08-04");
//        point.add("2021-08-11");
//        point.add("2021-09-01");
//        point.add("2021-07-03");
//        myCalendarPainter.setPointList(point);
        myCalendar.setCalendarPainter(myCalendarPainter);
        //标记今日是星期几
        myWeek.setCurrentDayOfWeek(DateUtils.getDayOfWeek());

        myCalendar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                myCalendar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                myCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
                    @Override
                    public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                        if (!localDate.toString().equals(nowSelectDate)) {
                            nowSelectDate = localDate.toString();
                            setToolBarTitle(localDate.toString(DateUtils.FORMAT_YYYY2MM2DD_W));
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.ln_expand, R.id.my_calendar_go_today})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_expand:
                if (canSlow) {
                    canSlow = false;
                    myCalendar.toWeek();
                } else {
                    canSlow = true;
                    myCalendar.toMonth();
                }
                break;
            case R.id.my_calendar_go_today:
                myCalendar.toToday();
                break;
        }
    }
}
