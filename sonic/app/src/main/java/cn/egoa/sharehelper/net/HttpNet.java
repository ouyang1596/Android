package cn.egoa.sharehelper.net;

import cn.egoa.sharehelper.ApplicationBase;
import cn.egoa.sharehelper.utils.AppInfoUtil;
import cn.egoa.sharehelper.utils.AppTool;

/**
 * Created by Ryan on 2017/12/16.
 * 网络请求接口
 */

public class HttpNet {
    private static String address = "https://api.egoa.cn/";

    /**
     * 客户端初始化接口
     */
    public static String init() {
        return address + "sys/init.php";
    }

    /**
     * 取得网络请求的UA
     */
    public static String getUserAgent() {
        return AppInfoUtil.getPackageName() + " (Android; OS/" + android.os.Build.VERSION.SDK + "; Branchs " + AppInfoUtil.getSystemInfo() + ") Version/" + AppInfoUtil.getVersionName() + " Device/" + ApplicationBase.screen_width + "x" + AppTool.getScreenH() + " Ca/" + "0";

    }
}
