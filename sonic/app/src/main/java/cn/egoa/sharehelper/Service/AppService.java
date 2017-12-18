package cn.egoa.sharehelper.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.egoa.sharehelper.rxbus.RxBus;
import cn.egoa.sharehelper.rxbus.event.ChangeEvent;

/**
 * Created by Ryan on 2017/12/18.
 */
public class AppService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ChangeEvent changeEvent = new ChangeEvent();
        changeEvent.putString("name", "ouyang");
        RxBus.getDefault().post(changeEvent);
        return START_REDELIVER_INTENT;
    }
}
