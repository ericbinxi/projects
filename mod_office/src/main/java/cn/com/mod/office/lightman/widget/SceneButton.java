package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joshua.common.util.PxAndDp;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.SceneInfo;

/**
 * 情景按钮
 * Created by CAT on 2014/11/14.
 */
public class SceneButton extends LinearLayout {
    private TextView mCancel;
    private TextView mSceneName;
    private ImageView mScene;
    private ImageView mDelete;
    private ImageView mClock;
    private ImageView mEdit;
    private SceneInfo mSceneInfo;
    private View mRoot;
    private ImageView mBorder1;
    private ImageView mBorder2;
    private boolean mIsChecked;
    private SceneButtonListener sceneButtonListener;

    public SceneButton(Context context) {
        super(context);
        init(context);
    }

    public SceneButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // 初始化
    private void init(final Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_scene_button, this, true);

        // 设置按钮的大小为屏幕宽度的1/4
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int dividerWidth = PxAndDp.dip2px(context, 1);
        mRoot = v.findViewById(R.id.root);
        mRoot.setLayoutParams(new LayoutParams(metrics.widthPixels / 4 - dividerWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 初始化控件
        mScene = (ImageView) v.findViewById(R.id.scene);
        mSceneName = (TextView) v.findViewById(R.id.scene_name);
        mDelete = (ImageView) v.findViewById(R.id.ic_delete);
        mClock = (ImageView) v.findViewById(R.id.ic_clock);
        mEdit = (ImageView) v.findViewById(R.id.ic_edit);
        mCancel = (TextView) v.findViewById(R.id.cancel);
        mBorder1 = (ImageView) v.findViewById(R.id.border1);
        mBorder2 = (ImageView) v.findViewById(R.id.border2);

        // 初始化事件处理
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneButtonListener != null) {
                    sceneButtonListener.onCanelClick(SceneButton.this, mSceneInfo);
                }
            }
        });

        mEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneButtonListener != null) {
                    sceneButtonListener.onEditClick(SceneButton.this, mSceneInfo);
                }
            }
        });

        mClock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneButtonListener != null) {
                    sceneButtonListener.onClockClick(SceneButton.this, mSceneInfo);
                }
            }
        });

        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneButtonListener != null) {
                    sceneButtonListener.onDeleteClick(SceneButton.this, mSceneInfo);
                }
            }
        });

        mScene.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneButtonListener != null) {
                    sceneButtonListener.onClick(SceneButton.this, mSceneInfo);
                }
            }
        });

        mScene.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (sceneButtonListener != null) {
                    sceneButtonListener.onLongClick(SceneButton.this, mSceneInfo);
                }
                return false;
            }
        });
    }

    // 加载情景数据
    public void setScenenInfo(SceneInfo sceneInfo) {
        this.mSceneInfo = sceneInfo;
        setSceneName(sceneInfo.name);
        switch (sceneInfo.type) {
            case SceneInfo.TYPE_DEFAULT:
                switch (Integer.parseInt(sceneInfo.id)) {
                    case 1:
                        setSceneIcon(getResources().getDrawable(R.drawable.default_01_off));
                        break;
                    case 2:
                        setSceneIcon(getResources().getDrawable(R.drawable.default_02_off));
                        break;
                    case 3:
                        setSceneIcon(getResources().getDrawable(R.drawable.default_03_off));
                        break;
                    case 4:
                        setSceneIcon(getResources().getDrawable(R.drawable.default_04_off));
                        break;
                }
            case SceneInfo.TYPE_DIY:
                if (sceneInfo.icon == null) {
                    setSceneIcon(getResources().getDrawable(R.drawable.default_diy2));
                } else {
                    setSceneIcon(sceneInfo.icon);
                }
                break;
            case SceneInfo.TYPE_NONE:
                setSceneIcon(getResources().getDrawable(R.drawable.diy_off));
                break;
        }
    }

    // 获取选中状态
    public boolean isChecked() {
        return this.mIsChecked;
    }

    // 设置选中状态
    public void setChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
        if (isChecked) {
            if (mSceneInfo.type == SceneInfo.TYPE_DEFAULT) {
                switch (Integer.parseInt(mSceneInfo.id)) {
                    case 1:
                        mScene.setImageResource(R.drawable.default_01);
                        break;
                    case 2:
                        mScene.setImageResource(R.drawable.default_02);
                        break;
                    case 3:
                        mScene.setImageResource(R.drawable.default_03);
                        break;
                    case 4:
                        mScene.setImageResource(R.drawable.default_04);
                        break;
                }
            } else if (mSceneInfo.type == SceneInfo.TYPE_NONE) {
                mScene.setImageResource(R.drawable.diy);
            }
            mBorder1.setImageResource(R.drawable.bg_scene_border);
            mBorder2.setImageResource(R.drawable.bg_scene_border);
            mEdit.setImageResource(R.drawable.ic_scene_edit);
            mClock.setImageResource(R.drawable.ic_scene_clock);
            mDelete.setImageResource(R.drawable.ic_scene_delete);

            mRoot.setBackgroundResource(R.drawable.bg_scene_button_press);
            mSceneName.setTextColor(getResources().getColor(R.color.black));
        } else {
            if (mSceneInfo.type == SceneInfo.TYPE_DEFAULT) {
                switch (Integer.parseInt(mSceneInfo.id)) {
                    case 1:
                        mScene.setImageResource(R.drawable.default_01_off);
                        break;
                    case 2:
                        mScene.setImageResource(R.drawable.default_02_off);
                        break;
                    case 3:
                        mScene.setImageResource(R.drawable.default_03_off);
                        break;
                    case 4:
                        mScene.setImageResource(R.drawable.default_04_off);
                        break;
                }
            } else if (mSceneInfo.type == SceneInfo.TYPE_NONE) {
                mScene.setImageResource(R.drawable.diy_off);
            }
            mBorder1.setImageResource(R.drawable.bg_scene_border_off);
            mBorder2.setImageResource(R.drawable.bg_scene_border_off);
            mEdit.setImageResource(R.drawable.ic_scene_edit_off);
            mClock.setImageResource(R.drawable.ic_scene_clock_off);
            mDelete.setImageResource(R.drawable.ic_scene_delete_off);
            mRoot.setBackgroundResource(R.drawable.bg_scene_button);
            mSceneName.setTextColor(getResources().getColor(R.color.white));
        }
    }

    // 设置情景名称
    public void setSceneName(String text) {
        mSceneName.setText(text);
    }

    // 设置情景图标
    public void setSceneIcon(Drawable drawable) {
        mScene.setImageDrawable(drawable);
    }

    // 显示工具
    public void showTools() {
        if (mSceneInfo != null) {
            switch (mSceneInfo.type) {
                case SceneInfo.TYPE_DEFAULT:
                    mClock.setVisibility(VISIBLE);
                    mCancel.setVisibility(VISIBLE);
                    break;
                case SceneInfo.TYPE_DIY:
                    mDelete.setVisibility(VISIBLE);
                    mEdit.setVisibility(VISIBLE);
                    mClock.setVisibility(VISIBLE);
                    mCancel.setVisibility(VISIBLE);
                    break;
                case SceneInfo.TYPE_NONE:
                    break;
            }
        }
    }

    // 隐藏工具
    public void hideTools() {
        if (mSceneInfo != null) {
            mDelete.setVisibility(INVISIBLE);
            mEdit.setVisibility(INVISIBLE);
            mClock.setVisibility(INVISIBLE);
        }
        mCancel.setVisibility(INVISIBLE);
    }

    // 设置按钮监听器
    public void setSceneButtonListener(SceneButtonListener listener) {
        this.sceneButtonListener = listener;
    }

    // 按钮监听器接口
    public interface SceneButtonListener {
        public void onLongClick(SceneButton button, SceneInfo info);

        public void onClick(SceneButton button, SceneInfo info);

        public void onClockClick(SceneButton button, SceneInfo info);

        public void onDeleteClick(SceneButton button, SceneInfo info);

        public void onEditClick(SceneButton button, SceneInfo info);

        public void onCanelClick(SceneButton button, SceneInfo info);
    }
}
