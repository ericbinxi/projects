package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.joshua.common.adapter.SimpleViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.config.ConfigUtils;


/**
 * 介绍界面，展示应用介绍图片
 * Created by CAT on 2014/11/6.
 */
public class IntroduceActivity extends BaseActivity {
    public static final String TAG = "IntroduceActivity";

    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    // 介绍图片的资源
    private int[] mImages = new int[]{
            R.drawable.introduce_01,
            R.drawable.introduce_02,
            R.drawable.introduce_03
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        // 初始化组件
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        // 设置适配器
        mViewPager.setAdapter(new SimpleViewPagerAdapter(getIntroduceViews()));
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mIndicator.setViewPager(mViewPager);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 当先显示的
            private int currentPage;
            // 是否正在跳转，防止Intent多次跳转
            private boolean isSkiping;

            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (isSkiping) {
                    return;
                }
                if (i == mImages.length - 1 && i == currentPage && v == 0) {
                    isSkiping = true;
                    Intent intent = new Intent(IntroduceActivity.this, LoginActivity.class);
                    new ConfigUtils(IntroduceActivity.this).setFirstLaunch(false);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
                currentPage = i;
            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    // 获取介绍界面的视图集
    public List<View> getIntroduceViews() {
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < mImages.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(mImages[i]);
            views.add(imageView);
        }
        return views;
    }

    @Override
    public void loadData() {

    }
}
