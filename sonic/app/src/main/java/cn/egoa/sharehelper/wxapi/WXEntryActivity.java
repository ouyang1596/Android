package cn.egoa.sharehelper.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.egoa.sharehelper.constant.Constant;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.entity_weigxin_dev);
        //
        // MyToast.SystemToast(this, "onCreate()", Toast.LENGTH_LONG);
        // // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.wx_appid, true);
        // // 将该app注册到微信
        api.registerApp(Constant.wx_appid);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onDestroy() {
        api.unregisterApp();
        super.onDestroy();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        // MyToast.SystemToast(this, "onReq()", Toast.LENGTH_LONG);
        switch (req.getType()) {
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        // ToastUtil.showToast("onResp：：errCode" + resp.errCode + "::getType::"
        // + resp.getType());

//        BoardCastManager.sendBoardCastStartLive();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                    // 分享成功提交后台
//                    if (ShareUtils.weixinOrWeixinFriend == 0) {
//                        ShareUtils.uploadService("wx_friend");
//                    } else {
//                        ShareUtils.uploadService("wx_pyq");
//                    }
//                    BoardCastManager.sendBoardCastShowShareResult(1, 1);
                    finish();
                } else {
                    SendAuth.Resp ss = (SendAuth.Resp) resp;
                    String code = ss.code;
                    String state = ss.state;
                    Toast.makeText(this, "code：" + code + "--state--" + ss.state, Toast.LENGTH_LONG).show();
//                    MyToast.SystemToast(this, "code：" + code + "--state--" + ss.state,
//                            Toast.LENGTH_LONG);
//                    getTokenFromWX(code, state);
//                    BoardCastManager.sendBoardCastShowShareResult(1, 0);
                }

                return;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // result = R.string.errcode_cancel;
//                 MyToast.SystemToast(this, "分享失败：", Toast.LENGTH_LONG);
//                BoardCastManager.sendBoardCastShowShareResult(1, 0);
                finish();
                return;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                // result = "分离失败，用户认证错误：";
//                 MyToast.SystemToast(this, "分离失败，用户认证错误:", Toast.LENGTH_LONG);
//                BoardCastManager.sendBoardCastShowShareResult(1, 0);
                finish();
                break;
            default:
                // result = "分享失败，未知错误：";
//                 MyToast.SystemToast(this, "分享失败，未知错误：", Toast.LENGTH_LONG);
//                BoardCastManager.sendBoardCastShowShareResult(1, 0);
                finish();
                break;
        }
    }

//    public void getTokenFromWX(final String code, final String state) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                RequestCallBackInfo requestCallBackInfo = new UserLoginBiz().getTokenFromWX(code, state);
//                if (requestCallBackInfo.RequestStatus && requestCallBackInfo.ServerStatusCode == 200) {
//                    try {
//                        String result = requestCallBackInfo.ServerCallBackInfo;
//                        if (!StringUtil.StringEmpty(result)) {
//                            JSONObject docsss = OperateJson.ReadJsonString(result, false);
//                            String access_token = OperateJson.getString(docsss, "access_token");
//                            String uid = OperateJson.getString(docsss, "openid");
//                            if (!StringUtil.StringEmpty(access_token) && !StringUtil.StringEmpty(uid)) {
//                                BoardCastManager.getWXtoken(access_token, uid);
//                            }
//                        }
//                    } catch (Exception e) {
//                        AppLogs.PrintException(e);
//                    }
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                    }
//                });
//            }
//        }).start();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
