package cn.egoa.sharehelper.net;

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
}
