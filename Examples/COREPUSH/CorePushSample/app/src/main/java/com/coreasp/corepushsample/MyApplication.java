package com.coreasp.corepushsample;

import android.app.Application;
import com.coreasp.CoreAspManager;

/**
 * Applicationのカスタムクラス
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // COREASPマネージャーの初期化処理
        CoreAspManager.initialize(this);
    }
}
