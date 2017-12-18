package cn.egoa.sharehelper.utils;

import android.content.pm.PackageManager;
import android.os.Build;

import cn.egoa.sharehelper.ApplicationBase;

/**
 * Created by Ryan on 2017/12/17.
 */

public class AppInfoUtil {
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

    /**
     * 返回设置相关信息项
     *
     * @return
     */
    public static String getSystemInfo() {
        StringBuilder sb = new StringBuilder();
        String manufString = Build.MANUFACTURER; // 制造商
        sb.append(manufString);
        String model = Build.MODEL; // 型号
        if (model == null || "".equals(model)) {
            model = Build.BOARD;
        }
        sb.append(":").append(model.replace("-", ""));
        return sb.toString();
    }
}
