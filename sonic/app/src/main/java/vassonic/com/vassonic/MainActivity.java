package vassonic.com.vassonic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mosheng.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button login_button_wx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        login_button_wx = findViewById(R.id.login_button_wx);
        login_button_wx.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button_wx:
                Log.d("Ryan_", "weixin");
                loginFromWeixin();
                break;
        }
    }
}
