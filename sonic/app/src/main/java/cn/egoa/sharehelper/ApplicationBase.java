package cn.egoa.sharehelper;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.greendao.database.Database;

import cn.egoa.sharehelper.Service.AppService;
import cn.egoa.sharehelper.grendaoentity.DaoMaster;
import cn.egoa.sharehelper.grendaoentity.DaoSession;
import cn.egoa.sharehelper.utils.LogUtil;

/**
 * Created by Ryan on 2017/12/15.
 */
public class ApplicationBase extends Application {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = true;//greenDao加密与否
    private DaoSession daoSession;
    private static Application ctx;
    // 屏幕宽度
    public static int screen_width;
    // 屏幕内容高度
    public static int screen_height;//包括顶部状态栏，不包括底部导航栏高度
    // 屏幕密度
    public static float density = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        LogUtil.init(this);
        CrashReport.initCrashReport(getApplicationContext(), "1807110e8e", LogUtil.isApkDebugable(this)/**建议在测试阶段建议设置成true，发布时设置为false*/);
        startService(new Intent(this, AppService.class));
        getScreenWH();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "sharehelper-db-encrypted" : "sharehelper-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Application getAppContext() {
        return ctx;
    }

    private void getScreenWH() {
        // 获取手机分辨率
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager winManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
    }
}
