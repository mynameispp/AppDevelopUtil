package com.ffzxnet.developutil.ui.boot_page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建者： feifan.pi 在 2017/7/19.
 */

public class BootPageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.boot_page_vs)
    ViewStub bootPageVs;
    @BindView(R.id.boot_page_countdown_tv)
    TextView bootPageCountdownTv;

    private boolean coerceStop = false;

    @Override
    protected void onClickTitleBack() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //启动页，不允许按返回键退出
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_page);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
//        Constans.userInfo = new UserInfo();
        //判断是否是第一次使用
        boolean firstUse=true;
//        boolean firstUse = SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.KEY_FIRST_TIME_USE, true);
        if (firstUse) {
            //第一次使用，显示引导页
//            SharedPreferencesUtil.getInstance().putBoolean(SharedPreferencesUtil.KEY_FIRST_TIME_USE, false);
            View view = bootPageVs.inflate();
            initViewPage(view);
        } else {
            bootPageCountdownTv.setVisibility(View.VISIBLE);
            countdownJump();
        }
    }

    /**
     * 倒计时跳转
     */
    private void countdownJump() {
        bootPageCountdownTv.setEnabled(false);
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //做一些费时的操作
                //5秒后跳转
                int i = 5;
                while (i >= 0 && !coerceStop) {
                    try {
                        emitter.onNext(i);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i--;
                }
                emitter.onNext(i);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())// 此方法为上面发出事件设置线程为IO线程
                .observeOn(AndroidSchedulers.mainThread())// 为消耗事件设置线程为UI线程
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer <= 3) {
                            bootPageCountdownTv.setText(integer + "秒 跳过");
                            bootPageCountdownTv.setEnabled(true);
                        } else {
                            bootPageCountdownTv.setText(integer + "秒");
                        }
                        if (integer == 0 || coerceStop) {
                            coerceStop = false;
                            //读取初始化的一些信息
                            getAppInitInfo();
                            finish();
                        }
                    }
                });
    }

    private void getAppInitInfo() {
        //读取本地用户信息
//        String userInfoJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_USER_INFO, "");
//        if (TextUtils.isEmpty(userInfoJson)) {
//            //没有就跳到登录
//            redirectActivity(LoginAndRegistActivity.class);
//        } else {
        try {
            //获取成功，跳到主界面
//            if (!TextUtils.isEmpty(userInfoJson)) {
//                UserInfo jUserinfo = (UserInfo) GsonUtil.toClass(userInfoJson, UserInfo.class);
//                LogUtil.showLog("userInfo==" , jUserinfo.toString() );
//                Constans.userInfo = jUserinfo;
//            }
//            redirectActivity(MainActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
            //解析数据异常，表示数据已更换或损坏
//            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_USER_INFO, "");
//            redirectActivity(MainActivity.class);
        }
//        }
    }

    //引导图
    private int[] guideImgs = {0};
    private int guideImgsLength = guideImgs.length;

    /**
     * 初始化引导页
     *
     * @param view
     */
    private void initViewPage(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.boot_page_vp);
        //移动点父类布局
        LinearLayout pointLay = (LinearLayout) view.findViewById(R.id.boot_page_point_lay);
        //引导图数据源
        List<SimpleDraweeView> draweeViews = new ArrayList<>();
        ViewGroup.LayoutParams draweeViewsParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        for (int i = 0; i < guideImgsLength; i++) {
            SimpleDraweeView draweeView = new SimpleDraweeView(this);
            draweeView.setLayoutParams(draweeViewsParams);
            GenericDraweeHierarchy hierarchy = builder
                    .setPlaceholderImage(guideImgs[i])
                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build();
            draweeView.setHierarchy(hierarchy);
            draweeViews.add(draweeView);
            if (i == (guideImgsLength - 1)) {
                draweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到登录界面
//                        redirectActivity(LoginAndRegistActivity.class);
                        finish();
                    }
                });
            }
        }
        BootPageAdapter adapter = new BootPageAdapter(draweeViews);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.boot_page_countdown_tv)
    public void onViewClicked() {
        //跳过
        coerceStop = true;
    }
}
