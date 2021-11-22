package com.ffzxnet.developutil.utils.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

//带有弹性的RecyclerView
public class RecyclerViewDampingLayoutView extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    //滚动时间 值越大，回弹速度越快
    private static final long ANIM_TIME = 200;
    private View childView;
    private Rect originalRect = new Rect();
    private List<View> mMoveViews = new ArrayList<>();
    private List<Rect> mMoveRects = new ArrayList<>();
    private boolean isMoved = false;
    private float startY;
    //阻尼大小
    private static final float OFFSET_RADIO = 0.5f;
    //最小滑动Y距离
    private final int canScrollY = 50;
    private boolean isRecyclerResult = false;

    public RecyclerViewDampingLayoutView(Context context) {
        this(context, null);
    }

    public RecyclerViewDampingLayoutView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewDampingLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setVerticalScrollBarEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof RecyclerView || getChildAt(i) instanceof ListView
                        || getChildAt(i) instanceof ScrollView) {
                    if (childView == null) {
                        childView = getChildAt(i);
                    } else {
                        throw new RuntimeException("MyScrollView 只能存在一个滑动子控件");
                    }
                }
            }
        }
        if (childView == null) {
            throw new RuntimeException("MyScrollView 必须有一个滑动子控件");
        }
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        super.onFinishInflate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        originalRect.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
        for (int i = 0; i < mMoveViews.size(); i++) {
            final View v = mMoveViews.get(i);
            v.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Rect rect = new Rect();
                    rect.set(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    mMoveRects.add(rect);
                    v.removeOnLayoutChangeListener(this);
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (childView == null) {
            return super.dispatchTouchEvent(ev);
        }
        boolean isTouchOutOfScrollView = ev.getY() >= originalRect.bottom || ev.getY() <= originalRect.top;
        if (isTouchOutOfScrollView) {
            //如果不在View的范围内
            if (isMoved) {
                recoverLayout();
            }
            return true;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int scrollY = (int) (nowY - startY);
                if ((isCanPullDown() && scrollY > canScrollY) || (isCanPullUp() && scrollY < -canScrollY) || (isCanPullUp() && isCanPullDown())) {
                    int offSet = (int) (scrollY * OFFSET_RADIO);
                    childView.layout(originalRect.left, originalRect.top + offSet, originalRect.right
                            , originalRect.bottom + offSet);
                    for (int i = 0; i < mMoveViews.size(); i++) {
                        if (mMoveViews.get(i) != null) {
                            mMoveViews.get(i).layout(mMoveRects.get(i).left, mMoveRects.get(i).top + offSet
                                    , mMoveRects.get(i).right, mMoveRects.get(i).bottom + offSet);
                        }
                    }
                    isMoved = true;
                    isRecyclerResult = false;
                    return true;
                } else {
                    startY = ev.getY();
                    isMoved = false;
                    isRecyclerResult = true;
                    recoverLayout();
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                if (isMoved) {
                    recoverLayout();
                }
                if (isRecyclerResult) {
                    return super.dispatchTouchEvent(ev);
                } else {
                    return true;
                }
            default:
                return true;
        }
    }

    /**
     * 位置还原
     */
    private void recoverLayout() {
        if (!isMoved) {
            return;
        }
        for (int i = 0; i < mMoveViews.size(); i++) {
            if (mMoveRects.get(i) != null) {
                TranslateAnimation anims = new TranslateAnimation(0, 0, mMoveViews.get(i).getTop(), mMoveRects.get(i).top);
                anims.setDuration(ANIM_TIME);
                mMoveViews.get(i).startAnimation(anims);
                mMoveViews.get(i).layout(mMoveRects.get(i).left, mMoveRects.get(i).top, mMoveRects.get(i).right, mMoveRects.get(i).bottom);
            }
        }

        TranslateAnimation anim = new TranslateAnimation(0, 0, childView.getTop() - originalRect.top, 0);
        anim.setDuration(ANIM_TIME);
        childView.startAnimation(anim);
        childView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
        isMoved = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //是否可以下拉
    private boolean isCanPullDown() {
        if (childView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) childView;
            final RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) {
                return true;
            }
            int firstVisiblePosition = 0;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                firstVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                int[] firs = null;
                firs = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(firs);
                if (firs != null) {
                    firstVisiblePosition = firs[0];
                }
            }
            if (firstVisiblePosition != 0 && adapter.getItemCount() != 0) {
                return false;
            }
            int mostTop = recyclerView.getChildCount() > 0 ? recyclerView.getChildAt(0).getTop() : 0;
            return mostTop >= 0;
        }

        return false;
    }

    //是否可以上拉
    private boolean isCanPullUp() {
        if (childView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) childView;
            final RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) {
                return true;
            }

            int lastItemPostion = adapter.getItemCount() - 1;
            int lastVisiblePosition = 0;
            int firstVisiblePosition = 0;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                lastVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                firstVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                int[] last = null;
                last = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPositions(last);
                if (last != null) {
                    lastVisiblePosition = last[0];
                }
                int[] firs = null;
                firs = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(firs);
                if (firs != null) {
                    firstVisiblePosition = firs[0];
                }
            }
            if (lastVisiblePosition >= lastItemPostion) {
                final int childIndex = lastVisiblePosition - firstVisiblePosition;
                final int childCount = recyclerView.getChildCount();
                final int index = Math.min(childIndex, childCount - 1);
                final View lastVisibleChild = recyclerView.getChildAt(index);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= childView.getBottom() - childView.getTop();
                }
            }
        }
        return false;
    }

    @Override
    public void onGlobalLayout() {
        requestLayout();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public void setMoveViews(View views) {
        this.mMoveViews.add(views);
        requestLayout();
    }
}
