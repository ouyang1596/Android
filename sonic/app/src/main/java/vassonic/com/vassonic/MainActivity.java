package vassonic.com.vassonic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mosheng.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button login_button_wx;
    private Button login_button_qq;
    public static final String qq_appid = "1104301257";
    /**
     * 腾讯SDK对象实例
     */
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTencent = Tencent.createInstance(qq_appid, MainActivity.this);
        initView();
    }

    private void initView() {
        login_button_wx = findViewById(R.id.login_button_wx);
        login_button_wx.setOnClickListener(this);
        login_button_qq = findViewById(R.id.login_button_qq);
        login_button_qq.setOnClickListener(this);
    }

    private IWXAPI api;
    public static final String wx_appid = "wxc2cca9846361b1f6";// 微信appid

    public void loginFromWeixin() {
        api = WXAPIFactory.createWXAPI(this, wx_appid, true);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        api.sendReq(req);
    }

    /**
     * 腾讯用户信息
     */
    private UserInfo mInfo;
    /**
     * api权限
     */
    public static String SCOPE = "all";

    private void onClickLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, SCOPE, loginListener);
        } else {
            mTencent.logout(this);
            updateUserInfo();
        }
    }

    IUiListener loginListener = new BaseUiListener() {

        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };
    private String token = "";
    private String type = "";
    private String openid = "";

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String tokened = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(tokened) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                // Toast.makeText(this, "登录成功:" + tokened,
                // Toast.LENGTH_SHORT).show();
                type = "qq";
                token = tokened;
                openid = openId;
                Toast.makeText(this, "getOtherLogin", Toast.LENGTH_LONG).show();
//                getOtherLogin();
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                // Toast.makeText(LoginActivity.this, "返回为空登录失败：",
                // Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                // Toast.makeText(LoginActivity.this, "返回为空登录失败：",
                // Toast.LENGTH_LONG).show();

                return;
            }
            // Toast.makeText(LoginActivity.this, "登录成功：",
            // Toast.LENGTH_LONG).show();
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {
            Toast.makeText(MainActivity.this, "登录失败：",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(MainActivity.this, "登录失败：" + e.errorDetail,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "登录失败：",
                    Toast.LENGTH_LONG).show();
        }
    }

    /******************************************
     * QQ登录
     *************************************/
    private void updateUserInfo() {
        if (mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject) response;
                    try {
                        if (json.has("nickname")) {
                            String qq_nickname = json.getString("nickname");
                            OtherLoginInfo.nickName = qq_nickname;
                        }
                        if (json.has("gender")) {
                            String gender = json.getString("gender");
                            OtherLoginInfo.sex = gender;
                        }
                        if (json.has("figureurl_qq_2")) {
                            OtherLoginInfo.headImage = json.getString("figureurl_qq_2");
                        }
//                        ApplicationBase.ctx.sendBroadcast(new Intent(BoardCastContacts.USERDETAIL_USERINFO_UPDATE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancel() {
                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button_wx:
                loginFromWeixin();
                break;
            case R.id.login_button_qq:
                onClickLogin();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }
}
