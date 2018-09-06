package com.coreasp_sdk;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.coreasp.CorePushAppLaunchAnalyticsManager;
import com.coreasp.CorePushManager;
import com.coreasp.CorePushRegisterUserAttributeManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 設定画面と通知履歴画面のタブを持つアクティビティ
 */
public class MainActivity extends TabActivity {

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 9000;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google Playサービスが利用可能か確認する
        checkGooglePlayServicesAvailable();

        CorePushManager manager = CorePushManager.getInstance();

        // アプリ内のユーザーIDを指定
        manager.setAppUserId("userid");

//        // 1:地域、2:性別 3:年代 4:好きなジャンル(複数選択可の場合)
//        HashMap<String, List<String>> multiCategoryIds = new HashMap<>();
//        multiCategoryIds.put("1", Arrays.asList("神奈川"));
//        multiCategoryIds.put("2", Arrays.asList("男性"));
//        multiCategoryIds.put("3", Arrays.asList("20代"));
//        multiCategoryIds.put("4", Arrays.asList("音楽", "読書"));
//        manager.setMultiCategoryIds(multiCategoryIds);
//
//        // 1:北海道、2:東北 3:関東、4:近畿
//        List<String> categoryIds = Arrays.asList("1", "2", "3", "4");
//        manager.setCategoryIds(categoryIds);

        // 通知センターから起動時に通知パラメータを取得
        Intent intent = getIntent();
        String date = manager.getDate(intent);
        String title = manager.getTitle(intent);
        String message = manager.getMessage(intent);
        String url = manager.getUrl(intent);

        // intentオブジェクトから通知IDを取得する
        String pushId = manager.getPushId(intent);

        if (pushId != null) {
            CorePushAppLaunchAnalyticsManager appLaunchAnalyticsManager = new CorePushAppLaunchAnalyticsManager(this);
            appLaunchAnalyticsManager.requestAppLaunchAnalytics(pushId, "0", "0", new CorePushAppLaunchAnalyticsManager.CompletionHandler() {
                @Override
                public void appLaunchAnalyticsManagerSuccess() {
                    Toast.makeText(MainActivity.this, "起動数送信成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void appLaunchAnalyticsManagerFail() {
                    Toast.makeText(MainActivity.this, "起動数送信失敗", Toast.LENGTH_SHORT).show();

                }
            });
        }

//        // ユーザー属性登録
//        List<String> attributes = Arrays.asList("1,3,7"); // 1,3,7のユーザー属性登録の場合
//        String attributeRegisterUrl = "[御社にて任意のURLをご用意ください]"; // ユーザー属性登録の任意のURL
//        CorePushRegisterUserAttributeManager userAttributeManager = new CorePushRegisterUserAttributeManager(this);
//        userAttributeManager.registerUserAttributes(attributes, attributeRegisterUrl, new CorePushRegisterUserAttributeManager.CompletionHandler() {
//            @Override
//            public void registerUserAttributeManagerSuccess() {
//                Toast.makeText(HomeActivity.this, "ユーザー属性登録成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void registerUserAttributeManagerFail() {
//                Toast.makeText(HomeActivity.this, "ユーザー属性登録失敗", Toast.LENGTH_SHORT).show();
//            }
//        });

        // タブ画面を初期化
        this.initTabs();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Google Playサービスが利用可能か確認する
        checkGooglePlayServicesAvailable();
    }

    protected void initTabs() {
        Resources res = getResources();
        TabHost tabHost = getTabHost();

        TabHost.TabSpec spec;
        Intent intent;

        // 設定画面
        intent = new Intent().setClass(this, SettingActivity.class);
        spec = tabHost.newTabSpec("設定")
                .setIndicator("設定", res.getDrawable(R.mipmap.ic_launcher))
                .setContent(intent);
        tabHost.addTab(spec);

        // 履歴画面
        intent = new Intent().setClass(this, HistoryActivity.class);
        spec = tabHost.newTabSpec("通知履歴")
                .setIndicator("通知履歴", res.getDrawable(R.mipmap.ic_launcher))
                .setContent(intent);

        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);
    }

    /**
     * Google Play servicesが利用可能かを確認する。
     */
    private void checkGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("CorePushSample", "Google Play Serviceを利用できます。");
        } else {
            Log.e("CorePushSample", "Google Play Serviceを利用できません。");

            if (apiAvailability.isUserResolvableError(resultCode)) {
                if(apiAvailability.isUserResolvableError(resultCode)) {
                    apiAvailability.getErrorDialog(this, resultCode,
                            REQUEST_GOOGLE_PLAY_SERVICES).show();
                }
            } else {
                finish();
            }
        }
    }

}
