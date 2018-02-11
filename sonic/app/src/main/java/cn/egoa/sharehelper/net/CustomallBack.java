package cn.egoa.sharehelper.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Ryan on 2017/12/16.
 */

public abstract class CustomallBack implements Callback {

    @Override
    public void onFailure(Call call, IOException e) {
        onErroe(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        onComplete(call, response);
    }

    public abstract void onErroe(Call call, IOException e);

    public abstract void onComplete(Call call, Response response);
}
