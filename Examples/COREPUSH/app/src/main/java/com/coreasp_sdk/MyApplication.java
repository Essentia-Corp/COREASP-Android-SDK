package com.coreasp_sdk;

import android.app.Application;

import com.coreasp.CoreAspManager;
import com.coreasp.CorePushLocationManager;
import com.coreasp.CorePushManager;

/**
 * Applicationのカスタムクラス
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // マネージャーの初期化処理
        CoreAspManager.initialize(this);

    }
}


