package cn.egoa.sharehelper;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

import cn.egoa.sharehelper.utils.LogUtils;


/**
 * Created by Ryan on 2017/12/15.
 */
public class ApplicationBase extends Application {
    private static Application ctx;
    @Override
    public void onCreate() {
        super.onCreate();
        ctx=this;
        LogUtils.init(this);
        CrashReport.initCrashReport(getApplicationContext(), "1807110e8e", LogUtils.isApkDebugable(this)/**建议在测试阶段建议设置成true，发布时设置为false*/);
    }
    public static Application getAppContext(){
        return ctx;
    }
}
