package cn.egoa.sharehelper.utils;

import android.content.pm.PackageManager;

import cn.egoa.sharehelper.ApplicationBase;

/**
 * Created by Ryan on 2017/12/17.
 */

public class AppInfoUtils {
    public static String getPackageName() {
        return ApplicationBase.getAppContext().getPackageName();
    }

    public static String getVersionName() {
        try {
            String versionName = ApplicationBase.getAppContext().getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0";
        }
    }

    public static int getVersionCode() {
        try {
            int versionCode = ApplicationBase.getAppContext().getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }

    }
}
