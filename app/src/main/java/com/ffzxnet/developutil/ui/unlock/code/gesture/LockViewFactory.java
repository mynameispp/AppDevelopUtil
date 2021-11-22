package com.ffzxnet.developutil.ui.unlock.code.gesture;

/**
 * LockViewFactory 启发自 ThreadPoolExecutor 中的 ThreadFactory
 */
public interface LockViewFactory {

    /**
     * 创建 LockView,必须是 newInstance 不能复用一个对象
     * @return
     */
    ILockView newLockView();
}
