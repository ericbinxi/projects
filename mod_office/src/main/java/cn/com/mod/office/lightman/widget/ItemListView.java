package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.adapter.ItemAdapter;

/**
 * 顶部展开列表
 * Created by CAT on 2014/10/29.
 */
public class ItemListView extends LinearLayout {
    private CheckBox mCheckAll;
    private ListView mListView;
    private ImageView mHasMore;
    private ItemAdapter mAdapter;
    private View content;

    public ItemListView(Context context) {
        super(context);
        init();
    }

    public ItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ItemAdapter adapter) {
        this.mAdapter = adapter;
        mAdapter.setOnSelectedCountChangeListener(new ItemAdapter.OnItemSelectedChangeListener() {
            @Override
            public void onItemSelectedChange(int itemSize, int selectedCount) {
                if (itemSize == selectedCount) {
                    mCheckAll.setChecked(true);
                } else {
                    mCheckAll.setChecked(false);
                }
            }
        });
        mListView.setAdapter(mAdapter);
    }

    // 初始化
    private void init() {
        // 加载视图
        content = View.inflate(getContext(), R.layout.view_list_content, null);
        if (Build.VERSION.SDK_INT >= 11) {
            content.setAlpha(0.8f);
        }
        // 加载组件
        // 全选按钮的背景
        final View checkAllBg = content.findViewById(R.id.bg_check_all);
        // 全选按钮
        mCheckAll = (CheckBox) content.findViewById(R.id.check_all);
        // 更多图标
        mHasMore = (ImageView) content.findViewById(R.id.has_more);
        // ListView
        mListView = (ListView) content.findViewById(R.id.list);

        // 初始化事件
        checkAllBg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckAll.performClick();
            }
        });
        mCheckAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mCheckAll.isChecked();
                for (int i = 0; i < mListView.getChildCount(); i++) {
                    View child = mListView.getChildAt(i);
                    TextView tv = (TextView) child.findViewById(R.id.name);
                    CheckBox cb = (CheckBox) child.findViewById(R.id.cb);
                    cb.setChecked(isChecked);
                }
                ((ItemAdapter) mListView.getAdapter()).selectAll(isChecked);
                mCheckAll.setChecked(isChecked);
            }
        });
        // 通过监听滚动事件控制更多图标的状态
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    mHasMore.setVisibility(INVISIBLE);
                } else {
                    mHasMore.setVisibility(VISIBLE);
                }
            }
        });
        this.addView(content);
    }

    private RelativeLayout choose_rooms;
    private LinearLayout choose_mode;
    private LinearLayout ll_mode_manager;
    private LinearLayout ll_clocks_setting;
    private LinearLayout ll_fault_declare;

    public void setRoomMode(final OnChooseRoomMode l){
        choose_mode = (LinearLayout) content.findViewById(R.id.choose_mode);
        ll_mode_manager = (LinearLayout) content.findViewById(R.id.ll_mode_manager);
        ll_clocks_setting = (LinearLayout) content.findViewById(R.id.ll_clocks_setting);
        ll_fault_declare = (LinearLayout) content.findViewById(R.id.ll_fault_declare);
        choose_rooms = (RelativeLayout) content.findViewById(R.id.choose_rooms);
        choose_rooms.setVisibility(GONE);
        choose_mode.setVisibility(VISIBLE);

        ll_mode_manager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onChooseRoomMode(1);
            }
        });
        ll_clocks_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onChooseRoomMode(2);
            }
        });
        ll_fault_declare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onChooseRoomMode(3);
            }
        });
    }

    public interface OnChooseRoomMode{
        void onChooseRoomMode(int index);
    }
}
