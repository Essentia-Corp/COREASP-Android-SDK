package com.coreasp_sdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.coreasp.CorePushManager;
import com.coreasp.CorePushNotificationHistoryManager;
import com.coreasp.CorePushNotificationHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // マネージャーの初期化処理
        CorePushManager.getInstance();
        Log.d("configKey", CorePushManager.getInstance().getConfigKey());
        Log.d("reousrceId", String.valueOf(CorePushManager.getInstance().getIconResourceId()));
//        Log.d("activity", CorePushManager.getInstance().getActivity().getName());
//        Log.d("notificationStyle", String.valueOf(CorePushManager.getInstance().getNotificationStyle()));

        CorePushManager.getInstance().registerToken();

//        final CorePushNotificationHistoryManager manager = new CorePushNotificationHistoryManager(this);

        final CorePushNotificationHistoryManager manager = new CorePushNotificationHistoryManager(this);
        manager.requestNotificationHistory(new CorePushNotificationHistoryManager.CompletionHandler() {
            @Override
            public void notificationHistoryManagerSuccess(List<CorePushNotificationHistoryModel> notificationHistoryModelList) {
                List<CorePushNotificationHistoryModel> list = notificationHistoryModelList;
                String a = "";
            }


            @Override
            public void notificationHistoryManagerFail() {
                String a = "";
            }
        });
    }
}
