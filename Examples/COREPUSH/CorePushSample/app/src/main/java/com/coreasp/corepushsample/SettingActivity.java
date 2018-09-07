package com.coreasp.corepushsample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.coreasp.CorePushManager;

/**
 * 通知設定画面のアクティビティ
 */
public class SettingActivity extends Activity {

    // トークン登録成功のコールバック用のレシーバー
    private BroadcastReceiver mRegistrationSuccessBroadcastReceiver;

    // トークン登録失敗のコールバック用のレシーバー
    private BroadcastReceiver mRegistrationFailBroadcastReceiver;

    // トークン解除成功のコールバック用のレシーバー
    private BroadcastReceiver mUnregistrationSuccessBroadcastReceiver;

    // トークン解除失敗のコールバック用のレシーバー
    private BroadcastReceiver mUnregistrationFailBroadcastReceiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        // トークン登録成功後のコールバック処理
        mRegistrationSuccessBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateView();
                Toast.makeText(SettingActivity.this, "トークン登録成功", Toast.LENGTH_SHORT).show();
            }
        };

        // トークン登録失敗後のコールバック処理
        mRegistrationFailBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateView();
                Toast.makeText(SettingActivity.this, "トークン登録失敗", Toast.LENGTH_SHORT).show();
            }
        };

        // トークン登録成功後のコールバック処理
        mUnregistrationSuccessBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateView();
                Toast.makeText(SettingActivity.this, "トークン解除成功", Toast.LENGTH_SHORT).show();
            }
        };

        // トークン登録失敗後のコールバック処理
        mUnregistrationFailBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateView();
                Toast.makeText(SettingActivity.this, "トークン解除失敗", Toast.LENGTH_SHORT).show();
            }
        };

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);

        // デバイストークンをCORE ASPサーバに送信済みか
        boolean isTokenSentToServer = CorePushManager.getInstance().isTokenSentToServer();

        if (isTokenSentToServer) {
            // デバイストークンを送信済みの場合は、通知設定をON
            checkBox.setChecked(true);
        } else {
              // デバイストークンを未送信の場合は、通知設定をOFF
            checkBox.setChecked(false);
        }

        // チェックボックスがクリックされた場合の動作を定義
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;

                //プッシュ通知の状態を取得
                boolean checked = checkBox.isChecked();

                CorePushManager manager = CorePushManager.getInstance();

                //プッシュ通知がONの場合
                if (checked) {
                    // CORE ASPサーバにデバイストークンを登録
                    manager.registerToken();
                    return;
                }

                //プッシュ通知がOFFの場合
                // CORE PUSHからデバイストークンを削除
                manager.unregisterToken();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // トークン登録成功後のコールバック用のレシーバーを登録
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationSuccessBroadcastReceiver,
                new IntentFilter(CorePushManager.COREPUSH_REGISTRATION_TOKEN_REQUEST_SUCCESS));

        // トークン登録成功後のコールバック用のレシーバーを登録
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationFailBroadcastReceiver,
                new IntentFilter(CorePushManager.COREPUSH_REGISTRATION_TOKEN_REQUEST_FAIL));

        // トークン解除成功後のコールバック用のレシーバーを登録
        LocalBroadcastManager.getInstance(this).registerReceiver(mUnregistrationSuccessBroadcastReceiver,
                new IntentFilter(CorePushManager.COREPUSH_UNREGISTRATION_TOKEN_REQUEST_SUCCESS));

        // トークン解除失敗後のコールバック用のレシーバーを登録
        LocalBroadcastManager.getInstance(this).registerReceiver(mUnregistrationFailBroadcastReceiver,
                new IntentFilter(CorePushManager.COREPUSH_UNREGISTRATION_TOKEN_REQUEST_FAIL));
    }

    @Override
    protected void onPause() {
        super.onPause();

        // トークン登録成功後のコールバック用のレシーバーを解除
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationSuccessBroadcastReceiver);

        // トークン登録失敗後のコールバック用のレシーバーを解除
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationFailBroadcastReceiver);

        // トークン解除成功後のコールバック用のレシーバーを解除
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUnregistrationSuccessBroadcastReceiver);

        // トークン解除失敗後のコールバック用のレシーバーを解除
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUnregistrationFailBroadcastReceiver);
    }

    /**
     * ビューを更新する。
     */
    private void updateView() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);

        // デバイストークンをCORE ASPサーバに送信済みか
        boolean isTokenSentToServer = CorePushManager.getInstance().isTokenSentToServer();

        if (isTokenSentToServer) {
            // デバイストークンを送信済みの場合は、通知設定をON
            checkBox.setChecked(true);
        } else {
            // デバイストークンを未送信の場合は、通知設定をOFF
            checkBox.setChecked(false);
        }
    }
}