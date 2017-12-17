package cn.egoa.sharehelper;

import android.app.Application;

import cn.egoa.sharehelper.utils.LogUtils;


/**
 * Created by Ryan on 2017/12/15.
 */
public class ApplicationBase extends Application {
    public static Application ctx;
    @Override
    public void onCreate() {
        super.onCreate();
        ctx=this;
        LogUtils.init(this);
    }
    public static Application getAppContext(){
        return ctx;
    }
}
