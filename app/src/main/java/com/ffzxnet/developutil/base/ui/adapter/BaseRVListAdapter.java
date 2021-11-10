package com.ffzxnet.developutil.base.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffzxnet.developutil.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建者： feifan.pi 在 2017/10/27.
 */

public abstract class BaseRVListAdapter<T> extends RecyclerView.Adapter {
    //无数据布局
    private final int type_no_data = 10086;
    //加载更多布局
    private final int type_load_more = 10010;
    //每次请求的数据量
    private int pageSize = 20;
    private boolean noMoreData;//是否已经加载完所有数据
    //空数据提示
    private String emptyMsg;
    //空数据提示文字颜色
    private int emptyTxtColor;
    //空数据文字大小
    private int emptyTxtSize;
    //空数据图片
    private int emptyResImage;
    //底部控件文字内容(已加载所有)
    private String noMoreMsg;
    //底部控件文字内容（正在加载中）
    private String loadingMsg;
    //底部控件背景
    private int bottomBGResId;
    //底部控件文字颜色
    private int bottomTextColorId;
    //是否需要底部提示布局
    private boolean noBottomView = false;
    //是否显示空数据布局
    private boolean noEmptyView = false;
    //当数据为空或者网络异常的时候可以点击屏幕响应方便重新加载
    private BaseRVListAdapterEmptyViewClickListen emptyClickListen;
    //设置底部提示控件点击事件
    private BaseRVListAdapterBottomViewClickListen bottomViewClickListen;
    //列表是否横向向滑动
    private boolean isHorizontalScroll;
    //列表数据
    private List<T> datas;

    public BaseRVListAdapter(List<T> datas) {
        addDatas(datas);
        noMoreData = this.datas.size() < pageSize;
    }

    public BaseRVListAdapter(List<T> datas, int maxSize) {
        pageSize = maxSize;
        addDatas(datas);
        noMoreData = this.datas.size() < pageSize;
    }

    /**
     * 是否是横向滑动
     *
     * @param horizontalScroll false
     */
    public void setIsHorizontalScroll(boolean horizontalScroll) {
        isHorizontalScroll = horizontalScroll;
    }

    /**
     * 默认每次请求20条
     * 设置每页数据量，需在setAdapter之前设置
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (null != getDatas()) {
            //数据不为空时，要判断下
            noMoreData = getDatas().size() < pageSize;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getDatas() {
        return datas;
    }

    /**
     * 是否还可以加载
     *
     * @return
     */
    public boolean isNoMoreData() {
        return noMoreData;
    }

    /**
     * 手动设置显示布局状态
     *
     * @param noMoreData
     */
    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
    }

    /**
     * 重新设置数据，之前的数据全部删除
     *
     * @param setDatas
     */
    public void setDatas(List<T> setDatas) {
        if (null == datas) {
            datas = new ArrayList<>();
        }
        if (null == setDatas) {
            setDatas = new ArrayList<>();
        }
        //如果不够请求数据的数量则表示已经加载完所有的数据
        noMoreData = setDatas.size() < pageSize;
        datas.clear();
        datas.addAll(setDatas);
        notifyDataSetChanged();
    }

    public void addDatas(List<T> addDatas) {
        if (null == datas) {
            datas = new ArrayList<>();
        }
        if (null == addDatas) {
            addDatas = new ArrayList<>();
        }
        noMoreData = addDatas.size() < pageSize;
        if (addDatas.size() > 0) {
            int oldSize = datas.size();
            datas.addAll(addDatas);
            notifyItemChanged(oldSize, datas.size());
        }
    }

    public void addDatas(List<T> addDatas, int position) {
        if (null == datas) {
            datas = new ArrayList<>();
        }
        if (null == addDatas) {
            addDatas = new ArrayList<>();
        }
        noMoreData = addDatas.size() < pageSize;
        if (addDatas.size() > 0) {
            int oldSize = datas.size();
            datas.addAll(position, addDatas);
            notifyItemChanged(position, datas.size() - oldSize);
        }
    }

    /**
     * 添加单个数据
     *
     * @param addData
     */
    public void add(T addData) {
        if (addData == null) {
            return;
        }
        int oldSize = datas.size();
        datas.add(addData);
        notifyItemInserted(datas.size() - 1);
        notifyItemRangeChanged(oldSize, datas.size());
    }

    /**
     * 添加单个数据
     *
     * @param position 指定位置
     */
    public void add(int position, T addData) {
        if (addData == null) {
            return;
        }
        int oldSize = datas.size();
        datas.add(position, addData);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, datas.size());
    }

    /**
     * 删除数据
     *
     * @param index
     */
    public void deleteData(int index) {
        datas.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, datas.size());
    }

    /**
     * 删除数据
     *
     * @param t
     */
    public void deleteData(T t) {
        deleteData(datas.indexOf(t));
    }

    /**
     * 清空所有数据
     */
    public void clearData() {
        noMoreData = false;
        if (null != datas) {
            datas.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 空数据提示文字
     *
     * @param emptyMsg
     */
    public void setEmptyMsg(String emptyMsg) {
        this.emptyMsg = emptyMsg;
    }

    /**
     * 空数据提示文字颜色
     *
     * @param emptyMsgColor
     */
    public void setEmptyMsgColor(int emptyMsgColor) {
        this.emptyTxtColor = emptyMsgColor;
    }

    /**
     * 空数据提示文字大小
     *
     * @param emptyMsgSize
     */
    public void setEmptyMsgSize(int emptyMsgSize) {
        this.emptyTxtSize = emptyMsgSize;
    }

    /**
     * 加载完所有数据文字
     *
     * @param noMoreMsg
     */
    public void setNoMoreMsg(String noMoreMsg) {
        this.noMoreMsg = noMoreMsg;
    }

    /**
     * 底部提示背景
     *
     * @return
     */
    public void setBottomBGResId(int bottomBGResId) {
        this.bottomBGResId = bottomBGResId;
    }

    /**
     * 底部提示文字颜色
     *
     * @param bottomTextColorId
     */
    public void setBottomTextColorId(int bottomTextColorId) {
        this.bottomTextColorId = bottomTextColorId;
    }

    /**
     * 加载中文字
     *
     * @param loadingMsg
     */
    public void setLoadingMsg(String loadingMsg) {
        this.loadingMsg = loadingMsg;
    }

    /**
     * 空数据提示图片
     *
     * @param emptyResImage
     */
    public void setEmptyResImage(int emptyResImage) {
        this.emptyResImage = emptyResImage;
    }

    /**
     * 是否需要底部布局
     *
     * @param noBottomView true=不要
     */
    public void setNoBottomView(boolean noBottomView) {
        this.noBottomView = noBottomView;
    }

    /**
     * 是否需要空数据布局
     *
     * @param noEmptyView true=不要
     */
    public void setNoEmptyView(boolean noEmptyView) {
        this.noEmptyView = noEmptyView;
    }

    /**
     * 当数据为空或者网络异常的时候可以点击屏幕响应方便重新加载
     *
     * @param emptyClickListen
     */
    public void setEmptyClickListen(BaseRVListAdapterEmptyViewClickListen emptyClickListen) {
        this.emptyClickListen = emptyClickListen;
    }

    /**
     * 设置底部提示控件点击事件
     *
     * @param bottomViewClickListen
     */
    public void setBottomViewClickListen(BaseRVListAdapterBottomViewClickListen bottomViewClickListen) {
        this.bottomViewClickListen = bottomViewClickListen;
    }

    /**
     * 自定义的数据布局类型
     * type_no_data = 10086;
     * type_load_more = 10010;
     * 以上两种数据不可以用
     *
     * @param position
     * @return
     */
    public abstract int getMyItemViewType(int position);

    @Override
    public int getItemViewType(int position) {
        if (null == datas || datas.size() == 0) {
            if (onAddTopItemCount() > 0 && position < onAddTopItemCount()) {
                //自定义数据
                return getMyItemViewType(position);
            }
            //无数据
            return type_no_data;
        } else if (position == datas.size() + onAddTopItemCount() + onAddBottomItemCount()) {
            //底部提示控件
            return type_load_more;
        } else {
            //自定义数据
            return getMyItemViewType(position);
        }
    }

    public abstract RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == type_load_more) {
            //提示加载更多或已经加载完所有数据
            if (isHorizontalScroll) {
                //横版列表的加载提示控件
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_bottom_view_vertical, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_bottom_view, parent, false);
            }
            return new ListBottomViewHolder(view);
        } else if (viewType == type_no_data) {
            //提示没有数据
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_empty, parent, false);
            return new ListEmptyViewHolder(view);
        } else {
            //自定义数据布局
            return onMyCreateViewHolder(parent, viewType);
        }
    }

    public abstract void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListEmptyViewHolder) {
            //空数据布局
            if (!noEmptyView) {
                final ListEmptyViewHolder emptyViewHolder = (ListEmptyViewHolder) holder;
                if (emptyResImage > 0) {
                    emptyViewHolder.setEmpty_img(emptyResImage);
                }
                String msg;
                if (!TextUtils.isEmpty(emptyMsg)) {
                    msg = emptyMsg;
                } else {
                    msg = "暂无数据";
                }
                if (null != emptyClickListen) {
                    emptyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            emptyClickListen.listEmptyViewClickListen();
                        }
                    });
                }

                if (emptyTxtColor != 0) {
                    emptyViewHolder.setEmpty_msg_color(emptyTxtColor);
                }
                if (emptyTxtSize > 0) {
                    emptyViewHolder.setEmpty_msg_size(emptyTxtSize);
                }

                emptyViewHolder.setEmpty_msg(msg);
            } else {
                holder.itemView.setVisibility(View.GONE);
            }
        } else if (holder instanceof ListBottomViewHolder) {
            //列表底部提示布局
            final ListBottomViewHolder listBottomViewHolder = (ListBottomViewHolder) holder;
            if (bottomBGResId > 0) {
                //设置背景
                listBottomViewHolder.setMsgBg(bottomBGResId);
            }
            if (bottomTextColorId > 0) {
                //设置字体颜色
                listBottomViewHolder.setMsgColor(bottomTextColorId);
            }
            if (noMoreData) {
                if (TextUtils.isEmpty(noMoreMsg)) {
                    listBottomViewHolder.setMsg("已加载全部数据");
                } else {
                    listBottomViewHolder.setMsg(noMoreMsg);
                }
                listBottomViewHolder.setloadingPBVisible(false);
                if (null != bottomViewClickListen) {
                    listBottomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomViewClickListen.listBottomViewClickListen();
                        }
                    });
                }
            } else if (listBottomViewHolder.getLoadingPBVisible() == View.GONE) {
                if (TextUtils.isEmpty(loadingMsg)) {
                    listBottomViewHolder.setMsg("正在加载中...");
                } else {
                    listBottomViewHolder.setMsg(loadingMsg);
                }
                listBottomViewHolder.setloadingPBVisible(true);
            }
        } else {
            //自定义绑定数据 position= (position- onAddTopItemCount())是因为列表前面可能有布局
            //在继承BaseRvList的子类中的getItemViewType（int position）
            // position < 0 的位置是数据列表前面的布局  position >= datas.size()是数据列表后面的布局
            onMyBindViewHolder(holder, position - onAddTopItemCount());
        }
    }

    /**
     * 额外在列表前添加布局
     * 没有使用0即可
     * 此处说的列表是数据列表
     *
     * @return
     */
    public abstract int onAddTopItemCount();

    /**
     * 额外在列表后面添加布局
     * 没有使用0即可
     * 此处说的列表是数据列表
     *
     * @return
     */
    public abstract int onAddBottomItemCount();

    @Override
    public int getItemCount() {
        int itemCount = onAddTopItemCount() + onAddBottomItemCount();
        if (!noBottomView) {
            //是否要底部提示语句
            itemCount = itemCount + 1;
        }
        if (null != datas) {
            itemCount = itemCount + datas.size();
        }
        if (itemCount == 0 && !noEmptyView) {
            //添加空数据布局
            itemCount = 1;
        }
        return itemCount;
    }

    /**
     * 退出界面销毁adapter
     */
    public void destory() {
        clearData();
        emptyClickListen = null;
        bottomViewClickListen = null;
        datas = null;
    }

    /**
     * 当数据为空或者网络异常的时候可以点击屏幕响应方便重新加载
     */
    public interface BaseRVListAdapterEmptyViewClickListen {
        /**
         * 当数据为空或者网络异常的时候可以点击屏幕响应方便重新加载
         */
        void listEmptyViewClickListen();
    }

    /**
     * 设置底部提示控件点击事件
     */
    public interface BaseRVListAdapterBottomViewClickListen {
        /**
         * 当数据为空或者网络异常的时候可以点击屏幕响应方便重新加载
         */
        void listBottomViewClickListen();
    }
}
