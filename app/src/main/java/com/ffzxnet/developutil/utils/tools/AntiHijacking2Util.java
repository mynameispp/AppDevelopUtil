package com.ffzxnet.developutil.utils.tools;

import android.app.Activity;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//不会触发隐私政策
//但每次都会提示
public class AntiHijacking2Util {
    //功能开关，true关闭提示
    public static boolean OFF = false;
    /**
     * 用于执行定时任务
     */
    private Timer timer = null;
    /**
     * 用于保存当前任务
     */
    private List<MyTimerTask> tasks = null;

    private static AntiHijacking2Util AntiHijacking2Util;

    private AntiHijacking2Util() {
        tasks = new ArrayList<>();
        timer = new Timer();
    }

    public static AntiHijacking2Util getInstance() {
        if (AntiHijacking2Util == null) {
            AntiHijacking2Util = new AntiHijacking2Util();
        }
        return AntiHijacking2Util;
    }

    public void onResume() {
        if (tasks.size() > 0) {
            tasks.get(tasks.size() - 1).setCanRun(false);
            tasks.remove(tasks.size() - 1);
        }
    }

    public void onPause(final Activity activity) {
        MyTimerTask task = new MyTimerTask(activity);
        tasks.add(task);
        timer.schedule(task, 1000);
    }

    class MyTimerTask extends TimerTask {
        private boolean canRun = true;
        private Activity activity;

        public void setCanRun(boolean canRun) {
            this.canRun = canRun;
        }

        public MyTimerTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!OFF && canRun && !BaseActivity.activities.contains(activity) && !MyConstans.Screen_Off) {
                        ToastUtil.showToastLongForAnit(MyApplication.getContext(),
                                activity.getString(R.string.activity_safe_warning)
                                , MyApplication.getColorByResId(R.color.colorPrimaryDark));
                    }
                    if (MyConstans.Screen_Off) {
                        MyConstans.Screen_Off = false;
                    }
                }
            });
        }
    }
}
