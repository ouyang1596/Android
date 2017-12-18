package cn.egoa.sharehelper.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import cn.egoa.sharehelper.ApplicationBase;
/**
 * Created by kds on 2017/12/18.
 */

public class AppTool {

    /**
     * 获取视图测量宽度
     */
    public static int getMeasuredWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        return width;
    }

    /**
     * 获取视图测量高度
     */
    public static int getMeasuredHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        return height;
    }

    /**
     * 获取顶部status bar 高度
     */
    public static int getStatusBarHeight(Context mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取底部 navigation bar 高度
     */
    public static int getNavigationBarHeight(Context mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getScreenH() {
        return ApplicationBase.screen_height + getNavigationBarHeight(ApplicationBase.getAppContext());
    }
}
